package noelflantier.sfartifacts.common.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;

public class PacketInvokStarting implements IMessage, IMessageHandler<PacketInvokStarting, IMessage> {
		
		public int x;
		public int y;
		public int z;
		
		public PacketInvokStarting(){
		
		}

		public PacketInvokStarting(TileHammerStand t){
			this.x = t.getPos().getX();
			this.y = t.getPos().getY();
			this.z = t.getPos().getZ();
		}
		
		@Override
		public IMessage onMessage(PacketInvokStarting message, MessageContext ctx) {
			SFArtifacts.myProxy.getThreadFromContext(ctx).addScheduledTask(new Runnable(){
				@Override
				public void run() {
					ctx.getServerHandler().playerEntity.closeScreen();
					HammerHelper.startInvoking(ctx.getServerHandler().playerEntity.worldObj,ctx.getServerHandler().playerEntity,new BlockPos( message.x, message.y, message.z));
				}}
			);
			return null;
		}
		
		@Override
		public void fromBytes(ByteBuf buf) {
		    x = buf.readInt();
		    y = buf.readInt();
		    z = buf.readInt();
			
		}
		@Override
		public void toBytes(ByteBuf buf) {
		    buf.writeInt(x);
		    buf.writeInt(y);
		    buf.writeInt(z);
			
		}

	}
