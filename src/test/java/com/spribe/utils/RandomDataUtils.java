package com.spribe.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomDataUtils {

    public static String generateUsername() {
        return "user%s".formatted(RandomStringUtils.randomNumeric(4));
    }

    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
