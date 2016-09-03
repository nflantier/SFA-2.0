package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class PacketUpgradeHammer implements IMessage, IMessageHandler<PacketUpgradeHammer, IMessage> {
	public int x;
	public int y;
	public int z;
	public int idUpgrade;
	public boolean fromStand;
	
	public PacketUpgradeHammer(){
	
	}

	public PacketUpgradeHammer(TileHammerStand t, int idUpgrade, boolean fromStand){
		this.fromStand = fromStand;
		if(fromStand){
			this.x = t.getPos().getX();
			this.y = t.getPos().getY();
			this.z = t.getPos().getZ();
		}
		this.idUpgrade = idUpgrade;
	}
	
	@Override
	public IMessage onMessage(PacketUpgradeHammer message, MessageContext ctx) {
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
					switch(message.idUpgrade){
						case 1 :
							int r = ItemNBTHelper.getInteger(hammer, "Radius", 0)-1;
							ItemNBTHelper.setInteger(hammer, "Radius", r<0?0:r);
							break;
						case 2 :
							int r1 = ItemNBTHelper.getInteger(hammer, "Radius", 0)+1;
							ItemNBTHelper.setInteger(hammer, "Radius", r1>5?5:r1);
							break;
						case 3 :
							ItemNBTHelper.setBoolean(hammer, "IsMagnetOn", true);
							break;
						case 4 :
							ItemNBTHelper.setBoolean(hammer, "IsMagnetOn", false);
							break;
						default : 
							break;
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
	    idUpgrade = buf.readInt();
	    fromStand = buf.readBoolean();
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(idUpgrade);
	    buf.writeBoolean(fromStand);
		
	}
}
