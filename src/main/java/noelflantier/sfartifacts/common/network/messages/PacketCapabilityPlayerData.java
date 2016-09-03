package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;

public class PacketCapabilityPlayerData implements IMessage, IMessageHandler<PacketCapabilityPlayerData, IMessage> {
	
	private NBTTagCompound data;

	public PacketCapabilityPlayerData(){
	}
	
	public PacketCapabilityPlayerData(EntityPlayer player){ 
		data = new NBTTagCompound();
		data = (NBTTagCompound) CapabilityPlayerProvider.PLAYER_DATA.getStorage().writeNBT(CapabilityPlayerProvider.PLAYER_DATA, player.getCapability(CapabilityPlayerProvider.PLAYER_DATA, null), null);
	}
	
	@Override
	public IMessage onMessage(PacketCapabilityPlayerData message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable(){
				@Override
				public void run() {
					CapabilityPlayerProvider.PLAYER_DATA.getStorage().readNBT(CapabilityPlayerProvider.PLAYER_DATA, SFArtifacts.myProxy.getPlayerEntity(ctx).getCapability(CapabilityPlayerProvider.PLAYER_DATA, null), null, message.data);
				}}
			);
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    data = ByteBufUtils.readTag(buf);
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    ByteBufUtils.writeTag(buf, data);
		
	}
}
