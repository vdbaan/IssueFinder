/*
 *  Copyright (C) 2017  S. van der Baan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.vdbaan.issuefinder.filter

import groovy.transform.CompileStatic
import groovy.util.logging.Log
import net.vdbaan.issuefinder.model.Finding

import java.util.concurrent.ConcurrentHashMap
import java.util.function.Predicate
import java.util.logging.Level

@Log
@CompileStatic
enum ColumnName {
    SCANNER("SCANNER"), IP("IP"), PORT("PORT"), SERVICE("SERVICE"), RISK("RISK"),
    EXPLOITABLE("EXPLOITABLE"), DESCRIPTION("DESCRIPTION"), PLUGIN('PLUGIN'),
    STATUS("STATUS"), PROTOCOL("PROTOCOL"), HOSTNAME("HOSTNAME"), CVSS('CVSS')
    private String value
    private static final Map<String, ColumnName> ENUM_MAP

    ColumnName(String value) {
        this.value = value
    }

    String getValue() {
        return value
    }

    @Override
    String toString() {
        return getValue()
    }

    static {
        Map<String, ColumnName> map = new ConcurrentHashMap<>()
        for (ColumnName instance : values()) {
            map.put(instance.getValue(), instance)
        }
        ENUM_MAP = Collections.unmodifiableMap(map)
    }

    static ColumnName get(String name) {
        return ENUM_MAP.get(name)
    }

    Object get(Finding f) {
        switch (this) {
            case SCANNER: return f.scanner
            case IP: return f.ip
            case PORT: return f.port
            case SERVICE: return f.service
            case RISK: return f.severity.toString()
            case EXPLOITABLE: return f.exploitable
            case DESCRIPTION: return f.fullDescription()
            case STATUS: return f.portStatus
            case PROTOCOL: return f.protocol
            case HOSTNAME: return f.hostName
            default: return null
        }
    }
}

@Log
class FindingPredicate implements Predicate<Finding> {
    enum LogicalOperation {
        LT("<"), LE("<="), GT(">"), GE(">="), EQ("=="), NE("!="), LIKE("LIKE"), APROX("~="), NOT("!"),
        AND("&&"), OR("||"), IN("IN"), BETWEEN("BETWEEN")
        final String representation

        private static final Map<String, LogicalOperation> ENUM_MAP

        LogicalOperation(String s) {
            representation = s
        }

        String getRepresentation() {
            return representation
        }

        static {
            Map<String, LogicalOperation> map = new ConcurrentHashMap<>()
            for (LogicalOperation instance : values()) {
                map.put(instance.getRepresentation(), instance)
            }
            ENUM_MAP = Collections.unmodifiableMap(map)
        }

        static LogicalOperation get(String name) {
            return ENUM_MAP.get(name)
        }
    }

    FindingPredicate(Object left, LogicalOperation operation, Object right) {
        this.left = left
        this.operation = operation
        this.right = right
    }

    Object left
    LogicalOperation operation
    Object right

    boolean test(Finding f) {
        Object lValue = this.left, rValue = this.right
        if (left instanceof ColumnName) {
            lValue = ((ColumnName) left).get(f)
        }

        if (right instanceof ColumnName) {
            rValue = ((ColumnName) right).get(f)
        }
        if (lValue == null || (rValue == null && operation != LogicalOperation.NOT)) {
            return false
        }
        if (ColumnName.RISK == left) {
            rValue = rValue.toString().toUpperCase()
        }
        switch (operation) {
            case LogicalOperation.AND: return ((FindingPredicate) lValue).and((FindingPredicate) rValue).test(f)
            case LogicalOperation.OR: return ((FindingPredicate) lValue).or((FindingPredicate) rValue).test(f)

            case LogicalOperation.EQ: return lValue.equals(rValue)
            case LogicalOperation.NE: return !lValue.equals(rValue)
            case LogicalOperation.LIKE: return ((String) lValue).contains(rValue)

            case LogicalOperation.LT: return compare(lValue, rValue) < 0
            case LogicalOperation.LE: return compare(lValue, rValue) <= 0
            case LogicalOperation.GT: return compare(lValue, rValue) > 0
            case LogicalOperation.GE: return compare(lValue, rValue) >= 0

            case LogicalOperation.NOT: return (lValue instanceof FindingPredicate) ?
                    ((FindingPredicate) lValue).negate().test(f) : !((Boolean) lValue).booleanValue()


            case LogicalOperation.IN: return ((List) rValue).contains(lValue)
            case LogicalOperation.BETWEEN: return between(lValue, rValue)
            default: return true

        }
    }

    private int compare(Object lValue, Object rValue) {
        if (isInteger(lValue)) {
            return Integer.compare(lValue as Integer, rValue as Integer)
        }
        switch (lValue.class) {
            case Finding.Severity: return ((Finding.Severity) lValue).compareTo((Finding.Severity) rValue)
            default: return ((String) lValue).compareTo(rValue)
        }
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value)
        } catch (NumberFormatException e) {
            log.log(Level.FINE, 'Got an exception', e)
            return false
        } catch (NullPointerException e) {
            log.log(Level.FINE, 'Got an exception', e)
            return false
        }
        // only got here if we didn't return false
        return true
    }

    boolean between(Object column, Object value) {
        List list = (List) value
        Object low = list.get(0)
        Object high = list.get(1)
        return ((String) column).compareTo(low) >= 0 &&
                ((String) column).compareTo(high) <= 0
    }

    String toString() {
        if (operation == LogicalOperation.NOT) {
            return "!" + ((left instanceof FindingPredicate) ? '(' + left + ')' : left)
        }
        if (left instanceof FindingPredicate && right instanceof FindingPredicate) {
            return String.format("(%s) %s (%s)", left, operation.representation, right)
        }
        String rightval
        switch (operation) {
            case LogicalOperation.IN:
            case LogicalOperation.BETWEEN:
                rightval = String.format("(%s)",((List<String>)right)*.trim().join(", "))
                break
            default:
                rightval = right.toString()
        }
        switch (left) {
            case { it instanceof ColumnName && it == ColumnName.RISK }:
                if(right instanceof List) {
                    rightval = String.format("('%s')",((List<String>)right)*.trim().join("', '")).toUpperCase()
                }
                else
                rightval = String.format("'%s'", rightval.toUpperCase())
                break
            case { it instanceof ColumnName && it == ColumnName.CVSS }:
                break
            default:
                rightval = String.format("'%s'", rightval)
        }
        return String.format("%s %s %s", left, operation.representation,rightval)
//        if (left instanceof ColumnName) {
//            if (operation == LogicalOperation.NOT) {
//                return "!" + ((ColumnName) left)
//            } else {
//                switch (left) {
//                    case {it instanceof ColumnName && it == ColumnName.RISK}:
//                        return String.format("%s %s '%s'", (left instanceof FindingPredicate) ? "(" + left + ")" : left, operation.representation, right.toString().toUpperCase())
//                    case {it instanceof ColumnName && it == ColumnName.CVSS}:
//                        return String.format("%s %s %s", (left instanceof FindingPredicate) ? "(" + left + ")" : left, operation.representation, right)
//                    default:
//                        return String.format("%s %s %s", (left instanceof FindingPredicate) ? "(" + left + ")" : left, operation.representation, (right instanceof FindingPredicate) ? "(" + right + ")" : "'" + right + "'")
//                }
//            }
//        } else if (left instanceof FindingPredicate) {
//            if (operation == LogicalOperation.NOT) {
//                return "!(" + left.toString() + ")"
//            } else {
//                return String.format("%s %s %s", (left instanceof FindingPredicate) ? "(" + left + ")" : left, operation.representation, (right instanceof FindingPredicate) ? "(" + right + ")" : "'" + right + "'")
//            }
//        } else {
//            return ""
//        }

    }
}
