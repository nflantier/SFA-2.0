package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.handlers.capabilities.CapabilityPlayerProvider;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class PacketEnchantHammer  implements IMessage, IMessageHandler<PacketEnchantHammer, IMessage> {
	public int x;
	public int y;
	public int z;
	public boolean fromStand;
	
	public int enchantId;
	public boolean enchantValue;
	
	public PacketEnchantHammer(){
		
	}
	
	public PacketEnchantHammer(TileHammerStand t, int enchantid, boolean enchantvalue, boolean fromstand){
		this.fromStand = fromstand;
		if(fromStand){
			this.x = t.getPos().getX();
			this.y = t.getPos().getY();
			this.z = t.getPos().getZ();
		}
		
		this.enchantId = enchantid;
		this.enchantValue = enchantvalue;
	}

	@Override
	public IMessage onMessage(PacketEnchantHammer message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				ItemStack hammer = new ItemStack(ModItems.itemThorHammer);
				
				if(message.fromStand){
					TileEntity t = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(new BlockPos( message.x, message.y, message.z ) );
					if(t!=null && t instanceof TileHammerStand)
						hammer = ((TileHammerStand)t).items[0];
				}else if(!message.fromStand){
					hammer = ctx.getServerHandler().playerEntity.getHeldItemMainhand();
					if(hammer==null)
						hammer = ctx.getServerHandler().playerEntity.getHeldItemOffhand();
				}
		
				if(hammer!=null){
					NBTTagList nbttaglist = hammer.getTagCompound().getTagList("EnchStored", 10);
					hammer.getTagCompound().removeTag("ench");
			        if (nbttaglist != null){
			    		for (int i = 0; i < nbttaglist.tagCount(); ++i)
				        {
				            if(nbttaglist.getCompoundTagAt(i).getShort("id")==message.enchantId){
				            	nbttaglist.getCompoundTagAt(i).setBoolean("enable",message.enchantValue);
				            }
				            if(nbttaglist.getCompoundTagAt(i).getBoolean("enable")){
				            	hammer.addEnchantment(Enchantment.getEnchantmentByID(nbttaglist.getCompoundTagAt(i).getShort("id")), nbttaglist.getCompoundTagAt(i).getInteger("lvl"));
				            }
				        }
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
	    fromStand = buf.readBoolean();
		this.enchantId = buf.readInt();
		this.enchantValue = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeBoolean(fromStand);
	    buf.writeInt(enchantId);
	    buf.writeBoolean(enchantValue);
	}
}
