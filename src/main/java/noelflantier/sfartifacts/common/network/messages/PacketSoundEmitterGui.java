package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.recipes.handler.SoundEmitterConfig;
import noelflantier.sfartifacts.common.tileentities.TileSoundEmiter;

public class PacketSoundEmitterGui implements IMessage, IMessageHandler<PacketSoundEmitterGui, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int frequency;
	public int mode;
	public int variant;
	
	public PacketSoundEmitterGui(){
	
	}

	public PacketSoundEmitterGui(BlockPos pos, int frequency, int mode, int variant){
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.frequency = frequency;
		this.mode = mode;
		this.variant = variant;
	}
	
	
	@Override
	public IMessage onMessage(PacketSoundEmitterGui message, MessageContext ctx) {
		SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te instanceof TileSoundEmiter){
					if(message.mode==0){
						((TileSoundEmiter)te).variant = message.variant;
						((TileSoundEmiter)te).frequencyEmited = message.frequency;
					}
					if(message.mode==1){
						((TileSoundEmiter)te).variant = message.variant;
						((TileSoundEmiter)te).frequencySelected = message.frequency;
					}
					if(message.mode==2){
						((TileSoundEmiter)te).isEmitting = false;
						((TileSoundEmiter)te).frequencyEmited = 0;
						((TileSoundEmiter)te).entityNameToSpawn = null;
					}
					if(message.mode==3){
						((TileSoundEmiter)te).isEmitting = true;
					}
					if(message.mode==4){
						String s = SoundEmitterConfig.getInstance().getNameForFrequency(message.frequency);
						if(((TileSoundEmiter)te).listScannedFrequency.containsKey(message.frequency))
								((TileSoundEmiter)te).listScannedFrequency.remove(message.frequency);
						((TileSoundEmiter)te).listScannedFrequency.put(message.frequency, s);
					}
					if(message.mode==5){
						((TileSoundEmiter)te).variant = message.variant;
					}
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
	    frequency = buf.readInt();
	    mode = buf.readInt();
	    variant = buf.readInt();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(frequency);
	    buf.writeInt(mode);
	    buf.writeInt(variant);
		
	}

}
