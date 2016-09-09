package noelflantier.sfartifacts.common.items.baseclasses;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class ItemHammerTool extends ItemTool implements IEnergyContainerItem{
	
	public float efficiencyOnNonProperMaterial = 10.0F;
	
	private static final Set<Block> EFFECTIVE_ON_PICK = Sets.newHashSet(new Block[] {Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE});
	private static final Set<Block> EFFECTIVE_ON_AXE = Sets.newHashSet(new Block[] {Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE});
	private static final Set<Block> EFFECTIVE_ON_SHOVEL = Sets.newHashSet(new Block[] {Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH});
	private static final Set<Material> EFFECTIVE_ON_MATERIAL = Sets.newHashSet(Material.IRON, Material.ANVIL, Material.ROCK, Material.GLASS, Material.ICE, Material.PACKED_ICE,Material.WOOD, Material.LEAVES, Material.CORAL, Material.CACTUS, Material.PLANTS, Material.VINE);
	
	private static final Set<Block> EFFECTIVE_Gl = new HashSet(){{
		addAll(EFFECTIVE_ON_PICK);	
		addAll(EFFECTIVE_ON_AXE);	
		addAll(EFFECTIVE_ON_SHOVEL);	
	}};

	private int capacity = 0;
	private int maxReceive = 0;
	private int maxExtract = 0;
	
	/*private static final Set SHOVEL_OVERRIDES = Sets.newHashSet(Blocks.GRASS, Blocks.DIRT, Blocks.SAND, Blocks.GRAVEL, Blocks.SNOW_LAYER, 
    		Blocks.SNOW, Blocks.CLAY, Blocks.FARMLAND, Blocks.SOUL_SAND, Blocks.MYCELIUM);
	private static final Set PICKAXE_OVERRIDES = Sets.newHashSet(Blocks.COBBLESTONE, Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB, Blocks.STONE, Blocks.SANDSTONE, 
			Blocks.MOSSY_COBBLESTONE, Blocks.IRON_ORE, Blocks.IRON_BLOCK, Blocks.COAL_ORE, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK, 
			Blocks.ICE, Blocks.NETHERRACK, Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK, Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.RAIL, Blocks.DETECTOR_RAIL, 
			Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL, Material.IRON, Material.ANVIL, Material.ROCK, Material.GLASS, Material.ICE, Material.PACKED_ICE);
	private static final Set AXE_OVERRIDES = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, 
			Material.WOOD, Material.LEAVES, Material.CORAL, Material.CACTUS, Material.PLANTS, Material.VINE);
	*/
	public ItemHammerTool(ToolMaterial material){
		super(material, EFFECTIVE_Gl);
		setCreativeTab(SFArtifacts.sfTabs);
		this.setMaxStackSize(1);
        this.setHasSubtypes(true);
	}

	/*
	 * 
	 * 
	 * TOOL
	 * 
	 * 
	 */
    
    @Override
	public float getStrVsBlock(ItemStack stack, IBlockState state){
    	float h = state.getBlockHardness(null, null);
    	float ns =  h * 2 > efficiencyOnProperMaterial ? h * 2 : efficiencyOnProperMaterial /*+ ( efficiencyOnProperMaterial - h ) * 0.05F*/;
        for (String type : getToolClasses(stack))
        {
            if (state.getBlock().isToolEffective(type, state))
                return ns;
        }
        return EFFECTIVE_Gl.contains(state.getBlock()) || EFFECTIVE_ON_MATERIAL.contains(state.getMaterial()) ? ns : efficiencyOnNonProperMaterial;
    }
    
    @Override
    public boolean canHarvestBlock(IBlockState state){
		return EFFECTIVE_Gl.contains(state.getBlock()) || EFFECTIVE_ON_MATERIAL.contains(state.getMaterial());
    }
    
    /*
     * 
     * 
     *  RF
     * 
     * 
     */
	
	public void setCapacity(int capacity){ this.capacity = capacity;}

	public void setMaxReceive(int maxReceive){ this.maxReceive = maxReceive;}

	public void setMaxExtract(int maxExtract){ this.maxExtract = maxExtract;}

	public int getCapacity(ItemStack stack){ 
		return capacity+ItemNBTHelper.getInteger(stack, "AddedCapacityLevel", 0)*ModConfig.rfAddedPerCapacityUpgrade; 
	}
	
	public int getClassCapacity(){ return this.capacity; }

	public int getMaxExtract(ItemStack stack){ return maxExtract; }

	public int getMaxReceive(ItemStack stack){ return maxReceive; }
	
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(container, "Energy", 0);
		int energyReceived = Math.min(getCapacity(container) - energy, Math.min(getMaxReceive(container), maxReceive));

		if (!simulate) {
			energy += energyReceived;
			ItemNBTHelper.setInteger(container, "Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		int energy = ItemNBTHelper.getInteger(container, "Energy", 0);
		int energyExtracted = Math.min(energy, Math.min(getMaxExtract(container), maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			ItemNBTHelper.setInteger(container, "Energy", energy);
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

		ItemStack it = new ItemStack(item, 1, 0);
		it = ItemNBTHelper.setInteger(it, "Energy", 0);
		it = ItemNBTHelper.setInteger(it, "AddedCapacityLevel", 0);
		list.add(it);
		
		if (capacity > 0){
			ItemStack it2 = new ItemStack(item, 1, 1);
			it2 = ItemNBTHelper.setInteger(it2, "Energy", capacity);
			it2 = ItemNBTHelper.setInteger(it2, "AddedCapacityLevel", 0);
			list.add(it2);
		}
	}
    
	@Override
	public boolean getHasSubtypes() {
		return capacity > 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(String.format("%s/%s RF", ItemNBTHelper.getInteger(stack, "Energy", 0), this.getCapacity(stack)));
	}
    
}
