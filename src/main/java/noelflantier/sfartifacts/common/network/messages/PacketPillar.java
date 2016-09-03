package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.pillar.TileMasterPillar;

public class PacketPillar implements IMessage, IMessageHandler<PacketPillar, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int amountToExtract;
	public int passiveEnergy;
	public int fluidEnergy;
	public int isRenderingPillarModel;
	
	public PacketPillar(){
		
	}
	
	public PacketPillar(TileMasterPillar t){
		this.x = t.getPos().getX();
		this.y = t.getPos().getY();
		this.z = t.getPos().getZ();
		this.amountToExtract = t.amountToExtract;
		this.passiveEnergy = t.passiveEnergy;
		this.fluidEnergy = t.fluidEnergy;
	}
	
	@Override
	public IMessage onMessage(PacketPillar message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof TileMasterPillar) {
					TileMasterPillar me = (TileMasterPillar)te;
					me.amountToExtract = message.amountToExtract;
					me.passiveEnergy = message.passiveEnergy;
					me.fluidEnergy = message.fluidEnergy;
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
	    passiveEnergy = buf.readInt();
	    fluidEnergy = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(amountToExtract);	
	    buf.writeInt(passiveEnergy);
	    buf.writeInt(fluidEnergy);
	}
}