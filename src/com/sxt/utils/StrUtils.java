package com.sxt.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 生成字符串的工具类
 *
 * @author Administrator
 */
public class StrUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    private static Random ran = new Random();

    /**
     * 生成图片的名字
     *
     * @param oldName XXXXX....AAAAA.PNG
     */
    public static String createStrUseTime(String oldName) {
        String newName = "";
        //得到文件的后缀名
        String end = oldName.substring(oldName.lastIndexOf("."));
        //得到时间随机数据
        String start1 = sdf.format(new Date());
        //得到四位随机数
        int start2 = ran.nextInt(9000) + 1000;
        newName = start1 + start2 + end;
        return newName;
    }


    /**
     * 使用UUID生成字符串
     * UUID原则上在同一个电脑上面产生的32位的字符串不是不会重复的
     *
     * @param args
     */
    public static String createStrUseUUID(String oldName) {
        String newName = "";
        //得到文件的后缀名
        String end = oldName.substring(oldName.lastIndexOf("."));
        //生成UUID
        String start = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        newName = start + end;
        return newName;
    }

    /**
     * 得到不前日期   YYYY-MM-DD
     */
    public static String createDirName() {
        return sdf2.format(new Date());
    }

    public static void main(String[] args) {
        System.out.println(createDirName());
    }
}
