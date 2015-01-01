package sourcecoded.core.core.launch;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import sourcecoded.core.core.SourceCodedCore;
import sourcecoded.core.lib.SCCSharedData;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

@IFMLLoadingPlugin.Name("SourceCodedCore")
@IFMLLoadingPlugin.MCVersion("1.8")
public class SCCCorePlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return SourceCodedCore.class.getName();
    }

    @Override
    public String getSetupClass() {
        return PluginSetup.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (data.get("coremodLocation") != null)
            SCCSharedData.blackboard.put("location", (File) data.get("coremodLocation"));
        else
            try {
                SCCSharedData.blackboard.put("location", new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}