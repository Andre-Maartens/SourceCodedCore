package sourcecoded.core.core.registrar;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import sourcecoded.core.core.block.BlockDozer;
import sourcecoded.core.core.block.TileDozer;

public enum SCCBlocks {
    DOZER(new BlockDozer(), "bobbledozer", TileDozer.class)
    ;

    Block block;
    String unlocName;
    Class<? extends TileEntity> te;

    SCCBlocks(Block block, String name) {
        this.block = block;
        this.unlocName = name;
    }

    SCCBlocks(Block block, String name, Class<? extends TileEntity> teClass) {
        this(block, name);
        te = teClass;
    }

    public void register() {
        GameRegistry.registerBlock(block, unlocName);
        if (te != null)
            GameRegistry.registerTileEntity(te, unlocName);
    }

    public static void registerAll() {
        for (SCCBlocks block : SCCBlocks.values())
            block.register();
    }
}
