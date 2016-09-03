package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.TileInductor;

public class PacketInductor implements IMessage, IMessageHandler<PacketInductor, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean canSend;
	public boolean canRecieve;
	public boolean canWSend;
	public boolean canWRecieve;
	
	public PacketInductor(){
	
	}

	public PacketInductor(TileInductor te){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.canWRecieve = te.canWirelesslyRecieve;
		this.canWSend = te.canWirelesslySend;
		this.canRecieve = te.canRecieve;
		this.canSend = te.canSend;
	}
	
	@Override
	public IMessage onMessage(PacketInductor message, MessageContext ctx) {		
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof TileInductor) {
					((TileInductor)te).canWirelesslyRecieve = message.canWRecieve;
					((TileInductor)te).canWirelesslySend = message.canWSend;
					((TileInductor)te).canRecieve = message.canRecieve;
					((TileInductor)te).canSend = message.canSend;
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
	    canWRecieve = buf.readBoolean();
	    canWSend = buf.readBoolean();
	    canRecieve = buf.readBoolean();
	    canSend = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(canWRecieve);
	    buf.writeBoolean(canWSend);
	    buf.writeBoolean(canRecieve);
	    buf.writeBoolean(canSend);
		
	}
}