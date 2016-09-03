package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.TileInjector;

public class PacketInjector implements IMessage, IMessageHandler<PacketInjector, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public boolean isRunning[];
	public int currentTickToInject[];
	public int tickToInject;
	
	public PacketInjector(){
	
	}

	public PacketInjector(TileInjector te){
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.isRunning = te.isRunning.clone();
		this.currentTickToInject = te.currentTickToInject.clone();
		this.tickToInject = te.tickToInject;
	}
	
	@Override
	public IMessage onMessage(PacketInjector message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof TileInjector) {
					((TileInjector)te).isRunning = message.isRunning.clone();
					((TileInjector)te).currentTickToInject = message.currentTickToInject.clone();
					((TileInjector)te).tickToInject = message.tickToInject;
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
	    int rl = buf.readInt();
	    isRunning = new boolean[rl];
	    for(int i =0;i<rl;i++)
	    	isRunning[i] = buf.readBoolean();
	    int ctl = buf.readInt();
	    currentTickToInject = new int[ctl];
	    for(int i =0;i<ctl;i++)
	    	currentTickToInject[i] = buf.readInt();
	    tickToInject = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(isRunning.length);
	    for(int i =0;i<isRunning.length;i++)
	    	buf.writeBoolean(isRunning[i]);
	    buf.writeInt(currentTickToInject.length);
	    for(int i =0;i<currentTickToInject.length;i++)
	    	buf.writeInt(currentTickToInject[i]);
	    buf.writeInt(tickToInject);
		
	}
}
