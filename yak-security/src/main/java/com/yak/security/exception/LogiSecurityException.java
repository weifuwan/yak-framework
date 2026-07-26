/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.exception;

public class LogiSecurityException
extends RuntimeException {
    public LogiSecurityException() {
    }

    public LogiSecurityException(CodeMsg codeMsg) {
        super(codeMsg.getCode() + "-" + codeMsg.getMessage());
    }

    public LogiSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}

