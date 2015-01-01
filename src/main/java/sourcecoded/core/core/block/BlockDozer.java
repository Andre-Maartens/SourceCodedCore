package sourcecoded.core.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

//Don't ask
public class BlockDozer extends Block implements ITileEntityProvider {

    public static final PropertyInteger ROTATION_PROP = PropertyInteger.create("rotation", 0, 15);

    public BlockDozer() {
        super(Material.rock);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATION_PROP, Integer.valueOf(0)));
        this.setUnlocalizedName("bobbledozer");
        this.setHardness(1F);
        this.setBlockBounds(0.3F, 0F, 0.3F, 0.7F, 0.6F, 0.7F);
    }

    public int getRenderType() {
        return -1;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileDozer();
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        TileDozer te = (TileDozer) worldIn.getTileEntity(pos);
        te.bobble();
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        int i = MathHelper.floor_double((double)((placer.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
        return getDefaultState().withProperty(ROTATION_PROP, i);
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ROTATION_PROP, Integer.valueOf(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(ROTATION_PROP)).intValue();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {ROTATION_PROP});
    }

}
