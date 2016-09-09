package noelflantier.sfartifacts.common.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.ItemThorHammer;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketHammerConfig;

public class ModKeyInput {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if(ModKeyBindings.hammerConfig.isPressed()) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if(player.getHeldItemMainhand()!=null && player.getHeldItemMainhand().getItem() instanceof ItemThorHammer && ItemNBTHelper.getBoolean(player.getHeldItemMainhand(), "CanBeConfigByHand", false)){
				PacketHandler.INSTANCE.sendToServer(new PacketHammerConfig(ModGUIs.guiIDHammerConfig));
				player.openGui(SFArtifacts.instance, ModGUIs.guiIDHammerConfig, Minecraft.getMinecraft().theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
			}/*else if(player.getHeldItemOffhand()!=null && player.getHeldItemOffhand().getItem() instanceof ItemThorHammer && ItemNBTHelper.getBoolean(player.getHeldItemOffhand(), "CanBeConfigByHand", false)){
				PacketHandler.INSTANCE.sendToServer(new PacketHammerConfig(ModGUIs.guiIDHammerConfig));
				player.openGui(SFArtifacts.instance, ModGUIs.guiIDHammerConfig, Minecraft.getMinecraft().theWorld, (int)player.posX, (int)player.posY, (int)player.posZ);
			}*/
		}/*else if(Minecraft.getMinecraft().gameSettings.keyBindSwapHands.isPressed()){
			
		}*/
	}
}
