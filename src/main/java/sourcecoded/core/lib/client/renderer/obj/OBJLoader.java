package sourcecoded.core.lib.client.renderer.obj;

import net.minecraft.util.ResourceLocation;

public class OBJLoader {

    public static WavefrontObject loadModel(ResourceLocation loc) {
        return new WavefrontObject(loc);
    }

}
