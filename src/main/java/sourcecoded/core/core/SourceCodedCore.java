package sourcecoded.core.core;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import sourcecoded.core.lib.SCCSharedData;

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
}
