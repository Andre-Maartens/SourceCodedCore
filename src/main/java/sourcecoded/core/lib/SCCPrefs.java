package sourcecoded.core.lib;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.LoadController;

import java.util.HashMap;

public class SCCPrefs {

    public static HashMap<String, Object> modInfo = new HashMap<String, Object>();

    static {
        modInfo.put("modid", "sourcecodedcore");
        modInfo.put("name", "SourceCodedCore");
        modInfo.put("version", "@VERSION@");
        modInfo.put("description", "A common, shared library for all of SourceCoded's (Jaci's) Mods");
    }

    public static String getString(String ID) {
        return (String) modInfo.get(ID);
    }
}
