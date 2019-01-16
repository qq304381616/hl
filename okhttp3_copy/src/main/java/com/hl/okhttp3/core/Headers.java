/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hl.okhttp3.core;

import android.support.annotation.Nullable;

import com.hl.okhttp3.core.internal.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Headers {
    private final String[] namesAndValues;
    Headers(Builder builder) {
        this.namesAndValues = builder.namesAndValues.toArray(new String[builder.namesAndValues.size()]);
    }
    private Headers(String[] namesAndValues) {
        this.namesAndValues = namesAndValues;
    }
    public static Headers of(String... namesAndValues) {
        if (namesAndValues == null) throw new NullPointerException("namesAndValues == null");
        if (namesAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("Expected alternating header names and values");
        }

        // Make a defensive copy and clean it up.
        namesAndValues = namesAndValues.clone();
        for (int i = 0; i < namesAndValues.length; i++) {
            if (namesAndValues[i] == null) throw new IllegalArgumentException("Headers cannot be null");
            namesAndValues[i] = namesAndValues[i].trim();
        }

        // Check for malformed headers.
        for (int i = 0; i < namesAndValues.length; i += 2) {
            String name = namesAndValues[i];
            String value = namesAndValues[i + 1];
            checkName(name);
            checkValue(value, name);
        }

        return new Headers(namesAndValues);
    }


    public @Nullable String get(String name) {
        return get(namesAndValues, name);
    }
    private static @Nullable String get(String[] namesAndValues, String name) {
        for (int i = namesAndValues.length - 2; i >= 0; i -= 2) {
            if (name.equalsIgnoreCase(namesAndValues[i])) {
                return namesAndValues[i + 1];
            }
        }
        return null;
    }

    public List<String> values(String name) {
        List<String> result = null;
        for (int i = 0, size = size(); i < size; i++) {
            if (name.equalsIgnoreCase(name(i))) {
                if (result == null) result = new ArrayList<>(2);
                result.add(value(i));
            }
        }
        return result != null
                ? Collections.unmodifiableList(result)
                : new ArrayList<String>();
    }
    public String value(int index) {
        return namesAndValues[index * 2 + 1];
    }

    public String name(int index) {
        return namesAndValues[index * 2];
    }
    public int size() {
        return namesAndValues.length / 2;
    }

    public Builder newBuilder() {
        Builder result = new Builder();
        Collections.addAll(result.namesAndValues, namesAndValues);
        return result;
    }


    public static final class Builder {
        final List<String> namesAndValues = new ArrayList<>(20);
        public Headers build() {
            return new Headers(this);
        }
        public @Nullable String get(String name) {
            for (int i = namesAndValues.size() - 2; i >= 0; i -= 2) {
                if (name.equalsIgnoreCase(namesAndValues.get(i))) {
                    return namesAndValues.get(i + 1);
                }
            }
            return null;
        }

        public Builder add(String name, String value) {
            checkName(name);
            checkValue(value, name);
            return addLenient(name, value);
        } Builder addLenient(String name, String value) {
            namesAndValues.add(name);
            namesAndValues.add(value.trim());
            return this;
        }

        Builder addLenient(String line) {
            int index = line.indexOf(":", 1);
            if (index != -1) {
                return addLenient(line.substring(0, index), line.substring(index + 1));
            } else if (line.startsWith(":")) {
                // Work around empty header names and header names that start with a
                // colon (created by old broken SPDY versions of the response cache).
                return addLenient("", line.substring(1)); // Empty header name.
            } else {
                return addLenient("", line); // No header name.
            }
        }

        public Builder removeAll(String name) {
            for (int i = 0; i < namesAndValues.size(); i += 2) {
                if (name.equalsIgnoreCase(namesAndValues.get(i))) {
                    namesAndValues.remove(i); // name
                    namesAndValues.remove(i); // value
                    i -= 2;
                }
            }
            return this;
        }

        public Builder set(String name, String value) {
            checkName(name);
            checkValue(value, name);
            removeAll(name);
            addLenient(name, value);
            return this;
        }
    }

    static void checkName(String name) {
        if (name == null) throw new NullPointerException("name == null");
        if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
        for (int i = 0, length = name.length(); i < length; i++) {
            char c = name.charAt(i);
            if (c <= '\u0020' || c >= '\u007f') {
                throw new IllegalArgumentException(Util.format(
                        "Unexpected char %#04x at %d in header name: %s", (int) c, i, name));
            }
        }
    }

    static void checkValue(String value, String name) {
        if (value == null) throw new NullPointerException("value for name " + name + " == null");
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);
            if ((c <= '\u001f' && c != '\t') || c >= '\u007f') {
                throw new IllegalArgumentException(Util.format(
                        "Unexpected char %#04x at %d in %s value: %s", (int) c, i, name, value));
            }
        }
    }

}
