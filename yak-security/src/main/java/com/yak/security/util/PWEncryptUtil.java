/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  org.springframework.util.StringUtils
 */
package com.yak.security.util;

import java.util.Base64;
import org.springframework.util.StringUtils;

public class PWEncryptUtil {
    private static final int TIMES = 3;
    private static final String SALT0 = "{@VjJ4ak{[#@%d@#]}J6Rllh@}";
    private static final String SALT1 = "Mv{#cdRgJ45Lqx}3IubEW87!==";
    private static final String UTF8 = "UTF-8";

    private static String encode(int i, String pw) throws Exception {
        String temp = pw + String.format(SALT0, i);
        String encodePW = Base64.getEncoder().encodeToString(temp.getBytes(UTF8));
        return encodePW;
    }

    public static String encode(String pw) throws Exception {
        if (StringUtils.isEmpty((Object)pw)) {
            return pw;
        }
        if (pw.endsWith(SALT1)) {
            return pw;
        }
        for (int i = 1; i < 4; ++i) {
            pw = PWEncryptUtil.encode(i, pw);
        }
        return pw + SALT1;
    }

    private static String decode(int i, String dpw) throws Exception {
        String temp = new String(Base64.getDecoder().decode(dpw), UTF8);
        int index = temp.indexOf(String.format(SALT0, i));
        return temp.substring(0, index);
    }

    public static String decode(String dpw) throws Exception {
        if (StringUtils.isEmpty((Object)dpw)) {
            return dpw;
        }
        if (!dpw.endsWith(SALT1)) {
            return dpw;
        }
        int indexSalt1 = dpw.indexOf(SALT1);
        dpw = dpw.substring(0, indexSalt1);
        for (int i = 3; i > 0; --i) {
            dpw = PWEncryptUtil.decode(i, dpw);
        }
        return dpw;
    }

    public static void main(String[] args) throws Exception {
        String encode = PWEncryptUtil.encode("fwer2342234vcsf");
        String dpw = PWEncryptUtil.decode(encode);
        System.out.println(encode + ":" + dpw);
        System.out.println(encode.length() + ":" + dpw);
    }
}

