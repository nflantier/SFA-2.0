package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.tileentities.TileRecharger;

public class PacketRechargerGui implements IMessage, IMessageHandler<PacketRechargerGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean wirelessRechargingEnable;
	
	public PacketRechargerGui(){
	
	}

	public PacketRechargerGui(TileRecharger te){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.wirelessRechargingEnable = te.wirelessRechargingEnable;
	}
	
	@Override
	public IMessage onMessage(PacketRechargerGui message, MessageContext ctx) {
		SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof TileRecharger) {
					((TileRecharger)te).wirelessRechargingEnable = message.wirelessRechargingEnable;
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
	    wirelessRechargingEnable = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(wirelessRechargingEnable);
		
	}

}
