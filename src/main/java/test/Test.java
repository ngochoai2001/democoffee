package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Test {
    public static void main(String[] args) throws IOException {
        URL u = new URL("https://lh3.googleusercontent.com/a/AGNmyxaikTlbYUJMeBqSiflYX0YWBTv5mQR7JFSvtXrvyw=s96-c");
        int contentLength = u.openConnection().getContentLength();
        InputStream openStream = u.openStream();
        byte[] binaryData = new byte[contentLength];
        openStream.read(binaryData);
        openStream.close();
    }
}
