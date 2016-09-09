package noelflantier.sfartifacts.common.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;

@SideOnly(Side.CLIENT)
public class ModEventsClient {
	@SubscribeEvent
	public void onRenderTickStart(TickEvent.RenderTickEvent evt) {
		if (evt.phase == Phase.START && Minecraft.getMinecraft().theWorld != null) {
			preRenderTick(Minecraft.getMinecraft(), Minecraft.getMinecraft().theWorld, evt.renderTickTime);
		}
	}

	public void preRenderTick(Minecraft mc, World world, float renderTick) {
		EntityHoverBoard.updateHoverboards(world);
	}
	
	/*@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre event){
		event.getMap().registerSprite(new ResourceLocation(Ressources.MODID,"particles/pt"));
	}*/
}
