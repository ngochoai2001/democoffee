package com.example.demo.utils;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtils {
    public static byte [] downloadImgFromGGLink(String link) throws IOException {
        URL u = new URL(link);
        int contentLength = u.openConnection().getContentLength();
        InputStream openStream = u.openStream();
        byte[] binaryData = new byte[contentLength];
        openStream.read(binaryData);
        openStream.close();
        return binaryData;
    }
}
