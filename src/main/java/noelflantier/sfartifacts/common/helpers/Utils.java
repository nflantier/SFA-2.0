package noelflantier.sfartifacts.common.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.IFluidBlock;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class Utils {
	
	public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
        if (cls == null) {
            return null;
        }
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        Class<?> superclass = cls.getSuperclass();
        while (superclass != null) {
            classes.add(superclass);
            superclass = superclass.getSuperclass();
        }
        return classes;
    }
	
	public static String getFileFromConfig(String fileName, boolean replaceIfExists) throws IOException {    
		if(replaceIfExists || !getConfigFile(fileName).exists()) {
			IOUtils.copy(Utils.class.getResourceAsStream("/assets/sfartifacts/config/" + fileName), new FileOutputStream(getConfigFile(fileName)));
		}            
		return IOUtils.toString(new FileReader(getConfigFile(fileName)));    
	}
	public static File getConfigFile(String name) {
		return new File(ModConfig.configDirectory, name);
	}
	
	public static boolean isInventoryHasItemClass(Class<? extends Item> cla, IInventory bi){
		int size = bi.getSizeInventory();
		for(int i = 0 ; i < size ; i++){
			if(bi.getStackInSlot(i)!=null && bi.getStackInSlot(i).getItem()!=null && bi.getStackInSlot(i).getItem().getClass()==cla)
				return true;
		}
		return false;
	}
	
	public static boolean isPlayerHoverFuid(EntityPlayer player, double decx, double decy, double decz){
        int i = MathHelper.floor_double(player.posX+decx);
        int j = player.worldObj.isRemote?MathHelper.floor_double(player.posY-1+decy):MathHelper.floor_double(player.posY+decy);
        int k = MathHelper.floor_double(player.posZ+decz);
        IBlockState blockstate = player.worldObj.getBlockState(new BlockPos(i,j,k));
        if(blockstate!=null && blockstate.getMaterial() != null && blockstate.getMaterial().isLiquid())
        	return true;
        return false;
	}
	
	public static boolean handleEntityInMaterial(Entity entity, boolean hulk){
		int x = MathHelper.floor_double(entity.posX);
        int y = entity.getEntityWorld().isRemote ? MathHelper.floor_double(entity.posY):MathHelper.floor_double(entity.posY);
        int z = MathHelper.floor_double(entity.posZ);
        BlockPos pos = new BlockPos(x,y,z);
        IBlockState blockstate = entity.getEntityWorld().getBlockState(pos);
        if(blockstate!=null && blockstate.getMaterial() != null && blockstate.getMaterial().isLiquid())
        	return hulk ? handleMaterialOnHulkFlesh(entity, blockstate, pos) : entity.getEntityWorld().handleMaterialAcceleration(entity.getEntityBoundingBox().expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), blockstate.getMaterial(), entity);
        return false;
	}
	
	public static boolean handleMaterialOnHulkFlesh(Entity entity, IBlockState state, BlockPos pos){
		if( !(entity instanceof EntityLivingBase))
			return false;
        entity.moveRelative(((EntityLivingBase)entity).moveStrafing, ((EntityLivingBase)entity).moveForward, state.getMaterial() == Material.WATER ? 0.1F : 0.2F);
        entity.moveEntity(entity.motionX, entity.motionY, entity.motionZ);
        //entity.motionY = 0.42F;
		return true;
	}
	
}
