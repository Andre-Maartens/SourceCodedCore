package sourcecoded.core.core.client.renderer.block;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import sourcecoded.core.core.block.BlockDozer;
import sourcecoded.core.core.block.TileDozer;
import sourcecoded.core.lib.client.renderer.obj.OBJLoader;
import sourcecoded.core.lib.client.renderer.obj.WavefrontObject;

//Just for shits' and giggles
public class RenderDozer extends TileEntitySpecialRenderer {

    WavefrontObject obj = OBJLoader.loadModel(new ResourceLocation("sourcecodedcore", "bobbledozer.obj"));
    ResourceLocation green = new ResourceLocation("sourcecodedcore", "Tex_0615_0.png");
    ResourceLocation black = new ResourceLocation("sourcecodedcore", "Tex_0625_0.png");
    ResourceLocation elite = new ResourceLocation("sourcecodedcore", "Tex_0635_0.png");

    @Override
    public void renderTileEntityAt(TileEntity tes, double x, double y, double z, float ptt, int partialDamage) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);

        TileDozer te = (TileDozer) tes;

        //float scale = 0.5F;
        float scale = 0.5F;
        float scale2 = 3F;

        x += 0.5D;
        z += 0.5D;
        GL11.glTranslated(x, y, z);
        GL11.glScalef(scale, scale, scale);

        float f2 = (float)(tes.getBlockMetadata() * 360) / 16.0F;

        GL11.glRotatef(-f2 + 180, 0F, 1F, 0F);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer wr = tess.getWorldRenderer();

        if (te.variant == 0)
            this.bindTexture(elite);
        else if (te.variant == 1)
            this.bindTexture(black);
        else
            this.bindTexture(green);

        wr.startDrawing(GL11.GL_TRIANGLES);

        obj.tessellateAllExcept(tess, "Mesh_0644", "Mesh_0643", "Mesh_0641", "Mesh_0640", "Mesh_0639", "Mesh_0635");

        tess.draw();
        wr.startDrawing(GL11.GL_TRIANGLES);

        float xv = te.lastBobbleTime + (te.bobbleTime - te.lastBobbleTime) * ptt;

        float offset = (float) ((xv / 5F) * Math.sin((xv*xv)/50F));

        GL11.glScalef(scale2, scale2, scale2);
        GL11.glRotatef(45F + offset, 1F, 0F, 0F);
        GL11.glTranslatef(-0.03F, -0.6F, -0.1F);

        GL11.glRotatef(10F, 0F, 0F, 0F);

        obj.tessellateOnly(tess, "Mesh_0644", "Mesh_0643", "Mesh_0641", "Mesh_0640", "Mesh_0639", "Mesh_0635");

        tess.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

}
