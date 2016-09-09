package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.tileentities.TileLiquefier;

public class PacketLiquefier implements IMessage, IMessageHandler<PacketLiquefier, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isRunning;
	public int currentTickToMelt;
	public int tickToMelt;
	
	public PacketLiquefier(){
	
	}

	public PacketLiquefier(TileLiquefier te){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.isRunning = te.isRunning;
		this.currentTickToMelt = te.currentTickToMelt;
		this.tickToMelt = te.tickToMelt;
	}
	
	@Override
	public IMessage onMessage(PacketLiquefier message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
				@Override
				public void run() {
					TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
					if(te!=null && te instanceof TileLiquefier) {
						((TileLiquefier)te).isRunning = message.isRunning;
						((TileLiquefier)te).currentTickToMelt = message.currentTickToMelt;
						((TileLiquefier)te).tickToMelt = message.tickToMelt;
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
	    isRunning = buf.readBoolean();
	    currentTickToMelt = buf.readInt();
	    tickToMelt = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isRunning);
	    buf.writeInt(currentTickToMelt);
	    buf.writeInt(tickToMelt);
		
	}
}
