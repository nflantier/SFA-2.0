package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;

public class PacketPillarGui  implements IMessage, IMessageHandler<PacketPillarGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int amountToExtract;
	
	public PacketPillarGui(){
	
	}

	public PacketPillarGui(BlockPos pos, int amountToExtract){
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.amountToExtract = amountToExtract;
	}
	
	@Override
	public IMessage onMessage(PacketPillarGui message, MessageContext ctx) {
		SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity( new BlockPos(message.x, message.y, message.z));
				if(te!=null && te instanceof TileMasterPillar) {
					((TileMasterPillar)te).amountToExtract = ((TileMasterPillar)te).amountToExtract+message.amountToExtract>ModConfig.maxAmountPillarCanExtract? 
							ModConfig.maxAmountPillarCanExtract:((TileMasterPillar)te).amountToExtract+message.amountToExtract<0?0:((TileMasterPillar)te).amountToExtract+message.amountToExtract;
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
	    amountToExtract = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(amountToExtract);
		
	}
}
