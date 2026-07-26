/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.security.common.enums.message;

public enum MessageCode {
    ROLE_ADD_MESSAGE("\u89d2\u8272\u6743\u9650\u53d8\u52a8\u901a\u77e5", "\u7ba1\u7406\u5458\u4e8e%s\uff0c\u4e3a\u60a8\u5206\u914d\u4e86\u89d2\u8272{%s}\uff0c\u8bf7\u77e5\u6089\uff1b"),
    ROLE_REMOVE_MESSAGE("\u89d2\u8272\u6743\u9650\u53d8\u52a8\u901a\u77e5", "\u7ba1\u7406\u5458\u4e8e%s\uff0c\u79fb\u9664\u4e86\u89d2\u8272{%s}\uff0c\u8bf7\u77e5\u6089\uff1b");

    private final String title;
    private final String content;

    private MessageCode(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }
}

