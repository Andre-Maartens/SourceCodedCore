package sourcecoded.core.core.launch;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import sourcecoded.core.core.launch.hooks.deps.DependencyLoader;
import sourcecoded.core.lib.SCCSharedData;
import sourcecoded.core.lib.launch.ILaunchCallHook;

import java.util.ArrayList;
import java.util.Map;

public class PluginSetup implements IFMLCallHook {

    static ArrayList<ILaunchCallHook> hooks = new ArrayList<ILaunchCallHook>();

    static {
        hooks.add(new DependencyLoader());
    }

    @Override
    public void injectData(Map<String, Object> data) {
        SCCSharedData.blackboard.put("classLoader", data.get("classLoader"));
        SCCSharedData.blackboard.put("devEnvironment", !(Boolean)data.get("runtimeDeobfuscationEnabled"));
    }

    @Override
    public Void call() throws Exception {
        for (ILaunchCallHook hook : hooks) {
            hook.call();
        }
        return null;
    }

}
