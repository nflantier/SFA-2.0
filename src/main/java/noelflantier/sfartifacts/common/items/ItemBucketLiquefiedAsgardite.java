package noelflantier.sfartifacts.common.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.SFArtifacts;
import noelflantier.sfartifacts.common.blocks.BlockMaterials;
import noelflantier.sfartifacts.common.handlers.ModFluids;
import noelflantier.sfartifacts.common.tileentities.pillar.TileBlockPillar;

public class ItemBucketLiquefiedAsgardite /*extends ItemBucket
{
    public ItemBucketLiquefiedAsgardite()
    {
    	super(ModFluids.blockLiquefiedAsgardite);
        this.setMaxStackSize(1);
        this.setCreativeTab(SFArtifacts.sfTabs);
		setUnlocalizedName(Ressources.UL_NAME_FILLED_BUCKET_ASGARDITE);
		setRegistryName(Ressources.UL_NAME_FILLED_BUCKET_ASGARDITE);
    }*/
    
    extends ItemFluidContainer{

	public ItemBucketLiquefiedAsgardite() {
		super(Fluid.BUCKET_VOLUME);
		setUnlocalizedName(Ressources.UL_NAME_FILLED_BUCKET_ASGARDITE);
		setRegistryName(Ressources.UL_NAME_FILLED_BUCKET_ASGARDITE);
		setCreativeTab(SFArtifacts.sfTabs);
		this.setMaxStackSize(1);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack it = new ItemStack(item, 1, 0);
		IFluidHandler fh = it.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
    	((FluidHandlerItemStack)fh).fill(new FluidStack(ModFluids.fluidLiquefiedAsgardite,Fluid.BUCKET_VOLUME),true);
		list.add(it);
	}
	
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand){
        float var4 = 1.0F;
        double trueX = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * (double) var4;
        double trueY = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * (double) var4 + 1.62D - (double) playerIn.getYOffset();
        double trueZ = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * (double) var4;
        boolean wannabeFull = false;
        RayTraceResult position = playerIn.rayTrace(4.5D, 0);

        if (position == null){
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
        else
        {

            if (position.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                int clickX = position.getBlockPos().getX();
                int clickY = position.getBlockPos().getY();
                int clickZ = position.getBlockPos().getZ();

                if (!worldIn.canMineBlockBody(playerIn,position.getBlockPos())){
                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
                }
                if (position.sideHit == EnumFacing.getFront(0)){
                    --clickY;
                }
                if (position.sideHit == EnumFacing.getFront(1)){
                    ++clickY;
                }
                if (position.sideHit == EnumFacing.getFront(2)){
                    --clickZ;
                }
                if (position.sideHit == EnumFacing.getFront(3)){
                    ++clickZ;
                }
                if (position.sideHit == EnumFacing.getFront(4)){
                    --clickX;
                }
                if (position.sideHit == EnumFacing.getFront(5)){
                    ++clickX;
                }

                if (!playerIn.canPlayerEdit(new BlockPos(clickX, clickY, clickZ), position.sideHit, itemStackIn)){
                    return new ActionResult(EnumActionResult.PASS, itemStackIn);
                }
                
                if(worldIn.getBlockState(position.getBlockPos()).getBlock() instanceof BlockMaterials 
                		&& worldIn.getTileEntity(position.getBlockPos())!=null 
                		&& worldIn.getTileEntity(position.getBlockPos()) instanceof TileBlockPillar){
                    return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
                }

                if (this.tryPlaceContainedLiquid(worldIn, clickX, clickY, clickZ, itemStackIn.getItemDamage()) && !playerIn.capabilities.isCreativeMode){
                	IFluidHandler fh = itemStackIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                	((FluidHandlerItemStack)fh).drain(Fluid.BUCKET_VOLUME, true);
                	return new ActionResult(EnumActionResult.SUCCESS, new ItemStack(Items.BUCKET));
                    
                }
            }
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
    }

    public boolean tryPlaceContainedLiquid (World world, int clickX, int clickY, int clickZ, int meta){
    	BlockPos pos = new BlockPos(clickX, clickY, clickZ);
        Material material = world.getBlockState(pos).getMaterial();
        boolean flag = !material.isSolid();

        if (!world.isAirBlock(pos) && !flag){
            return false;
        }else{
            if (!world.isRemote && flag && !material.isLiquid()){
            	world.destroyBlock(pos, true);
            }
            world.setBlockState(pos, ModFluids.blockLiquefiedAsgardite.getDefaultState());
            return true;
        }
    }

    /*@Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, net.minecraft.nbt.NBTTagCompound nbt) {
    	super.initCapabilities(stack, nbt);
        return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
    }*/
}
