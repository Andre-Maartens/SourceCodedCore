package sourcecoded.core.lib.client.renderer.obj;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class GroupObject {

    public String name;
    public ArrayList<Face> faces = new ArrayList<Face>();
    public int glDrawingMode;

    public GroupObject()
    {
        this("");
    }

    public GroupObject(String name)
    {
        this(name, -1);
    }

    public GroupObject(String name, int glDrawingMode)
    {
        this.name = name;
        this.glDrawingMode = glDrawingMode;
    }

    @SideOnly(Side.CLIENT)
    public void render()
    {
        if (faces.size() > 0)
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer wr = tessellator.getWorldRenderer();
            wr.startDrawing(glDrawingMode);
            render(tessellator, wr);
            tessellator.draw();
        }
    }

    @SideOnly(Side.CLIENT)
    public void render(Tessellator tessellator, WorldRenderer wr)
    {
        if (faces.size() > 0)
        {
            for (Face face : faces)
            {
                face.addFaceForRender(tessellator, wr);
            }
        }
    }

}
