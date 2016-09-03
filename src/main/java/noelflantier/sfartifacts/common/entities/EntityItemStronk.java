package noelflantier.sfartifacts.common.entities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityItemStronk extends EntityItem{

    public EntityItemStronk(World w, double x, double y, double z, ItemStack stack)
    {
    	super(w,x,y,z,stack);
    	this.isImmuneToFire = true;
    	this.fireResistance = 1000;
    	setNoPickupDelay();
    }
    public EntityItemStronk(World w, double x, double y, double z)
    {
    	super(w,x,y,z);
    	this.isImmuneToFire = true;
    	this.fireResistance = 1000;
    	setNoPickupDelay();
    }
	public EntityItemStronk(World w) {
		super(w);
    	this.isImmuneToFire = true;
    	this.fireResistance = 1000;
    	setNoPickupDelay();
	}

    @Override
    public void onUpdate(){
    	this.isImmuneToFire = true;
    	super.onUpdate();
    	this.isImmuneToFire = true;
    }

    @Override
    public void setFire(int p_70015_1_){
    }
    
    @Override
    protected void dealFireDamage(int p_70081_1_){
    }
    
    
    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_){
    	return false;
    }
}
