package noelflantier.sfartifacts.common.items;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import cofh.api.energy.IEnergyConnection;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.IBlockHasHammerInteractions;
import noelflantier.sfartifacts.common.helpers.HammerHelper;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;
import noelflantier.sfartifacts.common.helpers.SoundHelper;
import noelflantier.sfartifacts.common.items.baseclasses.IItemHasModes;
import noelflantier.sfartifacts.common.items.baseclasses.ItemMode;
import noelflantier.sfartifacts.common.network.PacketHandler;
import noelflantier.sfartifacts.common.network.messages.PacketEvent;
import noelflantier.sfartifacts.common.tileentities.ITileCanBeMaster;
import noelflantier.sfartifacts.common.tileentities.ITileCanHavePillar;
import noelflantier.sfartifacts.common.tileentities.ITileMustHaveMaster;
import noelflantier.sfartifacts.common.tileentities.TileInductor;
import noelflantier.sfartifacts.compatibilities.IC2Handler;
import noelflantier.sfartifacts.compatibilities.InterMods;

public class ItemBasicHammer extends ItemSFA implements IItemHasModes{

	public List<ItemMode> modes = new ArrayList<>();
	public ItemBasicHammer() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(Ressources.UL_NAME_BASIC_HAMMER);
		setRegistryName(Ressources.UL_NAME_BASIC_HAMMER);
		addMode("Basic", "Basic", "To associate structures");
		addMode("Construction", "Construction", "To see pillars models");
		addMode("Forge", "Forge", "To forge uprgades");
	}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D(){
        return true;
    }

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){   
		if(worldIn.isRemote)
			return EnumActionResult.SUCCESS;
		
		TileEntity t = worldIn.getTileEntity(pos);
		int mode = ItemNBTHelper.getInteger(stack, "Mode", 0);
		if(mode!=0)
			return EnumActionResult.FAIL;
		//if(t!=null)
		//Utils.getAllSuperclasses(t.getClass()).forEach((c)->{for(int i = 0 ; i < c.getInterfaces().length;i++){System.out.println(c.getInterfaces()[i]);}});
		
		if(t!=null && t instanceof ITileMustHaveMaster){
			PacketHandler.sendToTargetPoint(new PacketEvent(pos, 1000, 0),worldIn, pos);
			if(t instanceof TileInductor){
				if(ItemNBTHelper.getBoolean(stack, "hasmaster", false) 
				&& !(t.getPos().getX()==ItemNBTHelper.getInteger(stack, "mx", -1) && t.getPos().getY()==ItemNBTHelper.getInteger(stack, "my", -1)
				&& t.getPos().getZ()==ItemNBTHelper.getInteger(stack, "mz", -1))){
					ItemNBTHelper.setInteger(stack, "cx", t.getPos().getX());
					ItemNBTHelper.setInteger(stack, "cy", t.getPos().getY());
					ItemNBTHelper.setInteger(stack, "cz", t.getPos().getZ());
	
					ItemNBTHelper.setBoolean(stack, "haschild", true);
				}else{
					ItemNBTHelper.setInteger(stack, "mx", t.getPos().getX());
					ItemNBTHelper.setInteger(stack, "my", t.getPos().getY());
					ItemNBTHelper.setInteger(stack, "mz", t.getPos().getZ());
	
					ItemNBTHelper.setBoolean(stack, "hasmaster", true);
					playerIn.addChatComponentMessage(new TextComponentString("Master inductor at "+ItemNBTHelper.getInteger(stack, "mx", -1)+" "+
													ItemNBTHelper.getInteger(stack, "my", -1)+" "+ItemNBTHelper.getInteger(stack, "mz", -1)));
				}
			}else{
				ITileMustHaveMaster tcbm = (ITileMustHaveMaster)t;
				if(tcbm.hasMaster()){
					ItemNBTHelper.setInteger(stack, "mx", tcbm.getMasterX());
					ItemNBTHelper.setInteger(stack, "my", tcbm.getMasterY());
					ItemNBTHelper.setInteger(stack, "mz", tcbm.getMasterZ());
	
					ItemNBTHelper.setBoolean(stack, "hasmaster", true);
					playerIn.addChatComponentMessage(new TextComponentString("Master at "+ItemNBTHelper.getInteger(stack, "mx", -1)+" "+
													ItemNBTHelper.getInteger(stack, "my", -1)+" "+ItemNBTHelper.getInteger(stack, "mz", -1)));
				}
			}
		}else if(t!=null && ( t instanceof ITileCanHavePillar || t instanceof IEnergyConnection  || (InterMods.hasIc2 && IC2Handler.isEnergyStorage(t)))){
			PacketHandler.sendToTargetPoint(new PacketEvent(pos, 1000, 0),worldIn, pos);
			ItemNBTHelper.setInteger(stack, "cx", t.getPos().getX());
			ItemNBTHelper.setInteger(stack, "cy", t.getPos().getY());
			ItemNBTHelper.setInteger(stack, "cz", t.getPos().getZ());

			ItemNBTHelper.setBoolean(stack, "haschild", true);
			playerIn.addChatComponentMessage(new TextComponentString("Machine at "+ItemNBTHelper.getInteger(stack, "cx", -1)+" "+
											ItemNBTHelper.getInteger(stack, "cy", -1)+" "+ItemNBTHelper.getInteger(stack, "cz", -1)));
		}else if(worldIn.getBlockState(pos).getBlock() instanceof IBlockHasHammerInteractions == false){
			ItemNBTHelper.setBoolean(stack, "haschild", false);
			ItemNBTHelper.setBoolean(stack, "hasmaster", false);
			playerIn.addChatComponentMessage(new TextComponentString("Hammer reset"));
		}
		
		if(ItemNBTHelper.getBoolean(stack, "haschild", false) && ItemNBTHelper.getBoolean(stack, "hasmaster", false)){

			BlockPos postm = new BlockPos(ItemNBTHelper.getInteger(stack, "mx", -1), 
					ItemNBTHelper.getInteger(stack, "my", -1), ItemNBTHelper.getInteger(stack, "mz", -1));
			BlockPos postc = new BlockPos(ItemNBTHelper.getInteger(stack, "cx", -1), 
					ItemNBTHelper.getInteger(stack, "cy", -1), ItemNBTHelper.getInteger(stack, "cz", -1));
			
			TileEntity tm = worldIn.getTileEntity(postm);
			TileEntity tc = worldIn.getTileEntity(postc);
			ITileCanBeMaster tcbm = (ITileCanBeMaster)tm;
			
			if(tm instanceof TileInductor && tc instanceof TileInductor){
				ITileCanBeMaster tcbm2 = (ITileCanBeMaster)tc;
				if(tcbm.getChildsList().stream().anyMatch((c)->c.equals(postc))){
					tcbm.getChildsList().removeIf((c)->c.equals(postc));
					playerIn.addChatComponentMessage(new TextComponentString("Inductor removed"));
				}else{
					tcbm.getChildsList().add(postc);
					playerIn.addChatComponentMessage(new TextComponentString("Inductor connected"));
				}
				ItemNBTHelper.setBoolean(stack, "haschild", false);
				ItemNBTHelper.setBoolean(stack, "hasmaster", false);
			}else if(tm!=null && tcbm!=null && tcbm.getChildsList()!=null){
				if(tcbm.getChildsList().stream().anyMatch((c)->c.equals(postc))){
					tcbm.getChildsList().removeIf((c)->c.equals(postc));
					if(tc instanceof ITileCanHavePillar){
						((ITileCanHavePillar)tc).setMaster(null);
						worldIn.notifyBlockUpdate(postc, worldIn.getBlockState(postc), worldIn.getBlockState(postc), 3);
					}
					playerIn.addChatComponentMessage(new TextComponentString("Machine allready connected to that pillar. It has been removed."));
				}else{
					tcbm.getChildsList().add(postc);
					if(tc instanceof ITileCanHavePillar){
						((ITileCanHavePillar)tc).setMaster(postm);
						worldIn.notifyBlockUpdate(postc, worldIn.getBlockState(postc), worldIn.getBlockState(postc), 3);
					}
					playerIn.addChatComponentMessage(new TextComponentString("Machine connected!"));
				}
			}else if(tm==null){
				ItemNBTHelper.setBoolean(stack, "hasmaster", false);
			}
			
			if(ItemNBTHelper.getBoolean(stack, "haschild", false) && ItemNBTHelper.getBoolean(stack, "hasmaster", false)){
				ItemNBTHelper.setBoolean(stack, "hasmaster", false);
				ItemNBTHelper.setBoolean(stack, "haschild", false);
			}
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.FAIL;
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
		return "Basic Hammer mode : ";
	}
	
	@Override
	public List<ItemMode> getModes() {
		return this.modes;
	}

	@Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
		if(worldIn.isRemote || (this.shouldSneak() && !playerIn.isSneaking()) )return new ActionResult(EnumActionResult.PASS, itemStackIn);
		RayTraceResult mo = HammerHelper.rayTraceLightning(playerIn,this.getDistanceRay());
        if (mo == null){
        	changeMode(itemStackIn, playerIn);
        }
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
	/*public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer player){
		if(w.isRemote || (this.shouldSneak() && !player.isSneaking()) )return stack;
		
		MovingObjectPosition mo = HammerHelper.rayTraceLightning(player,this.getDistanceRay());
        if (mo == null){
        	changeMode(stack, player);
        }

	   return stack;
	}*/	

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack it = new ItemStack(item, 1, 0);
		it = ItemNBTHelper.setInteger(it, "Mode", 0);
		list.add(it);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		int m = ItemNBTHelper.getInteger(stack, "Mode", 0);
		list.add("Mode : "+getModes().get(m).name);
		list.add(getModes().get(m).description);
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			list.add("Right click any vanilla block to reset your hammer");
			list.add("Shift right click in the air to change modes");
		}else{
			list.add(TextFormatting.WHITE + "" + TextFormatting.ITALIC +"<Hold Shift>");
		}
	}
}
