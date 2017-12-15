package net.vdbaan.issuefinder.parser

import net.vdbaan.issuefinder.model.Finding

import java.util.zip.ZipEntry
import java.util.zip.ZipException
import java.util.zip.ZipFile

class BurpStateParser extends Parser {

    static scanner = "BurpState"

    BurpStateParser(File file) {
        this.content = file
    }

    static boolean identify(contents) {
        try {
            ZipFile zipFile = new ZipFile(contents, ZipFile.OPEN_READ)

            ZipEntry state = zipFile.getEntry('burp')

            return state.size > 0
        } catch (ZipException z) {
            return false
        }
    }

    List<Finding> parse() {
        List<Finding> result = new ArrayList<>()
        ZipFile zipFile = new ZipFile(content, ZipFile.OPEN_READ)
        ZipEntry state = zipFile.getEntry('burp')
        InputStream is = zipFile.getInputStream(state)

        final byte[] buf
        BurpByteBuffer tmp = new BurpByteBuffer()
        while (true) {
            buf = new byte[8196]
            int i = is.read(buf)
            boolean search = true
            tmp.append(buf)
            while (search) {
                if (tmp.contains('</issue>')) {
                    int end = tmp.indexOf('</issue>') + '</issue>'.length()
                    int start = tmp.indexOf('<issue>')
                    result << process(tmp.newBuffer(start, end))

                    tmp.delete(0, end)
                } else search = false
            }
            if (i < 0)
                break

        }
        is.close()
        return result
    }

    private Finding process(BurpByteBuffer input) {
        int type = getInt(getBytes(input, '<type>', '</type>'), 1)
        int sc = getInt(getBytes(input, '<sc>', '</sc>'), 1)
        int ss = getInt(getBytes(input, '<ss>', '</ss>'), 1)
        String host = new String(getBytes(input, '<httpService><host>', '</host>')).trim()
        int port = getInt(getBytes(input, '</host><port>', '</port>'), 1)
        boolean https = getBoolean(getBytes(input, '<https>', '</https>'), 1)
        String path = new String(getBytes(input, '<path>', '</path>')).trim()
        Map<Long, String> kv = new HashMap<>()
        while (input.contains('<longkey>')) {
            long key = getLong(getBytes(input, '<longkey>', '</longkey>'), 1)
            input.delete(0, input.indexOf('</longkey>') + 10)
            int valpos = input.indexOf('<value>')
            if (valpos == 0) {
                byte[] v = getBytes(input, '<value>', '</value>')
                for (int i = 0; i < 5; i++) v[i] = 0
                String val = new String(v).trim()
                kv.put(key, val)
                input.delete(0, input.indexOf('</value>') + 8)
            } else {
                kv.put(key, null)
            }
        }

        def plugin = type + ':' + (kv.get(15l) ?: 'UNKNOWN')
        return new Finding([scanner: scanner, ip: host, port: port as String, portStatus: 'open', protocol: 'tcp', service: (https) ? 'HTTPS' : 'HTTP',
                            plugin : plugin, severity: calc(ss),
                            summary: buildSummary(path, ss, sc, kv)])
    }

    private String buildSummary(String path, int sev, int conf, Map<Long, String> keyValues) {
        return """
Note        : Not all information can be extracted from the state file. For full info, please use a burp report.
Name        : ${keyValues.get(15l)}
Detail      : ${keyValues.get(38l)}
Path        : ${path}
Location    : ${path}
Severity    : ${calc(sev)} (${cc(conf)})
Background  : ${keyValues.get(16l)}
Remediation : ${keyValues.get(17l)}
"""
    }

    private String cc(int confidence) {
        switch (confidence) {
            case 3: return 'Certain'
            case 2: return 'Firm'
            case 1: return 'Tentative'
            default: return ''
        }
    }

    private Finding.Severity calc(int severity) {
        switch (severity) {
            case 4: return Finding.Severity.HIGH
            case 3: return Finding.Severity.MEDIUM
            case 2: return Finding.Severity.LOW
            case 1: return Finding.Severity.INFO
            default: return Finding.Severity.UNKNOWN
        }
    }


    private static byte[] getBytes(BurpByteBuffer input, String startTag, String stopTag) {

        int start = input.indexOf(startTag)
        int stop = input.indexOf(stopTag)
        return input.range(start + startTag.size(), stop)
    }

    private static long getLong(byte[] b, int off) {
        return ((b[off + 7] & 0xFFL)) +
                ((b[off + 6] & 0xFFL) << 8) +
                ((b[off + 5] & 0xFFL) << 16) +
                ((b[off + 4] & 0xFFL) << 24) +
                ((b[off + 3] & 0xFFL) << 32) +
                ((b[off + 2] & 0xFFL) << 40) +
                ((b[off + 1] & 0xFFL) << 48) +
                (((long) b[off]) << 56);
    }

    private static int getInt(byte[] b, int off) {
        return ((b[off + 3] & 0xFF)) +
                ((b[off + 2] & 0xFF) << 8) +
                ((b[off + 1] & 0xFF) << 16) +
                ((b[off]) << 24);
    }

    private static boolean getBoolean(byte[] b, int off) {
        return b[off] != 0;
    }
}

class BurpByteBuffer {
    byte[] buffer = new byte[0]

    void append(byte[] bytes) {
        int position = buffer.size()
        increaseCapacity(bytes.size())
        for (int i = 0; i < bytes.size(); i++) {
            buffer[i + position] = bytes[i]
        }
    }

    boolean contains(String value) {
        return indexOf(value) != -1
    }

    int indexOf(String value) {
        return indexOf(buffer, value.getBytes())
    }

    BurpByteBuffer newBuffer(int start, int stop) {
        BurpByteBuffer result = new BurpByteBuffer()
        result.append(range(start, stop))
        return result
    }

    byte[] range(int start, int stop) {
        return Arrays.copyOfRange(buffer, start, stop)
    }

    void delete(int start, int stop) {
        int last = buffer.size()
        buffer = Arrays.copyOfRange(buffer, stop, last)
    }

    private void increaseCapacity(int size) {
        int capacity = buffer.size()
        buffer = Arrays.copyOf(buffer, capacity + size)
    }

    String toString() {
        return new String(buffer)
    }

    int indexOf(byte[] outerArray, byte[] smallerArray) {
        for (int i = 0; i < outerArray.length - smallerArray.length + 1; ++i) {
            boolean found = true
            for (int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i + j] != smallerArray[j]) {
                    found = false
                    break
                }
            }
            if (found) return i
        }
        return -1
    }
}
