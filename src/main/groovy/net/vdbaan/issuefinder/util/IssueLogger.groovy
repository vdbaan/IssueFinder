/**
 * Copyright (C) 2017 S. van der Baan

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.vdbaan.issuefinder.util


import java.util.logging.*

class IssueLogger {
    static void setup(String... args) throws IOException {

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("")
        rootLogger.getHandlers().each {
            rootLogger.removeHandler(it)
        }

        final ConsoleHandler consoleHandler = new ConsoleHandler()
        consoleHandler.setLevel(Level.FINEST)
        consoleHandler.setFormatter(new IssueFormatter())
        rootLogger.addHandler(consoleHandler)
        if (args.contains('--debug')) {
            rootLogger.setLevel(Level.FINER)
        } else
            rootLogger.setLevel(Level.INFO)
    }
}

class IssueFormatter extends SimpleFormatter {
    private static final String format = '%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s] (%2$s) %5$s %6$s%n'
    private final Date dat = new Date()

    synchronized String format(LogRecord record) {
        dat.setTime(record.getMillis())
        String source = getSource()
        String message = formatMessage(record)
        String throwable = ""
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter()
            PrintWriter pw = new PrintWriter(sw)
            pw.println()
            record.getThrown().printStackTrace(pw)
            pw.close()
            throwable = sw.toString()
        }
        return String.format(format,
                dat,
                source,
                record.getLoggerName(),
                record.getLevel().getLocalizedLevelName(),
                message,
                throwable)
    }

    String getSource() {
        def st = Thread.currentThread().getStackTrace()
        int pos = 0
        while (!st[pos].declaringClass.equals('java.util.logging.Logger') && !st[pos].methodName.equals('doLog'))
            pos += 1
        pos += 4
        if (!st[pos].declaringClass.startsWith('java_util_logging_Logger$'))
            return String.format("%s:%d", st[pos].methodName, st[pos].lineNumber)
        pos += 1
        while (!st[pos].declaringClass.equals('org.codehaus.groovy.runtime.callsite.AbstractCallSite') && !st[pos].methodName.equals('call') && st[pos].lineNumber != 125)
            pos += 1
        pos += 2
        return String.format("%s:%d", st[pos].methodName, st[pos].lineNumber)
    }
}