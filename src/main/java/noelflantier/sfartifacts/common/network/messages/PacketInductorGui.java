package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.TileInductor;

public class PacketInductorGui  implements IMessage, IMessageHandler<PacketInductorGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int type;
	public boolean typevalue;
	
	public PacketInductorGui(){
	
	}

	public PacketInductorGui(TileInductor te, int type, boolean tv){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.type = type;
		this.typevalue = tv;
	}
	
	@Override
	public IMessage onMessage(PacketInductorGui message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof TileInductor) {
					switch(message.type){
						case 0 :
							((TileInductor)te).canWirelesslySend = message.typevalue;
							break;
						case 1 :
							((TileInductor)te).canWirelesslyRecieve = message.typevalue;
							break;
						case 2 :
							((TileInductor)te).canSend = message.typevalue;
							break;
						case 3 :
							((TileInductor)te).canRecieve = message.typevalue;
							break;
						default:
							break;
					}
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
	    type = buf.readInt();
	    typevalue = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(type);
	    buf.writeBoolean(typevalue);
		
	}
}