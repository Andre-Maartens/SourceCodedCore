package sourcecoded.core.core.launch.hooks.deps;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.UserDefinedFileAttributeView;

public class Dependency {

    public String name;
    public String downloadURL;
    public int revision;



    /* ATTACH TO FILE LOGIC */

    public void attach(File file) throws IOException {
        UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
        view.write("sourcecodedcore.dep.name", Charset.defaultCharset().encode(name));
        view.write("sourcecodedcore.dep.revision", Charset.defaultCharset().encode(String.valueOf(revision)));
    }

    public static Dependency detach(File file) throws IOException {
        UserDefinedFileAttributeView view = Files.getFileAttributeView(file.toPath(), UserDefinedFileAttributeView.class);
        Dependency dep = new Dependency();
        dep.name = readKey(view, "sourcecodedcore.dep.name");
        dep.revision = Integer.parseInt(readKey(view, "sourcecodedcore.dep.revision"));

        return dep;
    }

    static String readKey(UserDefinedFileAttributeView view, String key) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(view.size(key));
        view.read(key, buf);
        buf.flip();
        return Charset.defaultCharset().decode(buf).toString();
    }

}
