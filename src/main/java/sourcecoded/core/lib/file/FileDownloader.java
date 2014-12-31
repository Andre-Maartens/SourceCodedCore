package sourcecoded.core.lib.file;

import sourcecoded.core.core.SourceCodedCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader {

    public static File downloadFile(URL url, File outputFolder) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        int response = con.getResponseCode();

        if (response != HttpURLConnection.HTTP_OK) {
            con.disconnect();
            throw new IOException("Invalid HTTP Response: " + response);
        }

        String filename;
        URL contentURL = con.getURL();

        filename = contentURL.toString().substring(contentURL.toString().lastIndexOf("/") + 1);

        File save = new File(outputFolder, filename);
        save.createNewFile();

        FileOutputStream stream = new FileOutputStream(save.getAbsolutePath());
        InputStream inputStream = con.getInputStream();

        int bytesRead = -1;
        byte[] buffer = new byte[4096];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            stream.write(buffer, 0, bytesRead);
        }

        stream.close();
        inputStream.close();
        con.disconnect();
        return save;
    }

}
