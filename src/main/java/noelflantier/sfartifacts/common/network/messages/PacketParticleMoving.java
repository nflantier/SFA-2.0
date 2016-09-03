package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class PacketParticleMoving  implements IMessage, IMessageHandler<PacketParticleMoving, IMessage> {
	
	public double xTo;
	public double yTo;
	public double zTo;
	public double xFrom;
	public double yFrom;
	public double zFrom;
	
	public PacketParticleMoving(){
	
	}

	public PacketParticleMoving(BlockPos posto, BlockPos posfrom){
		this.xTo = posto.getX();
		this.yTo = posto.getY();
		this.zTo = posto.getZ();
		this.xFrom = posfrom.getX();
		this.yFrom = posfrom.getY();
		this.zFrom = posfrom.getZ();
	}
	
	@Override
	public IMessage onMessage(PacketParticleMoving message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				ParticleHelper.spawnCustomParticle(ParticleHelper.Type.ENERGYFLOW, (double)message.xFrom+0.5, (double)message.yFrom+0.5, (double)message.zFrom+0.5, new Object[]{(double)message.xTo+0.5, (double)message.yTo+0.5, (double)message.zTo+0.5,1});
			}}
		);
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    xTo = buf.readDouble();
	    yTo = buf.readDouble();
	    zTo = buf.readDouble();
	    xFrom = buf.readDouble();
	    yFrom = buf.readDouble();
	    zFrom = buf.readDouble();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeDouble(xTo);
	    buf.writeDouble(yTo);
	    buf.writeDouble(zTo);
	    buf.writeDouble(xFrom);
	    buf.writeDouble(yFrom);
	    buf.writeDouble(zFrom);
		
	}
}
