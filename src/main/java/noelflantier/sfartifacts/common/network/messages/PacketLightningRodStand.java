package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.tileentities.TileLightningRodStand;

public class PacketLightningRodStand implements IMessage, IMessageHandler<PacketLightningRodStand, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int lightningRodEnergy;
	
	public PacketLightningRodStand(){
		
	}
	
	public PacketLightningRodStand(TileLightningRodStand t){
		this.x = t.getPos().getX();
		this.y = t.getPos().getY();
		this.z = t.getPos().getZ();
		this.lightningRodEnergy = t.lightningRodEnergy;
	}
	
	@Override
	public IMessage onMessage(PacketLightningRodStand message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
				@Override
				public void run() {
					TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
					if(te!=null && te instanceof TileLightningRodStand) {
						TileLightningRodStand me = (TileLightningRodStand)te;
						me.lightningRodEnergy = message.lightningRodEnergy;
					}
				}}
			);
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {	
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
	    lightningRodEnergy = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(lightningRodEnergy);
	}
}
