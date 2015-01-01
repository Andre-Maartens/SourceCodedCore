package sourcecoded.core.core.proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import sourcecoded.core.core.crash.CrashHandler;
import sourcecoded.core.lib.proxy.IProxy;

public class CommonProxy implements IProxy {

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        FMLCommonHandler.instance().registerCrashCallable(CrashHandler.INSTANCE);
    }

    @Override
    public void postInit() {
    }

    @Override
    public void c_renderer() {
    }

    @Override
    public void c_keybinds() {
    }
}
