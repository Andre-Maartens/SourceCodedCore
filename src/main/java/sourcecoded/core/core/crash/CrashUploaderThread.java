package sourcecoded.core.core.crash;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import sourcecoded.core.lib.SCCSharedData;
import sourcecoded.core.lib.file.FileUtils;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CrashUploaderThread extends Thread {

    public CrashUploaderThread() {
        this.setName("SourceCodedCore -- Crash Uploader");
    }

    @Override
    public void run() {
        File dirPath = new File(SCCSharedData.getForgeRoot(), "crash-reports");
        File report = FileUtils.lastFileModified(dirPath);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(report));

            List<String> fileLines = FileUtils.readFull(reader);
            List<String> newLines = new ArrayList<String>();

            newLines.add("-----------------------------------------------------------------------------------------------------------");
            newLines.add("You are reading this because your game has crashed");
            newLines.add("SourceCodedCore automatically uploaded this report to GitHub's gists");
            newLines.add("Suspected crash details are as follows: ");
            parseStacktrace(fileLines, newLines);
            newLines.add("-----------------------------------------------------------------------------------------------------------");

            newLines.addAll(fileLines);

            reader.close();

            String response = uploadGist(newLines);
            response = parseResponse(response);
            Desktop.getDesktop().browse(new URI(response));
        } catch (Exception e) {
        }
    }

    void parseStacktrace(List<String> lines, List<String> addTo) {
        ArrayList<ModContainer> culprits = new ArrayList<ModContainer>();
        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i);
            if (currentLine.startsWith("Stacktrace:") && lines.get(i - 1).startsWith("-- Head --")) {
                //Stack trace
                for (int j = i; j < lines.size(); j++) {
                    currentLine = lines.get(j);
                    if (!currentLine.trim().startsWith("at")) continue;

                    for (ModContainer container : Loader.instance().getModList()) {
                        List<String> packages = container.getOwnedPackages();
                        if (packages != null)
                            for (String pkg : packages) {
                                if (currentLine.contains(pkg)) {
                                    //Trace apparently contains this mod
                                    if (!culprits.contains(container))
                                        culprits.add(container);
                                }
                            }
                    }
                }
                break;
            }
        }

        for (ModContainer culprit : culprits) {
            String modid = culprit.getModId();
            StringBuilder authors = new StringBuilder();
            for (String a : culprit.getMetadata().authorList) {
                authors.append(a + ", ");
            }

            addTo.add(String.format("Mod: %s (%s) appears to be included in this crash. Contact the authors (%s) for more information.", modid, culprit.getName(), authors.toString()));
        }
    }

    public static String uploadGist(List<String> lines) throws IOException {
        URL url = new URL("https://api.github.com/gists");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        String data = null;
        data = encodeFile(lines);

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        return stb.toString();
    }

    public static String encodeFile(List<String> lines) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);

        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append("\n");
        }
        String details = builder.toString();

        writer.beginObject();

        writer.name("public");
        writer.value(true);

        writer.name("description");
        writer.value("SourceCodedCore_CrashReport");

        writer.name("files");
        writer.beginObject();
        writer.name("scc_crash_RawLog.txt");
        writer.beginObject();
        writer.name("content");
        writer.value(details);
        writer.endObject();
        writer.endObject();

        writer.endObject();

        writer.close();

        return sw.toString();
    }

    public static String parseResponse(String response) {
        try {
            JsonObject obj = new JsonParser().parse(response).getAsJsonObject();
            return obj.get("html_url").getAsString();
        } catch (Exception e) {
            System.err.println("Could not upload Crash Report to Gist");
        }
        return "";
    }

}
