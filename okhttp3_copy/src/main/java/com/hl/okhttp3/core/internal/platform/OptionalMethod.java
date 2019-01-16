package com.hl.okhttp3.core.internal.platform;

public class OptionalMethod<T> {

    private final Class<?> returnType;

    private final String methodName;

    private final Class[] methodParams;


    OptionalMethod(Class<?> returnType, String methodName, Class... methodParams) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.methodParams = methodParams;
    }
}
