package sourcecoded.core.core.gameutility.screenshot;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import sourcecoded.core.core.client.Keybindings;
import sourcecoded.core.lib.config.SCConfigManager;

@SideOnly(Side.CLIENT)
public class ScreenshotTickHandler {

    public boolean isTakingScreenshotCurrently = false;         //Only run once

//    @SubscribeEvent                   Does not work in GUIs >.<
//    public void keyEvent(InputEvent.KeyInputEvent event) {
//        Minecraft mc = Minecraft.getMinecraft();
//        if (SourceCodedCore.keyScreenshot.isPressed()) {
//            IChatComponent data = SCScreenshotHandler.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
//            if (SCConfigManager.getBoolean(SCConfigManager.Properties.SCREENSHOT_MESSAGE)) mc.ingameGUI.getChatGUI().printChatMessage(data);
//        }
//    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (Keyboard.isKeyDown(Keybindings.keyScreenshot.getKeyCode())) {
            if (!isTakingScreenshotCurrently) {
                Minecraft mc = Minecraft.getMinecraft();
                IChatComponent data = SCScreenshotHandler.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
                if (mc.thePlayer != null && SCConfigManager.getBoolean(SCConfigManager.Properties.SCREENSHOT_MESSAGE))
                    mc.ingameGUI.getChatGUI().printChatMessage(data);
            }

            isTakingScreenshotCurrently = true;
        } else isTakingScreenshotCurrently = false;
    }
}