package com.ivan.test;

/**
 * 项目名称：UsbHostDemo
 * 类描述：根据values/dimens.xml, 自动计算比例并生成不同分辨率的dimens.xml；注意用dp和sp，不要用dip，否则生成可能会出错；xml值不要有空格
 * 创建人：Michael-hj
 * 创建时间：2016/5/10 0010 8:45
 * 修改人：Michael-hj
 * 修改时间：2016/5/10 0010 8:45
 * 修改备注：
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ABDimensTools_coppy {
    /**
     * 源文件
     */
    private static String oldFilePath = "/res/values/";
    /**
     * 新生成文件路径
     */
    private static String filePath720 = "/res/values-1280x720/";
    /**
     * 新生成文件路径
     */
    private static String filePath672 = "/res/values-1280x672/";
    /**
     * 新生成文件路径
     */
    private static String filePath1080 = "/res/values-1920x1080/";


    private static String rootPath = "/app/src/main/";
    private static String fileName = "dimens.xml";


    public static void gen() {
        String relativelyPath = System.getProperty("user.dir");
        File file = new File(relativelyPath + rootPath + oldFilePath + fileName);
        BufferedReader reader = null;
        StringBuilder px_dimens_672 = new StringBuilder();
        StringBuilder px_dimens_720 = new StringBuilder();
        StringBuilder px_dimens_1080 = new StringBuilder();


        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {

                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    int num = Integer.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));

                    px_dimens_672.append(start).append((int) Math.round(num * 1.5)).append(end).append("\n");
                    px_dimens_720.append(start).append((int) Math.round(num * 1.5)).append(end).append("\n");
                    px_dimens_1080.append(tempString).append("\n");
                } else {
                    px_dimens_672.append(tempString).append("\n");
                    px_dimens_720.append(tempString).append("\n");
                    px_dimens_1080.append(tempString).append("\n");
                }
                line++;
            }
            reader.close();
            System.out.println("<!--  px_dimens_672 -->");
            System.out.println(px_dimens_672);
            System.out.println("<!--  px_dimens_720 -->");
            System.out.println(px_dimens_720);
            System.out.println("<!--  px_dimens_1080 -->");
            System.out.println(px_dimens_1080);

            String px_dimens_672_file = relativelyPath + rootPath + filePath720;
            String px_dimens_720_file = relativelyPath + rootPath + filePath672;
            String px_dimens_1080_file = relativelyPath + rootPath + filePath1080;

            writeFile(px_dimens_672_file, px_dimens_672.toString());
            writeFile(px_dimens_720_file, px_dimens_720.toString());
            writeFile(px_dimens_1080_file, px_dimens_1080.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String filePath, String text) {
        PrintWriter out = null;
        File file = getFile(filePath, "dimens.xml");
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.close();
    }

    private static File getFile(String filePath, String fileName) {
        //path表示你所创建文件的路径
        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }
        // fileName表示你创建的文件名；为txt类型；
        File file = new File(f, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void main(String[] args) {
        gen();
    }
}
