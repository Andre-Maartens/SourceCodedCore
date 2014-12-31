package sourcecoded.core.lib;

import net.minecraftforge.fml.relauncher.FMLInjectionData;

import java.io.File;
import java.util.HashMap;

public class SCCSharedData {

    public static HashMap<String, Object> blackboard = new HashMap<String, Object>();

    static {
        blackboard.put("id", "sourcecodedcore");
        blackboard.put("name", "SourceCodedCore");
        blackboard.put("version", "@VERSION@");
        blackboard.put("description", "A common, shared library for all of SourceCoded's (Jaci's) Mods");

        blackboard.put("forgeRoot", new File(((File) (FMLInjectionData.data()[6])).getAbsolutePath().replace(File.separatorChar, '/')));

        blackboard.put("sccRoot", new File(getForgeRoot(), "mods/sourcecodedcore/"));
    }

    public static String getString(String ID) {
        return (String) blackboard.get(ID);
    }

    public static File getForgeRoot() {
        return (File) blackboard.get("forgeRoot");
    }

    public static File getSCCRoot() {
        return (File) blackboard.get("sccRoot");
    }
}
