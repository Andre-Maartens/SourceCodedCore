package sourcecoded.core.core;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.fml.client.FMLFileResourcePack;
import net.minecraftforge.fml.client.FMLFolderResourcePack;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import sourcecoded.core.core.proxy.ClientProxy;
import sourcecoded.core.core.registrar.SCCBlocks;
import sourcecoded.core.lib.SCCSharedData;
import sourcecoded.core.lib.config.SCConfigManager;
import sourcecoded.core.lib.config.VersionConfig;
import sourcecoded.core.lib.proxy.IProxy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class SourceCodedCore extends DummyModContainer {

    ILanguageAdapter adapter;

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

    @Override
    public File getSource() {
        return (File) SCCSharedData.blackboard.get("location");
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        if (getSource().isDirectory())
            return FMLFolderResourcePack.class;
        else
            return FMLFileResourcePack.class;
    }

    /* END SETUP */

    @SidedProxy(clientSide = "sourcecoded.core.core.proxy.ClientProxy", serverSide = "sourcecoded.core.core.proxy.ServerProxy")
    public static IProxy proxy;

    @Subscribe
    public void construct(FMLConstructionEvent event) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Side side = FMLCommonHandler.instance().getSide();
        ClassLoader mcl = Loader.instance().getModClassLoader();
        for (Field field : SourceCodedCore.class.getFields()) {
            if (field.isAnnotationPresent(SidedProxy.class)) {
                SidedProxy annotation = field.getAnnotation(SidedProxy.class);
                String targetType = side.isClient() ? annotation.clientSide() : annotation.serverSide();
                Object proxy = Class.forName(targetType, true, mcl).newInstance();
                field.set(null, proxy);
            }
        }
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        try {
            SCConfigManager.init(VersionConfig.createNewVersionConfig(event.getSuggestedConfigurationFile(), "0.1", SCCSharedData.getString("modid")));
        } catch (Exception e) {}
        proxy.preInit();
    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        proxy.init();
        SCCBlocks.registerAll();
    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
        proxy.c_keybinds();
        proxy.c_renderer();
    }

}
