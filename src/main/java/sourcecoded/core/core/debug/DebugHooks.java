package sourcecoded.core.core.debug;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public enum DebugHooks {
    INSTANCE;

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void guiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiMainMenu)
            event.buttonList.add(new GuiButton(137, event.gui.width - 25, event.gui.height - 25, 20, 20, "C"));
    }

    @SubscribeEvent
    public void guiAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (event.gui instanceof GuiMainMenu)
            if (event.button.id == 137) {
                int i = 1/0;    //Exception Inducer
            }
    }

}
