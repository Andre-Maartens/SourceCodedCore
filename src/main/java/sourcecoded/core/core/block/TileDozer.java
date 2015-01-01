package sourcecoded.core.core.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileDozer extends TileEntity implements IUpdatePlayerListBox {

    public int bobbleTime = -1;
    public int lastBobbleTime = -1;

    public int variant = 0;

    public TileDozer() {
        variant = new Random().nextInt(3);
    }

    public void bobble() {
        bobbleTime = 40;
        lastBobbleTime = bobbleTime;
    }

    public void update() {
        if (bobbleTime > 0) {
            lastBobbleTime = bobbleTime;
            bobbleTime--;

            this.markDirty();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        tags.setInteger("bobbleTime", bobbleTime);
        tags.setInteger("lastBobbleTime", lastBobbleTime);
        tags.setInteger("variant", variant);
    }

    @Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        bobbleTime = tags.getInteger("bobbleTime");
        lastBobbleTime = tags.getInteger("lastBobbleTime");
        variant = tags.getInteger("variant");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tags = new NBTTagCompound();
        writeToNBT(tags);
        return new S35PacketUpdateTileEntity(getPos(), 1, tags);
    }

    @Override
    public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        markDirty();
    }

}
