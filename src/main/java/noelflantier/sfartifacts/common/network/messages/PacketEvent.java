package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.SoundHelper;
import noelflantier.sfartifacts.common.tileentities.TileLiquefier;

public class PacketEvent implements IMessage, IMessageHandler<PacketEvent, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int type;
	public int data;
	
	public PacketEvent(){
	
	}

	public PacketEvent(BlockPos pos, int type, int data){
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.type = type;
		this.data = data;
	}
	
	@Override
	public IMessage onMessage(PacketEvent message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
				@Override
				public void run() {
					SoundHelper.playEventSFA(message.type, new BlockPos(message.x,message.y,message.z), message.data);
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
	    type = buf.readInt();
	    data = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(type);
	    buf.writeInt(data);
		
	}
}