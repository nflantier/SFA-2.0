package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;

public class PacketMightyFoundry implements IMessage, IMessageHandler<PacketMightyFoundry, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isRunning;
	public boolean isLocked;
	public double progression;
	
	public PacketMightyFoundry(){
	
	}

	public PacketMightyFoundry(TileMightyFoundry te){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.isLocked = te.isLocked;
		this.isRunning = te.isRunning;
		this.progression = te.progression;
	}
	
	@Override
	public IMessage onMessage(PacketMightyFoundry message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof TileMightyFoundry) {
					((TileMightyFoundry)te).progression = message.progression;
					((TileMightyFoundry)te).isRunning = message.isRunning;
				}
			}}
		);
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    isLocked = buf.readBoolean();
	    isRunning = buf.readBoolean();
	    progression = buf.readDouble();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isLocked);
	    buf.writeBoolean(isRunning);
	    buf.writeDouble(progression);
	}
}
