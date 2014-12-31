package sourcecoded.core.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import sourcecoded.core.core.proxy.ClientProxy;
import sourcecoded.core.lib.SCCSharedData;
import sourcecoded.core.lib.config.SCConfigManager;
import sourcecoded.core.lib.config.VersionConfig;
import sourcecoded.core.lib.proxy.IProxy;

import java.io.IOException;
import java.util.Arrays;

public class SourceCodedCore extends DummyModContainer {

    /* START MOD SETUP */

    public SourceCodedCore() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = SCCSharedData.getString("id");
        meta.name = SCCSharedData.getString("name");
        meta.version = SCCSharedData.getString("version");
        meta.authorList = Arrays.asList("SourceCodedCore");
        meta.description = SCCSharedData.getString("description");
        meta.screenshots = new String[0];
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    /* END SETUP */

    @SidedProxy(clientSide = "sourcecoded.core.core.proxy.ClientProxy", serverSide = "sourcecoded.core.core.proxy.ServerProxy")
    public static IProxy proxy;

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        SCConfigManager.init(VersionConfig.createNewVersionConfig(event.getSuggestedConfigurationFile(), "0.1", SCCSharedData.getString("modid")));
        proxy.preInit();
    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
        proxy.c_keybinds();
        proxy.c_renderer();
    }

}
