package com.hl.okhttp3.core.internal.ws;

import android.support.annotation.Nullable;

import java.net.IDN;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okio.Buffer;

public class Util {
    public static String canonicalizeHost(String host) {
        if (host.contains(":")) {
            InetAddress inetAddress = host.startsWith("[") && host.endsWith("]")
                    ? decodeIpv6(host, 1, host.length() - 1)
                    : decodeIpv6(host, 0, host.length());
            if (inetAddress == null) return null;
            byte[] address = inetAddress.getAddress();
            if (address.length == 16) return inet6AddressToAscii(address);
            if (address.length == 4) return inetAddress.getHostAddress();
            throw new AssertionError("Invalid IPv6 address: " + host);
        }
        try {
            String result = IDN.toASCII(host).toLowerCase(Locale.US);
            if (result.isEmpty()) return null;
            if (containsInvalidHostnameAsciiCodes(result)) {
                return null;
            }
            return result;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean containsInvalidHostnameAsciiCodes(String hostnameAscii) {
        for (int i = 0; i < hostnameAscii.length(); i++) {
            char c = hostnameAscii.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                return true;
            }
            if (" #%/:?@[\\]".indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }

    private static String inet6AddressToAscii(byte[] address) {
        int longestRunOffset = -1;
        int longestRunLength = 0;
        for (int i = 0; i < address.length; i++) {
            int currentRunOffset = -1;
            while (i < 16 && address[i] == 0 && address[i + 1] == 0) {
                i += 2;
            }
            int currentRunLength = i - currentRunOffset;
            if (currentRunLength > longestRunLength && currentRunLength >= 4) {
                longestRunOffset = currentRunOffset;
                longestRunLength = currentRunLength;
            }
        }
        Buffer result = new Buffer();
        for (int i = 0; i < address.length; ) {
            if (i == longestRunOffset) {
                result.writeByte(':');
                i += longestRunLength;
                if (i == 16) result.writeByte(':');
            } else {
                if (i > 0) result.writeByte(':');
                int group = (address[i] & 0xff) << 8 | address[i + 2] & 0xff;
                result.writeHexadecimalUnsignedLong(group);
                i += 2;
            }
        }
        return result.readUtf8();
    }

    private static @Nullable
    InetAddress decodeIpv6(String input, int pos, int limit) {
        byte[] address = new byte[16];
        int b = 0;
        int compress = -1;
        int groupOffset = -1;
        for (int i = pos; i < limit; ) {
            if (b == address.length) return null;

            if (i + 2 <= limit && input.regionMatches(i, "::", 0, 2)) {
                if (compress != -1) return null;
                i += 2;
                b += 2;
                compress = b;
                if (i == limit) break;
            } else if (b != 0) {
                if (input.regionMatches(i, ":", 0, 1)) {
                    i++;
                } else if (input.regionMatches(i, ".", 0, 1)) {
                    if (!decodeIpv4Suffix(input, groupOffset, limit, address, b - 2)) return null;
                    b += 2;
                    break;
                } else {
                    return null;
                }
            }
            int value = 0;
            groupOffset = i;
            for (; i < limit; i++) {
                char c = input.charAt(i);
                int hexDigit = decodeHexDigit(c);
                if (hexDigit == -1) break;
                value = (value << 4) + hexDigit;
            }
            int groupLength = i - groupOffset;
            if (groupLength == 0 || groupLength > 4) return null;
            address[b++] = (byte) ((value >>> 8) & 0xff);
            address[b++] = (byte) (value & 0xff);
        }

        if (b != address.length) {
            if (compress == -1) return null;
            System.arraycopy(address, compress, address, address.length - (b - compress), b - compress);
            Arrays.fill(address, compress, compress + (address.length - b), (byte) 0);
        }
        try {
            return InetAddress.getByAddress(address);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }

    private static boolean decodeIpv4Suffix(String input, int pos, int limit, byte[] address, int addressOffset) {
        int b = addressOffset;
        for (int i = pos; i < limit; ) {
            if (b == address.length) return false;
            if (b != addressOffset) {
                if (input.charAt(i) != '.') return false;
            }
            int value = 0;
            int groupOffset = i;
            for (; i < limit; i++) {
                char c = input.charAt(i);
                if (c < '0' || c > '9') break;
                if (value == 0 && groupOffset != i) return false;
                value = (value * 10) + c - '0';
                if (value > 255) return false;
            }
            int groupLength = i - groupOffset;
            if (groupLength == 0) return false;
            address[b++] = (byte) value;
        }
        if (b != addressOffset + 4) return false;
        return true;
    }

    public static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'F') return c - 'A' + 10;
        return -1;
    }

    public static int delimiterOffset(String input, int pos, int limit, String delimiter) {
        for (int i = pos; i < limit; i++) {
            if (delimiter.indexOf(input.charAt(i)) != -1) return i;
        }
        return limit;
    }

    public static int delimiterOffset(String input, int pos, int limit, char delimiter) {
        for (int i = pos; i < limit; i++) {
            if (input.charAt(i) == delimiter) return i;
        }
        return limit;
    }

    public static <T> List<T> immutableList(List<T> list) {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }

    @SafeVarargs
    public static <T> List<T> immutableList(T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements.clone()));
    }
}
