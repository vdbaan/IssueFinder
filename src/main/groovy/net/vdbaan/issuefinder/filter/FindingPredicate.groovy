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

    SCANNER('SCANNER'), IP('IP'), PORT('PORT'), SERVICE('SERVICE'), RISK('RISK'),
    EXPLOITABLE('EXPLOITABLE'), DESCRIPTION('DESCRIPTION'), PLUGIN('PLUGIN'),
    STATUS('STATUS'), PROTOCOL('PROTOCOL'), HOSTNAME('HOSTNAME'), CVSS('CVSS')

    private static final Map<String, ColumnName> ENUM_MAP

    private String value

    static {
        final Map<String, ColumnName> map = new ConcurrentHashMap<>()
        for (final ColumnName instance : values()) {
            map[instance.value] = instance
        }
        ENUM_MAP = Collections.unmodifiableMap(map)
    }

    static ColumnName get(final String name) {
        return ENUM_MAP[name]
    }

    ColumnName(final String value) {
        this.value = value
    }

    String getValue() {
        return value
    }

    @Override
    String toString() {
        return getValue()
    }

    Object get(final Finding finding) {
        switch (this) {
            case SCANNER: return finding.scanner
            case IP: return finding.ip
            case PORT: return finding.port
            case SERVICE: return finding.service
            case RISK: return finding.severity.toString()
            case EXPLOITABLE: return finding.exploitable
            case DESCRIPTION: return finding.fullDescription()
            case STATUS: return finding.portStatus
            case PROTOCOL: return finding.protocol
            case HOSTNAME: return finding.hostName
            default: return null
        }
    }

}

@Log
@SuppressWarnings('Instanceof')
class FindingPredicate implements Predicate<Finding> {

    @SuppressWarnings('ClassStartsWithBlankLine')
    enum LogicalOperation {
        
        LT('<'), LE('<='), GT('>'), GE('>='), EQ('=='), NE('!='), LIKE('LIKE'), APROX('~='), NOT('!'),
        AND('&&'), OR('||'), IN('IN'), BETWEEN('BETWEEN'), NLIKE('NOT LIKE')

        private static final Map<String, LogicalOperation> ENUM_MAP

        final String representation

        static {
            final Map<String, LogicalOperation> map = new ConcurrentHashMap<>()
            for (final LogicalOperation instance : values()) {
                map[instance.representation] = instance
            }
            ENUM_MAP = Collections.unmodifiableMap(map)
        }

        static LogicalOperation get(final String name) {
            return ENUM_MAP[name]
        }

        LogicalOperation(final String s) {
            representation = s
        }

        String getRepresentation() {
            return representation
        }

    }

    FindingPredicate() {}

    FindingPredicate(final Object left, final LogicalOperation operation, final Object right) {
        this.left = left
        this.operation = operation
        this.right = right
    }

    Object left
    LogicalOperation operation
    Object right

    boolean test(final Finding finding) {
        Object lValue = this.left, rValue = this.right
        if (left instanceof ColumnName) {
            lValue = ((ColumnName) left).get(finding)
        }

        if (right instanceof ColumnName) {
            rValue = ((ColumnName) right).get(finding)
        }
        if (lValue == null || (rValue == null && operation != LogicalOperation.NOT)) {
            return false
        }
        if (ColumnName.RISK == left) {
            rValue = rValue.toString().toUpperCase()
        }
        switch (operation) {
            case LogicalOperation.AND: return ((FindingPredicate) lValue).and((FindingPredicate) rValue).test(finding)
            case LogicalOperation.OR: return ((FindingPredicate) lValue).or((FindingPredicate) rValue).test(finding)

            case LogicalOperation.EQ: return lValue.equals(rValue)
            case LogicalOperation.NE: return !lValue.equals(rValue)
            case LogicalOperation.LIKE: return ((String) lValue).contains(rValue)

            case LogicalOperation.LT: return compare(lValue, rValue) < 0
            case LogicalOperation.LE: return compare(lValue, rValue) <= 0
            case LogicalOperation.GT: return compare(lValue, rValue) > 0
            case LogicalOperation.GE: return compare(lValue, rValue) >= 0

            case LogicalOperation.NOT: return (lValue instanceof FindingPredicate) ?
                    ((FindingPredicate) lValue).negate().test(finding) : !((Boolean) lValue).booleanValue()

            case LogicalOperation.IN: return ((List) rValue).contains(lValue)
            case LogicalOperation.BETWEEN: return between(lValue, rValue)
            default: return true

        }
    }

    private int compare(final Object lValue, final Object rValue) {
        if (isInteger(lValue)) {
            return Integer.compare(lValue as Integer, rValue as Integer)
        }
        switch (lValue.class) {
            case Finding.Severity: return ((Finding.Severity) lValue).compareTo((Finding.Severity) rValue)
            default: return ((String) lValue).compareTo(rValue)
        }
    }

    private boolean isInteger(final String value) {
        if (value == null) {
            log.log('NULL is not a number')
            return false
        }
        try {
            Integer.parseInt(value)
        } catch (final NumberFormatException e) {
            log.log(Level.FINE, 'Got an exception', e)
            return false
        } 
        // only got here if we didn't return false
        return true
    }

    boolean between(final Object column, final Object value) {
        final List list = (List) value
        final Object low = list[0]
        final Object high = list[1]
        return ((String) column).compareTo(low) >= 0 &&
                ((String) column).compareTo(high) <= 0
    }

    String toString() {
        if (left == null && operation == null && right == null) {
            return 'EMPTY INITIALIZED FINDING PREDICATE'
        }
        if (left instanceof FindingPredicate && right instanceof FindingPredicate) {
            return String.format('(%s) %s (%s)', left, operation.representation, right)
        }
        if (left == ColumnName.EXPLOITABLE) {
            return (operation == null)? 'EXPLOITABLE IS TRUE': 'EXPLOITABLE IS FALSE'
        }
        Object rightval
        switch (left) {
            case { it instanceof ColumnName && it == ColumnName.RISK }:
                if (right instanceof List) {
                    rightval = ((List<String>) right).collect { String.format('\'%s\'', it.trim().toUpperCase()) }
                } else {
                    rightval = String.format('\'%s\'', right.toString().toUpperCase())
                }
                break
            case { it instanceof ColumnName && it == ColumnName.CVSS }:
                break
            default:
                if (right instanceof List) {
                    rightval = ((List<String>) right).collect { String.format('\'%s\'', it.trim()) }
                } else {
                    rightval = String.format('\'%s\'', right.toString())
                }
        }

        switch (operation) {
            case LogicalOperation.LIKE:
            case LogicalOperation.NLIKE:
                rightval = '\'' + rightval.toString().replace('\'', '%') + '\''
                break
            case LogicalOperation.IN:
                rightval = String.format('(%s)', ((List<String>) rightval).join(', '))
                break
            case LogicalOperation.BETWEEN:
                rightval = String.format('%s', ((List<String>) rightval).join(' AND '))
                break
            default:
                break
        }
        return String.format('%s %s %s', left, operation.representation, rightval)
    }

}
