package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;
import noelflantier.sfartifacts.common.tileentities.ISFAEnergyHandler;

public class PacketEnergy  implements IMessage, IMessageHandler<PacketEnergy, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int energy;
	public int lastenergystored;
	
	public PacketEnergy(){
	
	}

	public PacketEnergy(BlockPos pos, int energy){
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.energy = energy;
		this.lastenergystored = -1;
	}

	public PacketEnergy(BlockPos pos, int energy, int lastenergystored){
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.energy = energy;
		this.lastenergystored = lastenergystored;
	}
	
	
	@Override
	public IMessage onMessage(PacketEnergy message, MessageContext ctx) {	
		if (ctx.side.isClient()) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable(){
				@Override
				public void run() {
					TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
					if(te!=null && te instanceof ISFAEnergyHandler) {
						((ISFAEnergyHandler)te).getEnergyStorage().setEnergyStored(message.energy);
						((ISFAEnergyHandler)te).setLastEnergyStored(message.lastenergystored);
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
	    energy = buf.readInt();
	    lastenergystored = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(energy);
	    buf.writeInt(lastenergystored);
		
	}
}
