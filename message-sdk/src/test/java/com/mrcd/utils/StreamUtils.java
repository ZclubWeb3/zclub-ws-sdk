package com.mrcd.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamUtils {

    private StreamUtils() {
    }

    public static String fileToString(String filePath) {
        FileInputStream fileInputStream = null;
        String content = "";
        try {
            File file = new File(filePath);
            fileInputStream = new FileInputStream(file);
            content = StreamUtils.streamToString(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtils.closeStream(fileInputStream);
        }
        return content;
    }

    /**
     * stream to string
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String streamToString(InputStream inputStream)
            throws IOException {
        if (inputStream == null) {
            return "";
        }
        StringBuilder sBuilder = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        while ((line = bufferedReader.readLine()) != null) {
            sBuilder.append(line).append("\n");
        }
        return sBuilder.toString();
    }

    /**
     * 关闭Closeable对象
     *
     * @param closeable
     */
    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
