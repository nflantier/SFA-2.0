package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;

public class PacketHammerConfig  implements IMessage, IMessageHandler<PacketHammerConfig, IMessage>{

	public int idGui;
	
	public PacketHammerConfig(){
		
	}

	public PacketHammerConfig(int id){
		this.idGui = id;
	}
	
	@Override
	public IMessage onMessage(PacketHammerConfig message, MessageContext ctx) {
		SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
			@Override
			public void run() {
				ctx.getServerHandler().playerEntity.openGui(SFArtifacts.instance, message.idGui, ctx.getServerHandler().playerEntity.worldObj, (int)ctx.getServerHandler().playerEntity.posX, (int)ctx.getServerHandler().playerEntity.posY, (int)ctx.getServerHandler().playerEntity.posZ);
			}}
		);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	    this.idGui = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {	
	    buf.writeInt(this.idGui);	
	}

}
