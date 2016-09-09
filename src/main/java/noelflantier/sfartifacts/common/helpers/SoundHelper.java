package noelflantier.sfartifacts.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    @SideOnly(Side.CLIENT)
	public static void playEventSFA(int type, BlockPos blockPosIn, int data){
		IBlockState state = Minecraft.getMinecraft().theWorld.getBlockState(blockPosIn);
		Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {
				switch (type)
		        {
		        	case 1000:
		        		Minecraft.getMinecraft().theWorld.playSound(blockPosIn, RECORD_BASIC_HAMMER, SoundCategory.AMBIENT, 0.2F, Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
		        		Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(blockPosIn, state);     
		    			break;
		        	case 1001:
		    			Minecraft.getMinecraft().theWorld.playSound(blockPosIn, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 0.2F, Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
		    			break;
		        	case 1002:
		    			Minecraft.getMinecraft().theWorld.playSound(blockPosIn, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 0.2F, Minecraft.getMinecraft().theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
		    			break;
	    			case 2001:
	    				if (state.getBlock().getDefaultState().getMaterial() != Material.AIR){
	                        SoundType soundtype = state.getBlock().getSoundType(state, Minecraft.getMinecraft().theWorld, blockPosIn, null);
	                        Minecraft.getMinecraft().theWorld.playSound(blockPosIn, soundtype.getBreakSound(), SoundCategory.BLOCKS, 0.2F, soundtype.getPitch() * 0.8F, true);
		                    Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(blockPosIn, state); 
	    				}   
	                    break;
		        }
			}
		});
	}

    /*@SideOnly(Side.CLIENT)
	public static void playBlockSound(EntityPlayer player, World worldIn, IBlockState state, BlockPos blockPosIn, Block block){
    	Minecraft.getMinecraft().addScheduledTask(new Runnable(){
			@Override
			public void run() {

                 if (block.getDefaultState().getMaterial() != Material.AIR)
                 {
                     SoundType soundtype = block.getSoundType(state, worldIn, blockPosIn, player);
                     Minecraft.getMinecraft().theWorld.playSound(blockPosIn, soundtype.getBreakSound(), SoundCategory.BLOCKS, 0.2F, soundtype.getPitch() * 0.8F, true);
                 }

                 Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(blockPosIn, block.getStateFromMeta(0 >> 12 & 255));    
			}
		});
	}*/
}
