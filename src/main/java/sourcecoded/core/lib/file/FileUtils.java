package sourcecoded.core.lib.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static File lastFileModified(File dir) {
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        if (files != null) {
            long lastMod = Long.MIN_VALUE;
            File choice = null;
            for (File file : files) {
                if (file.lastModified() > lastMod) {
                    choice = file;
                    lastMod = file.lastModified();
                }
            }
            return choice;
        }
        return null;
    }

    public static List<String> readFull(BufferedReader reader) throws IOException {
        String line = null;
        ArrayList<String> lines = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

}
