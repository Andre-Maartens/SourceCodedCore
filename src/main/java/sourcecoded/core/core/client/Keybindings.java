package sourcecoded.core.core.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keybindings {

    public static KeyBinding keyScreenshot = new KeyBinding("Take Screenshot", Keyboard.KEY_F2, "SourceCodedCore");

    public static void initKeybinds() {
        ClientRegistry.registerKeyBinding(Keybindings.keyScreenshot);
    }

}
