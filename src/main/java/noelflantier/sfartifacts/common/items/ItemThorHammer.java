package noelflantier.sfartifacts.common.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.MapMaker;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModKeyBindings;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.items.baseclasses.IItemHasModes;
import noelflantier.sfartifacts.common.items.baseclasses.ItemHammerTool;
import noelflantier.sfartifacts.common.items.baseclasses.ItemMode;

public class ItemThorHammer extends ItemHammerTool implements IItemHasModes{

	public static ToolMaterial HAMMER_3 = EnumHelper.addToolMaterial("SFA_HAMMER", 10, -1, 44F, 30.0F, -1);
	public List<ItemMode> modes = new ArrayList<>();
	
	public static int energyMining = ModConfig.rfMining;
	public static int energyLightning = ModConfig.rfLightning;
	public static int energyMoving = ModConfig.rfMoving;
	public static int energyTeleporting = ModConfig.rfTeleporting;
	public static int energyEntityHit = ModConfig.rfEntityHit;
	
	public ItemThorHammer() {
		super(HAMMER_3);
		setUnlocalizedName(Ressources.UL_NAME_THOR_HAMMER);
		setRegistryName(Ressources.UL_NAME_THOR_HAMMER);
		setCapacity(ModConfig.rfThorhammer);
		setMaxExtract(ModConfig.rfThorhammer);
		setMaxReceive(5000);
		setHarvestLevel("pickaxe", 100);
		setHarvestLevel("shovel", 100);
		setHarvestLevel("axe", 100);
		addMode("Mining", "Mining", "To mine");
		addMode("CanThrowLightning", "Lightning", "To throw lightnings");
		addMode("CanThrowToMove", "Moving", "To move with the hammer");
		addMode("CanTeleport", "Teleport", "To teleport");
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.NONE;
    }
	
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player){
		int radius = ItemNBTHelper.getInteger(itemstack, "Radius", 0);
		int depth = 0;
		boolean mine = false;
		mine = HammerHelper.breakOnMinning(itemstack, pos, radius, depth, player);
		return mine ? true : super.onBlockStartBreak(itemstack, pos, player);
    }
	
	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase el, EntityLivingBase el2){
        this.extractEnergy(stack, energyEntityHit, false);
        return true;
    }

	public boolean isEnchantValid(Enchantment enchant){
		return enchant.type == EnumEnchantmentType.DIGGER || enchant.type == EnumEnchantmentType.WEAPON;	
	}
	
	@Override
	public void changeMode(ItemStack stack, EntityPlayer player){
		int m = ItemNBTHelper.getInteger(stack, "Mode", 0);
		int nme = m;
		for(int i = nme;i<=getModes().size()-1;i++){
			if(i!=m && ItemNBTHelper.getBoolean(stack, getModes().get(i).nameNBT, false)){
				nme = i;
				break;
			}
			if(i==getModes().size()-1){
				nme = 0;
				break;
			}
		}
		player.addChatComponentMessage(new TextComponentString(this.getStringChat()+""+getModes().get(nme).name));
		ItemNBTHelper.setInteger(stack, "Mode", nme);
		
	}
	
	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(hand.equals(EnumHand.OFF_HAND))
			return new ActionResult(EnumActionResult.FAIL, itemStackIn);
		
    	if (!ItemNBTHelper.getBoolean(itemStackIn, "IsThrown", false)){
			if(playerIn.isSneaking()){
				if(!worldIn.isRemote)changeMode(itemStackIn, playerIn);
				return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
			}
			int m = ItemNBTHelper.getInteger(itemStackIn, "Mode", 0);
			
			if(!worldIn.isRemote && m==0 && this.getEnergyStored(itemStackIn)>=this.energyMining){
				ItemNBTHelper.setBoolean(itemStackIn, "IsThrown", true);
				itemStackIn.setItemDamage(2);
				HammerHelper.startMiningSequence(worldIn, playerIn, itemStackIn);
		        
			}
			if(!worldIn.isRemote && m==1 && this.getEnergyStored(itemStackIn)>=this.energyLightning){
				HammerHelper.startLightning(worldIn, itemStackIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn);
			}
			if(m==2 && this.getEnergyStored(itemStackIn)>=this.energyMoving ){
				ItemNBTHelper.setBoolean(itemStackIn, "IsMoving", true);
				HammerHelper.startMoving(worldIn, playerIn, itemStackIn);
			}
			if(m==3){
				HammerHelper.startGuiTeleport(worldIn, playerIn);
			}
	    }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if(entity instanceof EntityItem == false)
			entity.attackEntityFrom(DamageSource.causePlayerDamage(player), getToolMaterial().getDamageVsEntity());
		return true;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, EntityPlayer player){
        return !ItemNBTHelper.getBoolean(stack, "IsThrown", false);
    }
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {

		ItemStack it = new ItemStack(item, 1, 0);
		it = ItemNBTHelper.setInteger(it, "Energy", 0);
		it = ItemNBTHelper.setInteger(it, "Radius", 0);
		it = ItemNBTHelper.setBoolean(it, "CanMagnet", false);
		it = ItemNBTHelper.setBoolean(it, "IsMagnetOn", false);
		it = ItemNBTHelper.setBoolean(it, "IsMoving", false);
		it = ItemNBTHelper.setBoolean(it, "IsThrown", false);
		it = ItemNBTHelper.setBoolean(it, "CanThrowLightning", false);
		it = ItemNBTHelper.setBoolean(it, "CanThrowToMove", false);
		it = ItemNBTHelper.setBoolean(it, "CanBeConfigByHand", false);
		it = ItemNBTHelper.setBoolean(it, "CanTeleport", false);
		it = ItemNBTHelper.setInteger(it, "Mode", 0);//0 throwing hammer to mine   1 ligthning weapon    2 throwing hammer to fly    3 teleport
		it = ItemNBTHelper.setTagList(it,"EnchStored", new NBTTagList());
		list.add(it);
		
		if (this.getClassCapacity() > 0){
			ItemStack it2 = new ItemStack(item, 1, 1);
			it2 = ItemNBTHelper.setInteger(it2, "Energy", this.getClassCapacity());
			it2 = ItemNBTHelper.setInteger(it2, "Radius", 0);
			it2 = ItemNBTHelper.setBoolean(it2, "CanMagnet", true);
			it2 = ItemNBTHelper.setBoolean(it2, "IsMagnetOn", true);
			it2 = ItemNBTHelper.setBoolean(it2, "IsMoving", false);
			it2 = ItemNBTHelper.setBoolean(it2, "IsThrown", false);
			it2 = ItemNBTHelper.setBoolean(it2, "CanThrowLightning", true);
			it2 = ItemNBTHelper.setBoolean(it2, "CanThrowToMove", true);
			it2 = ItemNBTHelper.setBoolean(it2, "CanBeConfigByHand", true);
			it2 = ItemNBTHelper.setBoolean(it2, "CanTeleport", true);
			it2 = ItemNBTHelper.setInteger(it2, "Mode", 0);
			it2 = ItemNBTHelper.setTagList(it2,"EnchStored", new NBTTagList());
			list.add(it2);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		super.addInformation(stack, player, list, par4);
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			
			list.add(String.format("Radius %s",ItemNBTHelper.getInteger(stack, "Radius", 0)));
			
			if(ItemNBTHelper.getBoolean(stack, "CanMagnet", false)){
				if(ItemNBTHelper.getBoolean(stack, "IsMagnetOn", false))
					list.add(String.format("Magnet ON"));
				else
					list.add(String.format("Magnet OFF"));
			}else{
				list.add(String.format("Magnet not installed"));
			}	

			if(ItemNBTHelper.getBoolean(stack, "CanThrowToMove", false)){
				list.add(String.format("You can travel faster with your hammer"));
			}else{
				list.add(String.format("You can't throw your hammer to travel faster"));
			}		
			
			if(ItemNBTHelper.getBoolean(stack, "CanThrowLightning", false)){
				list.add(String.format("Lightning weapon installed"));
			}else{
				list.add(String.format("Lightning weapon not installed"));
			}
			if(ItemNBTHelper.getBoolean(stack, "CanTeleport", false)){
				list.add(String.format("Teleport installed"));	
			}else{
				list.add(String.format("Teleport not installed"));	
			}
			if(ItemNBTHelper.getBoolean(stack, "CanBeConfigByHand", false)){
				list.add(String.format("Configurable by hand press %s%s%s",TextFormatting.WHITE, Keyboard.getKeyName(ModKeyBindings.hammerConfig.getKeyCode()), TextFormatting.RESET));
			}else{
				list.add(String.format("Cannot be configured by hand"));
			}
			
		}else{
			list.add(TextFormatting.WHITE + "" + TextFormatting.ITALIC +"<Hold Shift>");
		}
	}

	@Override
	public List<ItemMode> getModes() {
		return this.modes;
	}

	@Override
	public double getDistanceRay() {
		return 5.0D;
	}

	@Override
	public boolean shouldSneak() {
		return true;
	}

	@Override
	public String getStringChat() {
		return "Hammer mode : ";
	}
}
