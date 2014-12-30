package sourcecoded.core.core.launch;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import sourcecoded.core.core.SourceCodedCore;
import sourcecoded.core.lib.SCCSharedData;

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
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}