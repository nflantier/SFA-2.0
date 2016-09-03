package noelflantier.sfartifacts.common.network.messages;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class PacketParticleGlobal implements IMessage, IMessageHandler<PacketParticleGlobal, IMessage> {
	
	public int id;
	public int x;
	public int y;
	public int z;
	
	public PacketParticleGlobal(){
	
	}

	public PacketParticleGlobal(ParticleHelper.Type type, int x, int y, int z){
		this.id = type.ordinal();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public IMessage onMessage(PacketParticleGlobal message, MessageContext ctx) {
		if(ParticleHelper.Type.values()[message.id]==null)
			return null;
		
		if(ParticleHelper.Type.values()[message.id]==ParticleHelper.Type.BOLT){
			
		}else if(ParticleHelper.Type.values()[message.id]==ParticleHelper.Type.ENERGYFLOW){
			
		}else if(ParticleHelper.Type.values()[message.id]==ParticleHelper.Type.ASGARDIANORES){
			if(ModConfig.isOresEmitParticles)
				ParticleHelper.spawnAsgardianParticles(message.x, message.y, message.z,new Random(),0.5F);
		}else if(ParticleHelper.Type.values()[message.id]==ParticleHelper.Type.LIGHTNING){
			
		}
		return null;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
	    id = buf.readInt();
	    x = buf.readInt();
	    y = buf.readInt();
	    z = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(id);
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
		
	}
}