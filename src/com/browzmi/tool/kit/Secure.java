package com.browzmi.tool.kit;

import com.browzmi.tool.LongEncoder;
import com.browzmi.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.UUID;

/*
* Project: Amigo4 (beta)
* Author: ArtemOrlov
* Created: 18.01.2007 16:54:36
*
* Copyright (c) 1999-2006 Magenta Corporation Ltd. All Rights Reserved.
* Magenta Technology proprietary and confidential.
* Use is subject to license terms.
*/
public final class Secure {
    public static final int UUID_LENGTH = 22;

    private PasswordGenerator passwordGenerator;

    Secure() {
        passwordGenerator = new PasswordGenerator();
    }

    public String generateKey() { //22 bytes/characters length
        return stringify(UUID.randomUUID());
    }

    public String generatePassword() {
        return passwordGenerator.generate();
    }

//    @Nullable
//    public static BigInteger parseKey(String key) {
//        try {
//            final Pair<Long, Long> longs = parse(key);
//            return convert2Longs2BI(longs.second, longs.first);
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @Nullable
    public static UUID parse(String key) {
        try {
            final Pair<Long, Long> longs = parseToPair(key);
            return new UUID(longs.first, longs.second);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String stringify(UUID uuid) {
        return stringify(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits());
    }

    public static String stringify(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException();
        }

        final long lsb = convertByteArray2Long(bytes, 0);
        final long msb = convertByteArray2Long(bytes, 8);

        return stringify(msb, lsb);
    }

//    private static BigInteger convert2Longs2BI(long lsb, long msb) {
//        final byte[] bytes = new byte[16];
//
//        System.arraycopy(convertLong2ByteArray(lsb), 0, bytes, 0, 8);
//        System.arraycopy(convertLong2ByteArray(msb), 0, bytes, 8, 8);
//
//        return new BigInteger(bytes);
//    }

    private static String stringify(long msb, long lsb) { //msb, lsb => msb-lsb
        return LongEncoder.convert(msb) + LongEncoder.convert(lsb);
    }

    private static Pair<Long, Long> parseToPair(String s) { //msblsb => msb, lsb
        return Pair.of(LongEncoder.convert(s.substring(0, LongEncoder.LENGTH)), LongEncoder.convert(s.substring(LongEncoder.LENGTH)));
    }

//    private static byte[] convertLong2ByteArray(long v) {
//        final byte[] bytes = new byte[8];
//        for (int i = 0; i < 8; i++) {
//            bytes[i] = (byte) (v);
//            v >>= 8;
//        }
//        return bytes;
//    }

    private static long convertByteArray2Long(byte[] bytes, int offset) {
        long result = 0;
        for (int i = offset + 7; i >= offset; i--) {
            result = (result << 8) | (bytes[i] & 0xff);
        }
        return result;
    }

    private static final class PasswordGenerator {
        private static final int ALPHABET_SIZE = 36;

        private final SecureRandom random = new SecureRandom();

        public final String generate() {
            final byte[] bytes = new byte[16];
            random.nextBytes(bytes);

            long msb = 0, lsb = 0;

            for (byte i = 0; i < 8; i++) {
                msb = (msb << 8) | (bytes[i] & 0xff);
            }
            for (byte i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (bytes[i] & 0xff);
            }

            return longToString(msb) + longToString(lsb);
        }

        private static String longToString(long l) {
            return Long.toString(Math.abs(l), ALPHABET_SIZE);
        }
    }
}
