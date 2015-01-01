package sourcecoded.core.core.proxy;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import sourcecoded.core.core.block.TileDozer;
import sourcecoded.core.core.client.Keybindings;
import sourcecoded.core.core.client.renderer.block.RenderDozer;
import sourcecoded.core.core.gameutility.screenshot.ScreenshotShareCommand;
import sourcecoded.core.core.gameutility.screenshot.ScreenshotTickHandler;
import sourcecoded.core.lib.config.SCConfigManager;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        if (SCConfigManager.getBoolean(SCConfigManager.Properties.SCREENSHOT_ENABLED)) {
            FMLCommonHandler.instance().bus().register(new ScreenshotTickHandler());
            ClientCommandHandler.instance.registerCommand(new ScreenshotShareCommand());
        }
    }

    @Override
    public void c_renderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileDozer.class, new RenderDozer());
    }

    @Override
    public void c_keybinds() {
        Keybindings.initKeybinds();
    }

}
