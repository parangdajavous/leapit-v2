package com.example.leapit._core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Util {

    // image/jpeg 중에 jpeg만 return
    public static String getMimeType(String imgBase64) {
        int beginIndex = imgBase64.indexOf("/") + 1;
        int endIndex = imgBase64.indexOf(";");
        String mimeType = imgBase64.substring(beginIndex, endIndex);
        return mimeType;
    }

    public static String encodeAsString(byte[] imgBytes, String mimeType) {
        String imgBase64 = Base64.getEncoder().encodeToString(imgBytes);
        imgBase64 = "data:$mimeType;base64,$imgBase64".replace("$mimeType", mimeType).replace("$imgBase64", imgBase64);
        return imgBase64;
    }

    public static byte[] decodeAsBytes(String imgBase64) {
        // 1. mimetype parsing
        String mimeType = getMimeType(imgBase64);
        //System.out.println(mimeType);

        // 2. img parsing
        int prefixEndIndex = imgBase64.indexOf(",");
        String img = imgBase64.substring(prefixEndIndex + 1);
        //System.out.println(img);

        // 3. base64 decode to byte[]
        byte[] imgBytes = Base64.getDecoder().decode(img);
        return imgBytes;
    }

    public static byte[] readImageAsByteArray(String filenameInUpload) {
        try {
            // 현재 프로젝트 루트 경로 + /upload/ + 파일명
            String uploadDir = System.getProperty("user.dir") + "/upload/";
            Path filePath = Paths.get(uploadDir + filenameInUpload);

            // 파일이 실제로 존재하는지 확인
            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("upload 폴더에서 파일을 찾을 수 없습니다: " + filePath);
            }

            // 파일을 byte[] 로 읽어서 반환
            return Files.readAllBytes(filePath);

        } catch (IOException e) {
            throw new RuntimeException("upload 폴더에서 이미지 읽기 실패: " + filenameInUpload, e);
        }
    }
}
