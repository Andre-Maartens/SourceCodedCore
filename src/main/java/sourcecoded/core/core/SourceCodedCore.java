package sourcecoded.core.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.MetadataCollection;
import net.minecraftforge.fml.common.ModMetadata;
import sourcecoded.core.lib.SCCPrefs;

import java.util.Arrays;

import static net.minecraftforge.common.ForgeVersion.*;

public class SourceCodedCore extends DummyModContainer {

    /* START MOD SETUP */

    public SourceCodedCore() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = SCCPrefs.getString("modid");
        meta.name = SCCPrefs.getString("name");
        meta.version = SCCPrefs.getString("version");
        meta.authorList = Arrays.asList("SourceCodedCore");
        meta.description = SCCPrefs.getString("description");
        meta.screenshots = new String[0];
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    /* END SETUP */

}
