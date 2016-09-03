package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.TileMachine;

public class PacketMachine implements IMessage, IMessageHandler<PacketMachine, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isManualyEnable;
	
	public PacketMachine(){
	
	}

	public PacketMachine(TileMachine te){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.isManualyEnable = te.isManualyEnable;
	}
	
	@Override
	public IMessage onMessage(PacketMachine message, MessageContext ctx) {	
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity t = ctx.getServerHandler().playerEntity.worldObj.getTileEntity( new BlockPos(message.x, message.y, message.z));	
				if(t!=null && t instanceof TileMachine) {
					((TileMachine)t).isManualyEnable = message.isManualyEnable;
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
	    isManualyEnable = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(isManualyEnable);
		
	}
}
