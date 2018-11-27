package com.bluetroy.servlet.utils;

import com.bluetroy.StartUp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ContentTypeUtil {
    private static final Map<String, String> CONTENT_TYPE;

    static {
        CONTENT_TYPE = new HashMap<>();
        scanContentType();
    }

    public static String getContentType(String filePath) {
        String key = filePath.substring(filePath.lastIndexOf("."));
        return CONTENT_TYPE.get(key);
    }

    private static void scanContentType() {
        try {
            String contentType = new String(StartUp.class.getResourceAsStream("/contentType.txt").readAllBytes());
            String[] contentTypeList = contentType.split("[\n]");
            for (String s : contentTypeList) {
                String[] line = s.split("\t");
                CONTENT_TYPE.put(line[0].trim(), line[1].trim());
                CONTENT_TYPE.put(line[2].trim(), line[3].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
