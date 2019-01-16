package com.hl.okhttp3.core;


import android.support.annotation.Nullable;

import com.hl.okhttp3.core.internal.Util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

import static java.nio.charset.StandardCharsets.UTF_8;

public abstract class ResponseBody implements Closeable {
    private @Nullable
    Reader reader;

    public abstract @Nullable MediaType contentType();

    public abstract long contentLength();

    public final InputStream byteStream() {
        return source().inputStream();
    }

    public abstract BufferedSource source();

    /**
     * Returns the response as a byte array.
     *
     * <p>This method loads entire response body into memory. If the response body is very large this
     * may trigger an {@link OutOfMemoryError}. Prefer to stream the response body if this is a
     * possibility for your response.
     */
    public final byte[] bytes() throws IOException {
        long contentLength = contentLength();
        if (contentLength > Integer.MAX_VALUE) {
            throw new IOException("Cannot buffer entire body for content length: " + contentLength);
        }

        byte[] bytes;
        try (BufferedSource source = source()) {
            bytes = source.readByteArray();
        }
        if (contentLength != -1 && contentLength != bytes.length) {
            throw new IOException("Content-Length ("
                    + contentLength
                    + ") and stream length ("
                    + bytes.length
                    + ") disagree");
        }
        return bytes;
    }

    /**
     * Returns the response as a character stream.
     *
     * <p>If the response starts with a <a href="https://en.wikipedia.org/wiki/Byte_order_mark">Byte
     * Order Mark (BOM)</a>, it is consumed and used to determine the charset of the response bytes.
     *
     * <p>Otherwise if the response has a Content-Type header that specifies a charset, that is used
     * to determine the charset of the response bytes.
     *
     * <p>Otherwise the response bytes are decoded as UTF-8.
     */
    public final Reader charStream() {
        Reader r = reader;
        return r != null ? r : (reader = new BomAwareReader(source(), charset()));
    }

    public final String string() throws IOException {
        try (BufferedSource source = source()) {
            Charset charset = Util.bomAwareCharset(source, charset());
            return source.readString(charset);
        }
    }

    private Charset charset() {
        MediaType contentType = contentType();
        return contentType != null ? contentType.charset(UTF_8) : UTF_8;
    }

    @Override public void close() {
        Util.closeQuietly(source());
    }

    /**
     * Returns a new response body that transmits {@code content}. If {@code contentType} is non-null
     * and lacks a charset, this will use UTF-8.
     */
    public static ResponseBody create(@Nullable MediaType contentType, String content) {
        Charset charset = UTF_8;
        if (contentType != null) {
            charset = contentType.charset();
            if (charset == null) {
                charset = UTF_8;
                contentType = MediaType.parse(contentType + "; charset=utf-8");
            }
        }
        Buffer buffer = new Buffer().writeString(content, charset);
        return create(contentType, buffer.size(), buffer);
    }

    /** Returns a new response body that transmits {@code content}. */
    public static ResponseBody create(final @Nullable MediaType contentType, byte[] content) {
        Buffer buffer = new Buffer().write(content);
        return create(contentType, content.length, buffer);
    }

    /** Returns a new response body that transmits {@code content}. */
    public static ResponseBody create(@Nullable MediaType contentType, ByteString content) {
        Buffer buffer = new Buffer().write(content);
        return create(contentType, content.size(), buffer);
    }

    /** Returns a new response body that transmits {@code content}. */
    public static ResponseBody create(final @Nullable MediaType contentType,
                                      final long contentLength, final BufferedSource content) {
        if (content == null) throw new NullPointerException("source == null");
        return new ResponseBody() {
            @Override public @Nullable MediaType contentType() {
                return contentType;
            }

            @Override public long contentLength() {
                return contentLength;
            }

            @Override public BufferedSource source() {
                return content;
            }
        };
    }

    static final class BomAwareReader extends Reader {
        private final BufferedSource source;
        private final Charset charset;

        private boolean closed;
        private @Nullable Reader delegate;

        BomAwareReader(BufferedSource source, Charset charset) {
            this.source = source;
            this.charset = charset;
        }

        @Override public int read(char[] cbuf, int off, int len) throws IOException {
            if (closed) throw new IOException("Stream closed");

            Reader delegate = this.delegate;
            if (delegate == null) {
                Charset charset = Util.bomAwareCharset(source, this.charset);
                delegate = this.delegate = new InputStreamReader(source.inputStream(), charset);
            }
            return delegate.read(cbuf, off, len);
        }

        @Override public void close() throws IOException {
            closed = true;
            if (delegate != null) {
                delegate.close();
            } else {
                source.close();
            }
        }
    }
}
