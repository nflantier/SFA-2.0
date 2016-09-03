package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.TileSoundEmiter;

public class PacketSoundEmitter implements IMessage, IMessageHandler<PacketSoundEmitter, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isEmitting;
	
	public PacketSoundEmitter(){
	
	}

	public PacketSoundEmitter(TileSoundEmiter te){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.isEmitting = te.isEmitting;
	}
	
	@Override
	public IMessage onMessage(PacketSoundEmitter message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof TileSoundEmiter) {
					((TileSoundEmiter)te).isEmitting = message.isEmitting;
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
	    isEmitting = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isEmitting);	
	}
}

