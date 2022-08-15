package com.mrcd.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用于在test 中读取与assets中的文件, assets 与 java 文件在同一目录. 例如
 */
public class AssetsUtil {

    private AssetsUtil() {
    }

    /**
     * @param fileName 文件名, 根目录为 test/asserts/
     * @return
     */
    public static JSONObject readJsonObject(String fileName) {
        try {
            return new JSONObject(readJsonString(fileName));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param fileName 文件名, 根目录为 test/asserts/
     * @return
     */
    public static JSONArray readJsonArray(String fileName) {
        try {
            return new JSONArray(readJsonString(fileName));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String readJsonString(String fileName) {
        // 构造为 test/assets/fileName 路径
        File fullPath = new File(getCurrentAssetDir(), fileName);
        return readFileContent(fullPath.getAbsolutePath());
    }

    /**
     * 获取当前测试项目的 asset 路径
     *
     * @return
     */
    public static File getCurrentAssetDir() {
        // 获取当前的路径, 也就是 test项目所在的位置
        final String dir = System.getProperty("user.dir");
        return new File(dir, File.separator + "src/test/assets/");
    }

    /**
     * 打开当前测试项目assets 路径的文件, 返回 InputStream
     *
     * @param fileName 文件名
     * @return
     */
    public static InputStream openAssetsStream(String fileName) {
        // 构造为 test/assets/fileName 路径
        File fullPath = new File(getCurrentAssetDir(), fileName);
        try {
            return new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取当前测试项目assets路径的文件
     *
     * @param fileName 文件名
     * @return
     */
    public static String readAssetsFile(String fileName) {
        // 构造为 test/assets/fileName 路径
        File fullPath = new File(getCurrentAssetDir(), fileName);
        try {
            return StreamUtils.streamToString(new FileInputStream(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readFileContent(String filePath) {
        FileInputStream fileInputStream = null;
        String content = "";
        try {
            File file = new File(filePath);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                content = StreamUtils.streamToString(fileInputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtils.closeStream(fileInputStream);
        }
        return content;
    }
}
