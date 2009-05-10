package com.browzmi.tool.kit;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/*
* Project: Amigo4 (beta)
* Author: ivashchenko
* Created: 11.04.2007 13:57:32
* 
* Copyright (c) 1999-2007 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
public final class Digest {
    private final MessageDigest md5; //128 bit - 16 bytes - 2 longs

    Digest() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getHashCode(String string) {
        try {
            //"373" results in 15-byte-output after conversion to BigInteger
            final MessageDigest messageDigest = (MessageDigest) md5.clone();
            return getHashcode(messageDigest, string);
        } catch (CloneNotSupportedException e) {
            synchronized(md5) {
                return getHashcode(md5, string);
            }
        }
    }

    private byte[] getHashcode(MessageDigest md5, String string) {
        md5.update(string.getBytes());
        return md5.digest();
    }
}
