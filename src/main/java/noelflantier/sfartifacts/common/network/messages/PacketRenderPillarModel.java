package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.tileentities.pillar.TileRenderPillarModel;

public class PacketRenderPillarModel implements IMessage, IMessageHandler<PacketRenderPillarModel, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int isRenderingPillarModel;
	
	public PacketRenderPillarModel(){
		
	}
	
	public PacketRenderPillarModel(TileRenderPillarModel t){
		this.x = t.getPos().getX();
		this.y = t.getPos().getY();
		this.z = t.getPos().getZ();
		this.isRenderingPillarModel = t.isRenderingPillarModel;
	}
	
	@Override
	public IMessage onMessage(PacketRenderPillarModel message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
				@Override
				public void run() {
					TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
					if(te!=null && te instanceof TileRenderPillarModel) {
						TileRenderPillarModel me = (TileRenderPillarModel)te;
						me.isRenderingPillarModel = message.isRenderingPillarModel;
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
	    isRenderingPillarModel = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(isRenderingPillarModel);
	}
}
