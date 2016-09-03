package noelflantier.sfartifacts.common.items;

import java.util.List;
import java.util.Map;

import com.google.common.collect.MapMaker;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.entities.EntityHoverBoard;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.ShieldHelper;

public class ItemHoverBoard extends ItemSFA implements IEnergyContainerItem{

	private static int[] capacity = new int[]{ModConfig.capacityHoverBoardMarty,ModConfig.capacityHoverBoardBiff};
	private static int[] maxReceive = new int[]{ModConfig.capacityHoverBoardMarty,ModConfig.capacityHoverBoardBiff};
	private static int[] maxExtract = new int[]{ModConfig.capacityHoverBoardMarty,ModConfig.capacityHoverBoardBiff};
	public static int[] rfPerSecongHoverboard = new int[]  {ModConfig.rfPerSecondHoverBoardMarty, ModConfig.rfPerSecondHoverBoardBiff};
	public static final int MATTEL_HOVERBOARD = 0;
	public static final int PITBULL_HOVERBOARD = 1;
	
	private static Map<EntityPlayer, EntityHoverBoard> spawnedHoverBoardsMap = new MapMaker().weakKeys().weakValues().makeMap();

	public ItemHoverBoard() {
		super();
		setUnlocalizedName(Ressources.UL_NAME_HOVERBOARD);
		setRegistryName(Ressources.UL_NAME_HOVERBOARD);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	@Override
    public int getMetadata(int meta){
		return meta;
    }
	
	@Override
    public String getUnlocalizedName(ItemStack itemstack){
        return super.getUnlocalizedName()+"."+Ressources.UL_NAME_TYPE_HOVERBOARD[itemstack.getItemDamage()>>1];
    }
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(hand.equals(EnumHand.OFF_HAND))
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		
		if (!worldIn.isRemote && playerIn != null) {
			EntityHoverBoard hoverboard = spawnedHoverBoardsMap.get(playerIn);
			if (hoverboard != null){
				if(hoverboard.getTypeHoverBoard()==itemStackIn.getItemDamage()>>1 && hoverboard.getSlot()==playerIn.inventory.currentItem)
					despawnHoverBoard(playerIn, hoverboard);
			}else {
				if(this.getEnergyStored(itemStackIn)>0){
					spawnHoverBoard(playerIn,itemStackIn);
				}
			}
		}
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }
	
	
	private static void despawnHoverBoard(EntityPlayer player, EntityHoverBoard hoverboard) {
		hoverboard.setDead();
		spawnedHoverBoardsMap.remove(player);
	}

	private static void spawnHoverBoard(EntityPlayer player, ItemStack stack) {
		EntityHoverBoard hoverboard = new EntityHoverBoard(player.worldObj, player);
		hoverboard.setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationPitch, player.rotationYaw);
		player.worldObj.spawnEntityInWorld(hoverboard);
		spawnedHoverBoardsMap.put(player, hoverboard);
	}
	
	public int getCapacity(ItemStack stack){ return capacity[stack.getItemDamage()>>1]+ItemNBTHelper.getInteger(stack, "AddedCapacityLevel", 0)*ModConfig.rfAddedPerCapacityUpgradeOnHoverboards; }
	public int getMaxExtract(ItemStack stack){ return maxExtract[stack.getItemDamage()>>1]; }
	public int getMaxReceive(ItemStack stack){ return maxReceive[stack.getItemDamage()>>1]; }
	
	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
		if (player != null && !player.worldObj.isRemote) {
			EntityHoverBoard hoverboard = spawnedHoverBoardsMap.get(player);
			if (hoverboard != null){
				if(hoverboard.getTypeHoverBoard()==stack.getItemDamage()>>1 && hoverboard.getSlot()==player.inventory.currentItem)
					despawnHoverBoard(player, hoverboard);
			}
		}
        return true;
    }
	
	@Override
	public int receiveEnergy(ItemStack stack, int maxReceive, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(stack, "Energy", 0);
		int energyReceived = Math.min(getCapacity(stack) - energy, Math.min(getMaxReceive(stack), maxReceive));

		if (!simulate) {
			energy += energyReceived;
			ItemNBTHelper.setInteger(stack, "Energy", energy);
		}
		return energyReceived;
	}
	
	@Override
	public int extractEnergy(ItemStack stack, int maxExtract, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(stack, "Energy", 0);
		int energyExtracted = Math.min(energy, Math.min(getMaxExtract(stack), maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			ItemNBTHelper.setInteger(stack, "Energy", energy);
		}
		return energyExtracted;
	}
	
	@Override
	public int getEnergyStored(ItemStack stack) {
		return ItemNBTHelper.getInteger(stack, "Energy", 0);
	}
	
	@Override
	public int getMaxEnergyStored(ItemStack stack) {
		return getCapacity(stack);
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !(getEnergyStored(stack) > getMaxEnergyStored(stack));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ((double)getEnergyStored(stack) / (double)getMaxEnergyStored(stack));
	}
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i<Ressources.UL_NAME_TYPE_HOVERBOARD.length;i++){
			ItemStack it = new ItemStack(item, 1, i+1*i);
			it = ItemNBTHelper.setInteger(it, "AddedCapacityLevel", 0);
			list.add(ItemNBTHelper.setInteger(it, "Energy", 0));
			if (capacity[i] > 0){
				ItemStack it2 = new ItemStack(item, 1, i+1*i+1);
				it2 = ItemNBTHelper.setInteger(it2, "AddedCapacityLevel", 0);
				list.add(ItemNBTHelper.setInteger(it2, "Energy", capacity[i]));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(String.format("%s/%s RF", ItemNBTHelper.getInteger(stack, "Energy", 0), this.getCapacity(stack)));
	}
}