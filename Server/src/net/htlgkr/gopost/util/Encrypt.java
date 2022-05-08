package net.htlgkr.gopost.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Encrypt {
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();

        for (byte datum : data) {
            int halfByte = (datum >>> 4) & 0x0F;
            int twoHalves = 0;
            do {
                if ((0 <= halfByte) && (halfByte <= 9)) buf.append((char) ('0' + halfByte));
                else buf.append((char) ('a' + (halfByte - 10)));
                halfByte = datum & 0x0F;
            } while (twoHalves++ < 1);
        }
        return buf.toString();
    }

    public static String SHA512(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] sha1hash;
            md.update(text.getBytes(StandardCharsets.UTF_8), 0, text.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}