package com.scooter.common.util;

import java.util.Random;
import java.util.UUID;

/**
 * String utils.
 *
 * @version 1.0
 */
public class StringUtils {

    /**
     * Test object is empty.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    /**
     * Test object is not empty.
     *
     * @param str the str
     * @return the boolean
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /**
     * generate 36 bits UUID
     *
     * @return
     */
    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    public static String createOrderId() {
        return System.currentTimeMillis() + getRandom(8);

    }

    /**
     * generate a 6 bits random code
     *
     * @return
     */
    public static String genVerifyCode() {
        return getRandom(6);
    }

    /**
     * 获取随机数
     *
     * @param length 位数
     * @return
     */
    public static String getRandom(int length) {
        StringBuilder str = new StringBuilder();//定义变长字符串
        Random random = new Random();
        //随机生成数字，并添加到字符串
        for (int i = 0; i < length; i++) {
            str.append(random.nextInt(10));
        }

        return str.toString();
    }
}
