package noelflantier.sfartifacts.common.items.baseclasses;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.items.ItemSFA;

public class ItemParticle extends ItemSFA{
	
	public ItemParticle() {
		super();
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return false;
    }
    
    @Override
	public boolean hasCustomEntity(ItemStack stack){
    	return true;
	}
    
    /*@Override
    public Entity createEntity(World world, Entity location, ItemStack stack)
    {
    	EntityItem eqi = new EntityItemParticle(world, location, stack);
		eqi.motionX = location.motionX;
		eqi.motionY = location.motionY;
		eqi.motionZ = location.motionZ;
		eqi.delayBeforeCanPickup = ( (EntityItem) location ).delayBeforeCanPickup;
        return eqi;
    }*/
}
