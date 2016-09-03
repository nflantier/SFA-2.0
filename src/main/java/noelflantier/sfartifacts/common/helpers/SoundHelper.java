package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noelflantier.sfartifacts.Ressources;

public class SoundHelper {	
	public static SoundEvent RECORD_BASIC_HAMMER;

	public static void registerSounds() {
		RECORD_BASIC_HAMMER = registerSound("record.basicHammer");
	}

	private static SoundEvent registerSound(String soundName) {
		final ResourceLocation soundID = new ResourceLocation(Ressources.MODID, soundName);
		return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
	}
	
	public static void playEventSFA(EntityPlayer player, int type, BlockPos blockPosIn, int data){
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				switch (type)
		        {
		        	case 1000:
		    			Minecraft.getMinecraft().theWorld.playSound(blockPosIn, RECORD_BASIC_HAMMER, SoundCategory.AMBIENT, 0.2F, Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
		    			break;
		        	case 1001:
		    			Minecraft.getMinecraft().theWorld.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 0.2F, Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
		    			break;
		        	case 1002:
		    			Minecraft.getMinecraft().theWorld.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 0.2F, Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
		    			break;
	    			case 2001:
	                    Block block = Block.getBlockById(data & 4095);

	                    if (block.getDefaultState().getMaterial() != Material.AIR)
	                    {
	                        SoundType soundtype = block.getSoundType();
	                        Minecraft.getMinecraft().theWorld.playSound(blockPosIn, soundtype.getBreakSound(), SoundCategory.BLOCKS, 0.2F, soundtype.getPitch() * 0.8F, true);
	                    }

	                    Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(data >> 12 & 255));
	                    break;
		        }
			}
		});
	}
	
	public static void playBlockSound(BlockPos blockPosIn, Block block){
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {

                 if (block.getDefaultState().getMaterial() != Material.AIR)
                 {
                     SoundType soundtype = block.getSoundType();
                     Minecraft.getMinecraft().theWorld.playSound(blockPosIn, soundtype.getBreakSound(), SoundCategory.BLOCKS, 0.2F, soundtype.getPitch() * 0.8F, true);
                 }

                 Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(0 >> 12 & 255));
                 
			}
		});
	}
}
