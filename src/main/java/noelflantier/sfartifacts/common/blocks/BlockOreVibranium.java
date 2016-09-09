package noelflantier.sfartifacts.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumStatusVibranium;
import noelflantier.sfartifacts.common.blocks.SFAProperties.PropertyStatusVibranium;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.ModItems;
import noelflantier.sfartifacts.common.helpers.ParticleHelper;

public class BlockOreVibranium extends ABlockSFA{
	public static String[] typeVibranium = new String[] {"0", "35", "65", "100"};
	public static int[][] tabC = {{0,0,1},{0,0,-1},{1,0,0},{-1,0,0},{0,1,0},{0,-1,0}};
	public static int tickBase = ModConfig.tickToCookVibraniumOres/16;
    public static final PropertyStatusVibranium SV = PropertyStatusVibranium.create("status");
    
	public BlockOreVibranium(Material materialIn) {
		super(materialIn);
		setRegistryName(Ressources.UL_NAME_ORE_VIBRANIUM);
		setUnlocalizedName(Ressources.UL_NAME_ORE_VIBRANIUM);
		setHarvestLevel("pickaxe",3);
        setDefaultState(this.blockState.getBaseState().withProperty(SV, EnumStatusVibranium.S0));
		setHardness(3.0F);
		setResistance(2000.0F);
	}
	@Override
	public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{SV});
	}
	
    
    public IBlockState getStateFromMeta(int meta){    
    	return this.getDefaultState().withProperty(SV, EnumStatusVibranium.values()[meta]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state){
        return ((EnumStatusVibranium) state.getValue(SV)).ordinal();
    }

	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune){
        return getMetaFromState(state)==15?ModItems.itemVibraniumDust:super.getItemDropped(state, rand, fortune);
    }

    @Override
    public int quantityDroppedWithBonus(int p_149679_1_, Random random)
    {
        return this.quantityDropped(random) + random.nextInt(p_149679_1_ + 1);
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 2+ random.nextInt(2);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
    	int metadata = getMetaFromState(state);
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(Item.getItemFromBlock(this), 1, metadata));
		return !state.getValue(SV).equals(EnumStatusVibranium.S15)?ret:super.getDrops(world, pos, state, fortune);
    	
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player){
        return true;//state.getValue(SV).equals(EnumStatusVibranium.S15)?true:false;
    }

    @Override
    public int tickRate(World world){
        return 40;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state){
    	worldIn.scheduleBlockUpdate(pos, this, this.tickRate(worldIn),0);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 15));
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand){
    	super.updateTick(world, pos, state, rand);
    	if(world.isRemote)
    		return;
    	
    	int tick = tickBase;

        for(int i = 0 ; i < this.tabC.length ; i++){
        	Block b = world.getBlockState(pos.add(this.tabC[i][0], this.tabC[i][1], this.tabC[i][2])).getBlock();
        	Fluid f = FluidRegistry.lookupFluidForBlock(b);
        	if( f!=null && f.getTemperature()>=1300)
        		tick-=40;
        }
        
        int meta = getMetaFromState(state);
    	if(meta<15){
    		if(tick<tickBase){
    			world.setBlockState(pos, getStateFromMeta(meta+1), 2);
    			world.scheduleBlockUpdate(pos, world.getBlockState(pos).getBlock(), tick<1?1:tick, 0);
    		}else{
        		world.scheduleBlockUpdate(pos, world.getBlockState(pos).getBlock(), tickRate(world)>1?tickRate(world):1, 0);
    		}
    	}
    	
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random random)
    {
    	if(ModConfig.isOresEmitParticles && random.nextFloat()>0.5F && stateIn.getValue(SV) == EnumStatusVibranium.S15){
    		float nx = (float)pos.getX()+0.5F;
    	    float ny = (float)pos.getY()+0.5F;
    	    float nz = (float)pos.getZ()+0.5F; 
    		float rdx =	0;
    	    float rdy = random.nextFloat()*2-1F;
    		float rdz = 0;
    	    if(rdy>0.5F || rdy<-0.5F){
    	    	rdx = random.nextFloat()*2-1F;
    	    	rdz = random.nextFloat()*2-1F;
    	    }else{
    	    	float tx = random.nextFloat();
    	    	if(tx>=0.5F){
    	    		rdx = random.nextFloat()*-1;
    	    	}else{
    	    		rdx = random.nextFloat();
    	    	}
    	    	
    	    	float tz = random.nextFloat();
    	    	if(tz>=0.5F){
    	    		rdz = random.nextFloat()*-1;
    	    	}else{
    	    		rdz = random.nextFloat();
    	    	}
    	    }	
    	    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, nx+rdx, ny+rdy, nz+rdz, 0.0D, 0.0D, 0.0D);
    	}
		return;
    }
}
