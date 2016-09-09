package noelflantier.sfartifacts.common.network.messages;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.common.tileentities.ISFAFluid;

public class PacketFluid  implements IMessage, IMessageHandler<PacketFluid, IMessage> {
	
	public int x;
	public int y;
	public int z;
	public int quantity[];
	public int capacity[];
	public String fluidName[];
	
	public PacketFluid(){
	
	}

	public PacketFluid(BlockPos pos, int[] quantity, int[] capacity, String[] fluidId){
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.quantity = quantity.clone();
		this.capacity = capacity.clone();
		this.fluidName = fluidId.clone();
	}
	
	@Override
	public IMessage onMessage(PacketFluid message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				TileEntity te = Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(message.x,message.y, message.z));
				if(te!=null && te instanceof ISFAFluid) {
					List<FluidTank> ft = ((ISFAFluid)te).getFluidTanks();
					if(ft!=null){
						int i = 0;
						for(FluidTank f: ft){
							f.setFluid(FluidRegistry.getFluidStack(message.fluidName[i], message.quantity[i]));
							f.setCapacity(message.capacity[i]);
							i+=1;
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
	    int qt = buf.readInt();
	    this.quantity = new int[qt];
	    for(int i =0;i<qt;i++)
	    	this.quantity[i] = buf.readInt();
	    int cp = buf.readInt();
	    this.capacity = new int[cp];
	    for(int i =0;i<cp;i++)
	    	this.capacity[i] = buf.readInt();
	    
	    int fi = buf.readInt();
	    this.fluidName = new String[fi];
	    for(int i =0;i<fi;i++){
		    int lst = buf.readInt();
		    char tc[] = new char[lst];
		    for(int j =0;j<lst;j++)
		    	tc[j] = buf.readChar();
		    this.fluidName[i] = new String(tc);
	    }
		
	}
	@Override
	public void toBytes(ByteBuf buf) {
	    buf.writeInt(x);
	    buf.writeInt(y);
	    buf.writeInt(z);
	    buf.writeInt(this.quantity.length);
	    for(int i =0;i<this.quantity.length;i++)
	    	buf.writeInt(this.quantity[i]);
	    buf.writeInt(this.capacity.length);
	    for(int i =0;i<this.capacity.length;i++)
	    	buf.writeInt(this.capacity[i]);
	    buf.writeInt(this.fluidName.length);
	    for(int i =0;i<this.fluidName.length;i++){
	    	char tc[] = this.fluidName[i].toCharArray();
	    	buf.writeInt(tc.length);
		    for(int j =0;j<tc.length;j++)
		    	buf.writeChar(tc[j]);
	    }
		
	}
}
