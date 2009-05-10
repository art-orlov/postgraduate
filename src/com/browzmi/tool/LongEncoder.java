package com.browzmi.tool;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 14.12.2007 12:08:18
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
public final class LongEncoder {
    public static byte LENGTH = 11;

    private static byte BASE = 62;
    private static final int NEGATIVE_OFFSET = 20;
    private static char[] alphabet = new char[BASE];

    static {
        for (byte i = 0; i < BASE; i++) { //-_.* are valid candidates too
            alphabet[i] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(i);
        }
    }

    public static String convert(long v) {
        final char[] result = new char[LENGTH];

        final int offset = v < 0 ? NEGATIVE_OFFSET : 0;
        v &= Long.MAX_VALUE;
        for (int i = LENGTH - 1; i > 0; i--) {
            result[i] = convert((byte)(v % BASE));
            v /= BASE;
        }
        result[0] = convert((byte)(v % BASE + offset));

        return new String(result);
    }

    public static long convert(String v) {
        final char[] data = v.toCharArray();
        long result = convert(data[0]);
        final boolean negative = result >= NEGATIVE_OFFSET;
        if (negative) {
            result -= NEGATIVE_OFFSET;
        }
        for (int i = 1; i < LENGTH; i++) {
            result *= BASE;
            result += convert(data[i]);
        }
        return negative ? result | Long.MIN_VALUE : result;
    }

    private static char convert(byte v) {
        return alphabet[v];
    }

    private static byte convert(char v) {
        for (byte i = 0; i < BASE; i++) {
            if (alphabet[i] == v) return i;
        }
        throw new IllegalArgumentException();
    }
}
