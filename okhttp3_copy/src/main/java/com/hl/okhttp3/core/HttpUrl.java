package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import com.hl.okhttp3.core.internal.Util;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okio.Buffer;

public final class HttpUrl {
    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    static final String QUERY_ENCODE_SET = " \"'<>#";
    static final String QUERY_COMPONENT_REENCODE_SET = " \"'<>#&=";
    static final String QUERY_COMPONENT_ENCODE_SET = " !\"#$&'(),/:;<=>?@[]\\^`{|}~";
    static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    static final String FRAGMENT_ENCODE_SET = "";
    static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";

    final String scheme;
    private final String username;
    private final String password;
    final String host;
    final int port;
    private final List<String> pathSegments;
    private final @Nullable
    List<String> queryNamesAndValues;
    private final @Nullable
    String fragment;
    private final String url;
    public String host() {
        return host;
    }
    HttpUrl(Builder builder) {
        this.scheme = builder.scheme;
        this.username = percentDecode(builder.encodedUsername, false);
        this.password = percentDecode(builder.encodedPassword, false);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = percentDecode(builder.encodedPathSegments, false);
        this.queryNamesAndValues = builder.encodedQueryNamesAndValues != null
                ? percentDecode(builder.encodedQueryNamesAndValues, true)
                : null;
        this.fragment = builder.encodedFragment != null
                ? percentDecode(builder.encodedFragment, false) : null;
        this.url = builder.toString();
    }

    static String percentDecode(String encoded, boolean plusIsSpace) {
        return percentDecode(encoded, 0, encoded.length(), plusIsSpace);
    }

    static List<String> percentDecode(List<String> list, boolean plusIsSpace) {
        int size = list.size();
        List<String> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String s = list.get(i);
            result.add(s != null ? percentDecode(s, plusIsSpace) : null);
        }
        return Collections.unmodifiableList(result);
    }

    static String percentDecode(String encoded, int pos, int limit, boolean plusIsSpace) {
        for (int i = pos; i < limit; i++) {
            char c = encoded.charAt(i);
            if (c == '%' || (c == '+' && plusIsSpace)) {
                Buffer out = new Buffer();
                out.writeUtf8(encoded, pos, i);
                percentDecode(out, encoded, i, limit, plusIsSpace);
                return out.readUtf8();
            }
        }
        return encoded.substring(pos, limit);
    }

    static void percentDecode(Buffer out, String encoded, int pos, int limit, boolean plusIsSpace) {
        int codePoint = 0;
        for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
            codePoint = encoded.codePointAt(i);
            if (codePoint == '%' && i + 2 < limit) {
                int d1 = Util.decodeHexDigit(encoded.charAt(i + 1));
                int d2 = Util.decodeHexDigit(encoded.charAt(i + 2));
                if (d1 != -1 && d2 != -1) {
                    out.writeByte((d1 << 4) + d2);
                    i += 2;
                    continue;
                }
            } else if (codePoint == '+' && plusIsSpace) {
                out.writeByte(' ');
                continue;
            }
            out.writeUtf8CodePoint(codePoint);
        }
    }

    public static int defaultPort(String scheme) {
        if (scheme.equals("http")) {
            return 80;
        } else if (scheme.equals("https")) {
            return 443;
        } else {
            return -1;
        }
    }

    public static HttpUrl get(String url) {
        return new Builder().parse(null, url).build();
    }

    public URI uri() {
        String uri = newBuilder().reencodeForUri().toString();
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            try {
                String stripped = uri.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", "");
                return URI.create(stripped);
            } catch (Exception e1) {
                throw new RuntimeException(e);
            }
        }
    }

    public Builder newBuilder() {
        Builder result = new Builder();
        result.scheme = scheme;
        result.encodedUsername = encodedUsername();
        result.encodedPassword = encodedPassword();
        result.host = host;
        result.port = port != defaultPort(scheme) ? port : -1;
        result.encodedPathSegments.clear();
        result.encodedPathSegments.addAll(encodedPathSegments());
        result.encodedQuery(encodedQuery());
        result.encodedFragment = encodedFragment();
        return result;
    }

    private String encodedFragment() {
        if (fragment == null) return null;
        int fragmentStart = url.indexOf('#') + 1;
        return url.substring(fragmentStart);
    }

    public @Nullable
    String encodedQuery() {
        if (queryNamesAndValues == null) return null;
        int queryStart = url.indexOf('?') + 1;
        int queryEnd = Util.delimiterOffset(url, queryStart, url.length(), '#');
        return url.substring(queryStart, queryEnd);
    }

    public String encodedUsername() {
        if (username.isEmpty()) return "";
        int usernameStart = scheme.length() + 3;
        int usernameEnd = Util.delimiterOffset(url, usernameStart, url.length(), ":@");
        return url.substring(usernameStart, usernameEnd);
    }

    public String encodedPassword() {
        if (password.isEmpty()) return "";
        int passwordStart = url.indexOf(':', scheme.length() + 3) + 1;
        int passwordEnd = url.indexOf('@');
        return url.substring(passwordStart, passwordEnd);
    }

    public List<String> encodedPathSegments() {
        int pathStart = url.indexOf('/', scheme.length() + 3);
        int pathEnd = Util.delimiterOffset(url, pathStart, url.length(), "?#");
        List<String> result = new ArrayList<>();
        for (int i = pathStart; i < pathEnd; ) {
            i++;
            int segmentEnd = Util.delimiterOffset(url, i, pathEnd, '/');
            result.add(url.substring(i, segmentEnd));
            i = segmentEnd;
        }
        return result;
    }

    static List<String> queryStringToNameAndValues(String encodedQuery) {
        List<String> result = new ArrayList<>();
        for (int pos = 0; pos <= encodedQuery.length(); ) {
            int ampersandOffset = encodedQuery.indexOf('&', pos);
            if (ampersandOffset == -1) ampersandOffset = encodedQuery.length();

            int equalsOffset = encodedQuery.indexOf('=', pos);
            if (equalsOffset == -1 || equalsOffset > ampersandOffset) {
                result.add(encodedQuery.substring(pos, ampersandOffset));
                result.add(null);
            } else {
                result.add(encodedQuery.substring(pos, equalsOffset));
                result.add(encodedQuery.substring(equalsOffset + 1, ampersandOffset));
            }
            pos = ampersandOffset + 1;
        }
        return result;
    }

    static boolean percentEncoded(String encoded, int pos, int limit) {
        return pos + 2 < limit
                && encoded.charAt(pos) == '%'
                && Util.decodeHexDigit(encoded.charAt(pos + 1)) != -1
                && Util.decodeHexDigit(encoded.charAt(pos + 2)) != -1;
    }

    static String canonicalize(String input, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly) {
        return canonicalize(input, 0, input.length(), encodeSet, alreadyEncoded, strict, plusIsSpace, asciiOnly, null);
    }

    private static String canonicalize(String input, int pos, int limit, String encodeSet, boolean alreadyEncoded,
                                       boolean strict, boolean plusIsSpace, boolean asciiOnly, @Nullable Charset charset) {
        int codePoint;
        for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
            codePoint = input.codePointAt(i);
            if (codePoint < 0x20
                    || codePoint == 0x7f
                    || codePoint >= 0x80 && asciiOnly
                    || encodeSet.indexOf(codePoint) != -1
                    || codePoint == '%' && (!alreadyEncoded || strict && !percentEncoded(input, i, limit))
                    || codePoint == '+' && plusIsSpace) {
                Buffer out = new Buffer();
                out.writeUtf8(input, pos, i);
                canonicalize(out, input, i, limit, encodeSet, alreadyEncoded, strict, plusIsSpace, asciiOnly, charset);
                return out.readUtf8();
            }
        }
        return input.substring(pos, limit);
    }

    static void canonicalize(Buffer out, String input, int pos, int limit, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly, @Nullable Charset charset) {
        Buffer encodedCharBuffer = null; // Lazily allocated.
        int codePoint;
        for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
            codePoint = input.codePointAt(i);
            if (alreadyEncoded
                    && (codePoint == '\t' || codePoint == '\n' || codePoint == '\f' || codePoint == '\r')) {
                // Skip this character.
            } else if (codePoint == '+' && plusIsSpace) {
                // Encode '+' as '%2B' since we permit ' ' to be encoded as either '+' or '%20'.
                out.writeUtf8(alreadyEncoded ? "+" : "%2B");
            } else if (codePoint < 0x20
                    || codePoint == 0x7f
                    || codePoint >= 0x80 && asciiOnly
                    || encodeSet.indexOf(codePoint) != -1
                    || codePoint == '%' && (!alreadyEncoded || strict && !percentEncoded(input, i, limit))) {
                // Percent encode this character.
                if (encodedCharBuffer == null) {
                    encodedCharBuffer = new Buffer();
                }

                // TODO
                if (charset == null || charset.equals("")) {
                    encodedCharBuffer.writeUtf8CodePoint(codePoint);
                } else {
                    encodedCharBuffer.writeString(input, i, i + Character.charCount(codePoint), charset);
                }

                while (!encodedCharBuffer.exhausted()) {
                    int b = encodedCharBuffer.readByte() & 0xff;
                    out.writeByte('%');
                    out.writeByte(HEX_DIGITS[(b >> 4) & 0xf]);
                    out.writeByte(HEX_DIGITS[b & 0xf]);
                }
            } else {
                // This character doesn't need encoding. Just copy it over.
                out.writeUtf8CodePoint(codePoint);
            }
        }
    }

    static List<String> queryStringToNamesAndValues(String encodedQuery) {
        List<String> result = new ArrayList<>();
        for (int pos = 0; pos <= encodedQuery.length(); ) {
            int ampersandOffset = encodedQuery.indexOf('&', pos);
            if (ampersandOffset == -1) ampersandOffset = encodedQuery.length();

            int equalsOffset = encodedQuery.indexOf('=', pos);
            if (equalsOffset == -1 || equalsOffset > ampersandOffset) {
                result.add(encodedQuery.substring(pos, ampersandOffset));
                result.add(null); // No value for this name.
            } else {
                result.add(encodedQuery.substring(pos, equalsOffset));
                result.add(encodedQuery.substring(equalsOffset + 1, ampersandOffset));
            }
            pos = ampersandOffset + 1;
        }
        return result;
    }

    public String redact() {
        return newBuilder("/...")
                .username("")
                .password("")
                .build()
                .toString();
    }
    public @Nullable Builder newBuilder(String link) {
        try {
            return new Builder().parse(this, link);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public boolean isHttps() {
        return scheme.equals("https");
    }

    public String encodedPath() {
        int pathStart = url.indexOf('/', scheme.length() + 3); // "://".length() == 3.
        int pathEnd = delimiterOffset(url, pathStart, url.length(), "?#");
        return url.substring(pathStart, pathEnd);
    }
    public static int delimiterOffset(String input, int pos, int limit, String delimiters) {
        for (int i = pos; i < limit; i++) {
            if (delimiters.indexOf(input.charAt(i)) != -1) return i;
        }
        return limit;
    }

    public @Nullable String query() {
        if (queryNamesAndValues == null) return null; // No query.
        StringBuilder result = new StringBuilder();
        namesAndValuesToQueryString(result, queryNamesAndValues);
        return result.toString();
    }

    static void namesAndValuesToQueryString(StringBuilder out, List<String> namesAndValues) {
        for (int i = 0, size = namesAndValues.size(); i < size; i += 2) {
            String name = namesAndValues.get(i);
            String value = namesAndValues.get(i + 1);
            if (i > 0) out.append('&');
            out.append(name);
            if (value != null) {
                out.append('=');
                out.append(value);
            }
        }
    }

    public int port() {
        return port;
    }

    public String scheme() {
        return scheme;
    }

    public @Nullable HttpUrl resolve(String link) {
        Builder builder = newBuilder(link);
        return builder != null ? builder.build() : null;
    }

    public static final class Builder {

        static final String INVALID_HOST = "Invalid URL host";

        @Nullable
        String scheme;
        @Nullable
        String host;
        int port = -1;
        String encodedUsername = "";
        String encodedPassword = "";

        final List<String> encodedPathSegments = new ArrayList<>();
        @Nullable
        List<String> encodedQueryNamesAndValues;
        @Nullable
        String encodedFragment;

        public Builder() {
            encodedPathSegments.add("");
        }

        public Builder scheme(String scheme) {
            if (scheme == null) {
                throw new NullPointerException("scheme == null");
            } else if (scheme.equalsIgnoreCase("http")) {
                this.scheme = "http";
            } else if (scheme.equalsIgnoreCase("https")) {
                this.scheme = "https";
            } else {
                throw new IllegalArgumentException("unexpected scheme: " + scheme);
            }
            return this;
        }

        public Builder host(String host) {
            if (host == null) throw new NullPointerException("host == null");
            String encoded = canonicalizeHost(host, 0, host.length());
            if (encoded == null) throw new IllegalArgumentException("unexpected host: " + host);
            this.host = encoded;
            return this;
        }

        public Builder port(int port) {
            if (port <= 0 || port > 65535)
                throw new IllegalArgumentException("unexpected port: " + port);
            this.port = port;
            return this;
        }

        private static @Nullable
        String canonicalizeHost(String input, int pos, int limit) {
            String percentDecoded = percentDecode(input, pos, limit, false);
            return Util.canonicalizeHost(percentDecoded);
        }

        int effectivePort() {
            return port != -1 ? port : defaultPort(scheme);
        }

        public HttpUrl build() {
            if (scheme == null) throw new IllegalArgumentException("scheme == null");
            if (host == null) throw new IllegalArgumentException("host == null");
            return new HttpUrl(this);
        }

        public Builder encodedQuery(@Nullable String encodedQuery) {
            this.encodedQueryNamesAndValues = encodedQuery != null
                    ? queryStringToNameAndValues(canonicalize(encodedQuery, QUERY_ENCODE_SET, true, false, true, true))
                    : null;
            return this;
        }

        public Builder reencodeForUri() {
            for (int i = 0, size = encodedPathSegments.size(); i < size; i++) {
                String pathSegment = encodedPathSegments.get(i);
                encodedPathSegments.set(i,
                        canonicalize(pathSegment, PATH_SEGMENT_ENCODE_SET_URI, true, true, false, true));
            }
            if (encodedQueryNamesAndValues != null) {
                for (int i = 0, size = encodedQueryNamesAndValues.size(); i < size; i++) {
                    String component = encodedQueryNamesAndValues.get(i);
                    if (component != null) {
                        encodedQueryNamesAndValues.set(i,
                                canonicalize(component, QUERY_COMPONENT_ENCODE_SET_URI, true, true, true, true));
                    }
                }
            }
            if (encodedFragment != null) {
                encodedFragment = canonicalize(
                        encodedFragment, FRAGMENT_ENCODE_SET_URI, true, true, false, false);
            }
            return this;
        }


        Builder parse(@Nullable HttpUrl base, String input) {
            int pos = Util.skipLeadingAsciiWhitespace(input, 0, input.length());
            int limit = Util.skipTrailingAsciiWhitespace(input, pos, input.length());

            // Scheme.
            int schemeDelimiterOffset = schemeDelimiterOffset(input, pos, limit);
            if (schemeDelimiterOffset != -1) {
                if (input.regionMatches(true, pos, "https:", 0, 6)) {
                    this.scheme = "https";
                    pos += "https:".length();
                } else if (input.regionMatches(true, pos, "http:", 0, 5)) {
                    this.scheme = "http";
                    pos += "http:".length();
                } else {
                    throw new IllegalArgumentException("Expected URL scheme 'http' or 'https' but was '"
                            + input.substring(0, schemeDelimiterOffset) + "'");
                }
            } else if (base != null) {
                this.scheme = base.scheme;
            } else {
                throw new IllegalArgumentException(
                        "Expected URL scheme 'http' or 'https' but no colon was found");
            }

            // Authority.
            boolean hasUsername = false;
            boolean hasPassword = false;
            int slashCount = slashCount(input, pos, limit);
            if (slashCount >= 2 || base == null || !base.scheme.equals(this.scheme)) {
                // Read an authority if either:
                //  * The input starts with 2 or more slashes. These follow the scheme if it exists.
                //  * The input scheme exists and is different from the base URL's scheme.
                //
                // The structure of an authority is:
                //   username:password@host:port
                //
                // Username, password and port are optional.
                //   [username[:password]@]host[:port]
                pos += slashCount;
                authority:
                while (true) {
                    int componentDelimiterOffset = Util.delimiterOffset(input, pos, limit, "@/\\?#");
                    int c = componentDelimiterOffset != limit
                            ? input.charAt(componentDelimiterOffset)
                            : -1;
                    switch (c) {
                        case '@':
                            // User info precedes.
                            if (!hasPassword) {
                                int passwordColonOffset = Util.delimiterOffset(
                                        input, pos, componentDelimiterOffset, ':');
                                String canonicalUsername = canonicalize(input, pos, passwordColonOffset,
                                        USERNAME_ENCODE_SET, true, false, false, true, null);
                                this.encodedUsername = hasUsername
                                        ? this.encodedUsername + "%40" + canonicalUsername
                                        : canonicalUsername;
                                if (passwordColonOffset != componentDelimiterOffset) {
                                    hasPassword = true;
                                    this.encodedPassword = canonicalize(input, passwordColonOffset + 1,
                                            componentDelimiterOffset, PASSWORD_ENCODE_SET, true, false, false, true,
                                            null);
                                }
                                hasUsername = true;
                            } else {
                                this.encodedPassword = this.encodedPassword + "%40" + canonicalize(input, pos,
                                        componentDelimiterOffset, PASSWORD_ENCODE_SET, true, false, false, true, null);
                            }
                            pos = componentDelimiterOffset + 1;
                            break;

                        case -1:
                        case '/':
                        case '\\':
                        case '?':
                        case '#':
                            // Host info precedes.
                            int portColonOffset = portColonOffset(input, pos, componentDelimiterOffset);
                            if (portColonOffset + 1 < componentDelimiterOffset) {
                                host = canonicalizeHost(input, pos, portColonOffset);
                                port = parsePort(input, portColonOffset + 1, componentDelimiterOffset);
                                if (port == -1) {
                                    throw new IllegalArgumentException("Invalid URL port: \""
                                            + input.substring(portColonOffset + 1, componentDelimiterOffset) + '"');
                                }
                            } else {
                                host = canonicalizeHost(input, pos, portColonOffset);
                                port = defaultPort(scheme);
                            }
                            if (host == null) {
                                throw new IllegalArgumentException(
                                        INVALID_HOST + ": \"" + input.substring(pos, portColonOffset) + '"');
                            }
                            pos = componentDelimiterOffset;
                            break authority;
                    }
                }
            } else {
                // This is a relative link. Copy over all authority components. Also maybe the path & query.
                this.encodedUsername = base.encodedUsername();
                this.encodedPassword = base.encodedPassword();
                this.host = base.host;
                this.port = base.port;
                this.encodedPathSegments.clear();
                this.encodedPathSegments.addAll(base.encodedPathSegments());
                if (pos == limit || input.charAt(pos) == '#') {
                    encodedQuery(base.encodedQuery());
                }
            }

            // Resolve the relative path.
            int pathDelimiterOffset = Util.delimiterOffset(input, pos, limit, "?#");
            resolvePath(input, pos, pathDelimiterOffset);
            pos = pathDelimiterOffset;

            // Query.
            if (pos < limit && input.charAt(pos) == '?') {
                int queryDelimiterOffset = Util.delimiterOffset(input, pos, limit, '#');
                this.encodedQueryNamesAndValues = queryStringToNamesAndValues(canonicalize(
                        input, pos + 1, queryDelimiterOffset, QUERY_ENCODE_SET, true, false, true, true, null));
                pos = queryDelimiterOffset;
            }

            // Fragment.
            if (pos < limit && input.charAt(pos) == '#') {
                this.encodedFragment = canonicalize(
                        input, pos + 1, limit, FRAGMENT_ENCODE_SET, true, false, false, false, null);
            }

            return this;
        }

        private static int slashCount(String input, int pos, int limit) {
            int slashCount = 0;
            while (pos < limit) {
                char c = input.charAt(pos);
                if (c == '\\' || c == '/') {
                    slashCount++;
                    pos++;
                } else {
                    break;
                }
            }
            return slashCount;
        }

        private static int portColonOffset(String input, int pos, int limit) {
            for (int i = pos; i < limit; i++) {
                switch (input.charAt(i)) {
                    case '[':
                        while (++i < limit) {
                            if (input.charAt(i) == ']') break;
                        }
                        break;
                    case ':':
                        return i;
                }
            }
            return limit; // No colon.
        }

        private void resolvePath(String input, int pos, int limit) {
            // Read a delimiter.
            if (pos == limit) {
                // Empty path: keep the base path as-is.
                return;
            }
            char c = input.charAt(pos);
            if (c == '/' || c == '\\') {
                // Absolute path: reset to the default "/".
                encodedPathSegments.clear();
                encodedPathSegments.add("");
                pos++;
            } else {
                // Relative path: clear everything after the last '/'.
                encodedPathSegments.set(encodedPathSegments.size() - 1, "");
            }

            // Read path segments.
            for (int i = pos; i < limit; ) {
                int pathSegmentDelimiterOffset = Util.delimiterOffset(input, i, limit, "/\\");
                boolean segmentHasTrailingSlash = pathSegmentDelimiterOffset < limit;
                push(input, i, pathSegmentDelimiterOffset, segmentHasTrailingSlash, true);
                i = pathSegmentDelimiterOffset;
                if (segmentHasTrailingSlash) i++;
            }
        }

        private void push(String input, int pos, int limit, boolean addTrailingSlash,
                          boolean alreadyEncoded) {
            String segment = canonicalize(
                    input, pos, limit, PATH_SEGMENT_ENCODE_SET, alreadyEncoded, false, false, true, null);
            if (isDot(segment)) {
                return; // Skip '.' path segments.
            }
            if (isDotDot(segment)) {
                pop();
                return;
            }
            if (encodedPathSegments.get(encodedPathSegments.size() - 1).isEmpty()) {
                encodedPathSegments.set(encodedPathSegments.size() - 1, segment);
            } else {
                encodedPathSegments.add(segment);
            }
            if (addTrailingSlash) {
                encodedPathSegments.add("");
            }
        }

        private void pop() {
            String removed = encodedPathSegments.remove(encodedPathSegments.size() - 1);

            // Make sure the path ends with a '/' by either adding an empty string or clearing a segment.
            if (removed.isEmpty() && !encodedPathSegments.isEmpty()) {
                encodedPathSegments.set(encodedPathSegments.size() - 1, "");
            } else {
                encodedPathSegments.add("");
            }
        }

        private boolean isDot(String input) {
            return input.equals(".") || input.equalsIgnoreCase("%2e");
        }

        private boolean isDotDot(String input) {
            return input.equals("..")
                    || input.equalsIgnoreCase("%2e.")
                    || input.equalsIgnoreCase(".%2e")
                    || input.equalsIgnoreCase("%2e%2e");
        }

        private static int parsePort(String input, int pos, int limit) {
            try {
                // Canonicalize the port string to skip '\n' etc.
                String portString = canonicalize(input, pos, limit, "", false, false, false, true, null);
                int i = Integer.parseInt(portString);
                if (i > 0 && i <= 65535) return i;
                return -1;
            } catch (NumberFormatException e) {
                return -1; // Invalid port.
            }
        }

        private static int schemeDelimiterOffset(String input, int pos, int limit) {
            if (limit - pos < 2) return -1;

            char c0 = input.charAt(pos);
            if ((c0 < 'a' || c0 > 'z') && (c0 < 'A' || c0 > 'Z'))
                return -1; // Not a scheme start char.

            for (int i = pos + 1; i < limit; i++) {
                char c = input.charAt(i);

                if ((c >= 'a' && c <= 'z')
                        || (c >= 'A' && c <= 'Z')
                        || (c >= '0' && c <= '9')
                        || c == '+'
                        || c == '-'
                        || c == '.') {
                    continue; // Scheme character. Keep going.
                } else if (c == ':') {
                    return i; // Scheme prefix!
                } else {
                    return -1; // Non-scheme character before the first ':'.
                }
            }

            return -1; // No ':'; doesn't start with a scheme.
        }

        public Builder username(String username) {
            if (username == null) throw new NullPointerException("username == null");
            this.encodedUsername = canonicalize(username, USERNAME_ENCODE_SET, false, false, false, true);
            return this;
        }

        public Builder password(String password) {
            if (password == null) throw new NullPointerException("password == null");
            this.encodedPassword = canonicalize(password, PASSWORD_ENCODE_SET, false, false, false, true);
            return this;
        }
    }
}
