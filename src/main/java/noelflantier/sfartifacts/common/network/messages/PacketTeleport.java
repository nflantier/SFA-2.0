package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.SoundHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;

public class PacketTeleport implements IMessage, IMessageHandler<PacketTeleport, IMessage> {

	public String name = "";
	public String coord = "";
	public int idTask;
	public int idLink = -1;
	
	public PacketTeleport(){
	
	}

	public PacketTeleport(int idTask, String name, String coord){
		this.idTask = idTask;
		this.name = name;
		this.coord = coord;
	}

	public PacketTeleport(int idTask, int idLink){
		this.idTask = idTask;
		this.idLink = idLink;
	}

	public PacketTeleport(int idTask, String coord){
		this.idTask = idTask;
		this.coord = coord;
	}
	
	@Override
	public IMessage onMessage(PacketTeleport message, MessageContext ctx) {
		
		SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
			@Override
			public void run() {
				ItemStack hammer = ctx.getServerHandler().playerEntity.getHeldItemMainhand();
				if(hammer==null || hammer.getItem() instanceof ItemThorHammer == false){
					hammer = ctx.getServerHandler().playerEntity.getHeldItemOffhand();
					if(hammer==null || hammer.getItem() instanceof ItemThorHammer == false)
						return;
				}
				
				if(hammer!=null){
					switch(message.idTask){
						case 0 :
							if (!hammer.getTagCompound().hasKey("TeleportCoord", 9))
								hammer.getTagCompound().setTag("TeleportCoord", new NBTTagList());
							NBTTagList nbttaglist = hammer.getTagCompound().getTagList("TeleportCoord", 10);
					        NBTTagCompound nbttagcompound = new NBTTagCompound();
					        nbttagcompound.setString("name", message.name);
					        nbttagcompound.setString("coord", message.coord);
					        nbttaglist.appendTag(nbttagcompound);
					        break;
						case 1 :
							NBTTagList nbttaglist1 = hammer.getTagCompound().getTagList("TeleportCoord", 10);
							if(nbttaglist1.getCompoundTagAt(message.idLink)!=null)
								nbttaglist1.removeTag(message.idLink);
							break;
						case 2 :
							String[] st = message.coord.split(",");
							if(st.length==4){
								if (st[0].matches("[+-]?[0-9]+") && st[1].matches("[+-]?[0-9]+") && st[2].matches("[+-]?[0-9]+") && st[3].matches("[+-]?[0-9]+")){
									
									SoundHelper.playEventSFA(1001, null, 0);
									ctx.getServerHandler().playerEntity.worldObj.addWeatherEffect(new EntityLightningBolt(ctx.getServerHandler().playerEntity.worldObj, ctx.getServerHandler().playerEntity.posX+3, ctx.getServerHandler().playerEntity.posY, ctx.getServerHandler().playerEntity.posZ+3, true));
				                	HammerHelper.startTeleporting(ctx.getServerHandler().playerEntity, st);
				            		HammerHelper.extractEnergyInHammer(hammer,((ItemThorHammer)hammer.getItem()).energyTeleporting);
				            		SoundHelper.playEventSFA(1001, null, 0);
								}
							}
							break;
						default:
							break;
					}
				}
			}
		});
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		idTask = buf.readInt();
		idLink = buf.readInt();
	    int l = buf.readInt();
	    name = "";
	    for(int i = 0;i<l;i++)
	    	name = name+buf.readChar();
	    int l2 = buf.readInt();
	    coord = "";
	    for(int i = 0;i<l2;i++)
	    	coord = coord+buf.readChar();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(idTask);
	    buf.writeInt(idLink);
	    buf.writeInt(name.length());
	    for(int i=0;i<name.length();i++)
	    	buf.writeChar(name.charAt(i));
	    buf.writeInt(coord.length());
	    for(int i=0;i<coord.length();i++)
	    	buf.writeChar(coord.charAt(i));
	}

}
