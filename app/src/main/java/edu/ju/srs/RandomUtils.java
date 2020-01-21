package edu.ju.srs;
import java.security.SecureRandom;
import java.util.Random;
public class RandomUtils {
    static final String SOURCE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final String SECURE = "!@#$%^&*()<>?_+-=0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom secureRnd = new SecureRandom();
    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(SOURCE.charAt(secureRnd.nextInt(SOURCE.length())));
        return sb.toString();
    }
    public static String SrandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(SECURE.charAt(secureRnd.nextInt(SECURE.length())));
        return sb.toString();
    }
}
