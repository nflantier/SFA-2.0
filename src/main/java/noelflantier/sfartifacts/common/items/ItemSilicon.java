package noelflantier.sfartifacts.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class ItemSilicon extends ItemSFA{

	public static int[] colorPrct = new int[]{16777215, 14606046, 12434877, 10921638, 9737364 };
	
    public ItemSilicon(){
		super();
		this.setHasSubtypes(true);
		setUnlocalizedName(Ressources.UL_NAME_SILICON);
		setRegistryName(Ressources.UL_NAME_SILICON);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack itemstack){
    	int i = itemstack.getItemDamage();
    	if (i < 0 || i >= Ressources.UL_NAME_TYPE_SILICON.length){
        	i = 0;
        }
        return super.getUnlocalizedName()+"."+Ressources.UL_NAME_TYPE_SILICON[i];
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs creaT, List list){
    	list.add(new ItemStack(item, 1, 0));
    	list.add(new ItemStack(item, 1, 1));
    	list.add(new ItemStack(item, 1, 2));
	}

	@Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isequiped) {
    	if(stack.getItemDamage()==2 && !world.isRemote){
    		long age = ItemNBTHelper.getLong(stack, "agesilicon", -1);
    		ItemNBTHelper.setLong(stack, "worldtime", world.getTotalWorldTime());
    		if(age<=-1){
        		ItemNBTHelper.setLong(stack, "agesilicon", world.getTotalWorldTime()+ModConfig.pureSiliconLifeSpan);
        		return;
    		}
    		if(age-world.getTotalWorldTime()<=0){
    			if(entity instanceof EntityPlayer){
    				((EntityPlayer)entity).inventory.setInventorySlotContents(slot, new ItemStack(ModItems.itemSilicon,stack.stackSize,1));
    			}
    			return;
    		}
    	}
    }
	
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
    	if(stack.getItemDamage()==2 && !world.isRemote){
    		ItemNBTHelper.setLong(stack, "agesilicon", world.getTotalWorldTime()+ModConfig.pureSiliconLifeSpan);
    	}
	}

	/*@Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int p_82790_2_){
		if(stack.getItemDamage()==2){
    		long age = ItemNBTHelper.getLong(stack, "agesilicon", -1);
    		if(age>-1){
    			float prct = (float)Math.round( ((1 - (float)((float)age-(float)ItemNBTHelper.getLong(stack, "worldtime", age)) / (float)ModConfig.pureSiliconLifeSpan) * 10)) / 10;
    			int c = Math.round(colorPrct.length*prct);
    			if(c%2==0)
    				c=0;
    			else
    				c=1;
    	        return c<colorPrct.length?colorPrct[c]:14737632;
    		}
	        return 14737632;
		}
        return 16777215;
    }*/

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(stack.getItemDamage()==2){
			list.add("Decay : "+(ItemNBTHelper.getLong(stack, "agesilicon", -1)-ItemNBTHelper.getLong(stack, "worldtime",-1)));
		}
	}
	
	@Override
    public int getMetadata(int meta){
		return meta;
    }
    
}
