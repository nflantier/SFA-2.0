package noelflantier.sfartifacts.client.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noelflantier.sfartifacts.common.entities.EntityHulk;

@SideOnly(Side.CLIENT)
public class ModelHulk extends ModelBase{

	public ModelRenderer bassin;
    public ModelRenderer pec;
    public ModelRenderer head;
    public ModelRenderer rightFoot;
    public ModelRenderer rightLeg;
    public ModelRenderer leftFoot;
    public ModelRenderer leftLeg;
    public ModelRenderer rightUpperArm;
    public ModelRenderer rightArm;
    public ModelRenderer leftUpperArm;
    public ModelRenderer leftArm;
    
    public ModelHulk() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.rightLeg = new ModelRenderer(this, 0, 96);
        this.rightLeg.setRotationPoint(-14.0F, -7.0F, -1.0F);
        this.rightLeg.addBox(0.0F, 0.0F, 1.0F, 13, 17, 13, 0.0F);
        this.setRotateAngle(rightLeg, -0.3490658503988659F, 0.0F, 0.0F);
        this.leftFoot = new ModelRenderer(this, 52, 96);
        this.leftFoot.setRotationPoint(2.0F, 9.0F, -5.0F);
        this.leftFoot.addBox(0.0F, 0.0F, 0.0F, 11, 15, 11, 0.0F);
        this.rightArm = new ModelRenderer(this, 0, 126);
        this.rightArm.setRotationPoint(-37.0F, -24.0F, 0.0F);
        this.rightArm.addBox(0.0F, 0.0F, 0.0F, 15, 29, 15, 0.0F);
        this.setRotateAngle(rightArm, -0.4363323129985824F, 0.0F, 0.17453292519943295F);
        this.leftLeg = new ModelRenderer(this, 0, 96);
        this.leftLeg.setRotationPoint(1.0F, -7.0F, -1.0F);
        this.leftLeg.addBox(0.0F, 0.0F, 1.0F, 13, 17, 13, 0.0F);
        this.setRotateAngle(leftLeg, -0.3490658503988659F, 0.0F, 0.0F);
        this.leftArm = new ModelRenderer(this, 0, 126);
        this.leftArm.setRotationPoint(22.0F, -20.0F, 0.0F);
        this.leftArm.addBox(0.0F, 0.0F, 0.0F, 15, 29, 15, 0.0F);
        this.setRotateAngle(leftArm, -0.4363323129985824F, 0.0F, -0.17453292519943295F);
        this.leftUpperArm = new ModelRenderer(this, 60, 126);
        this.leftUpperArm.setRotationPoint(12.0F, -30.0F, -6.0F);
        this.leftUpperArm.addBox(0.0F, 0.0F, 0.0F, 13, 26, 13, 0.0F);
        this.setRotateAngle(leftUpperArm, 0.3490658503988659F, 0.0F, -0.6108652381980153F);
        this.pec = new ModelRenderer(this, 0, 0);
        this.pec.setRotationPoint(-21.0F, -35.0F, -15.0F);
        this.pec.addBox(0.0F, 0.0F, 0.0F, 42, 29, 27, 0.0F);
        this.setRotateAngle(pec, 0.4363323129985824F, 0.0F, 0.0F);
        this.bassin = new ModelRenderer(this, 0, 56);
        this.bassin.setRotationPoint(-16.0F, -22.0F, -2.0F);
        this.bassin.addBox(0.0F, 0.0F, 0.0F, 32, 21, 19, 0.0F);
        this.rightFoot = new ModelRenderer(this, 52, 96);
        this.rightFoot.setRotationPoint(-13.0F, 9.0F, -5.0F);
        this.rightFoot.addBox(0.0F, 0.0F, 0.0F, 11, 15, 11, 0.0F);
        this.rightFoot.mirror = true;
        this.rightUpperArm = new ModelRenderer(this, 60, 126);
        this.rightUpperArm.setRotationPoint(-23.0F, -38.0F, -6.0F);
        this.rightUpperArm.addBox(0.0F, 0.0F, 0.0F, 13, 26, 13, 0.0F);
        this.setRotateAngle(rightUpperArm, 0.3490658503988659F, 0.0F, 0.6108652381980153F);
        this.head = new ModelRenderer(this, 0, 170);
        this.head.setRotationPoint(-7.0F, -52.0F, -12.0F);
        this.head.addBox(0.0F, 0.0F, 0.0F, 14, 14, 14, 0.0F);
        this.setRotateAngle(head, 0.17453292519943295F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
    	super.render(entity, f, f1, f2, f3, f4, f5);
    	this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.rightLeg.render(f5);
        this.leftFoot.render(f5);
        this.rightArm.render(f5);
        this.leftLeg.render(f5);
        this.leftArm.render(f5);
        this.leftUpperArm.render(f5);
        this.pec.render(f5);
        this.bassin.render(f5);
        this.rightFoot.render(f5);
        this.rightUpperArm.render(f5);
        this.head.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
       /* this.head.rotateAngleY = f3 / (180F / (float)Math.PI);
        this.head.rotateAngleX = f4 / (180F / (float)Math.PI);
        this.rightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 2.0F * f1 * 0.5F;
        this.leftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        this.rightArm.rotateAngleZ = 0.0F;
        this.leftArm.rotateAngleZ = 0.0F;
        
        this.rightFoot.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.1F * f1;
        this.leftFoot.rotateAngleX = MathHelper.cos(f * 0.6662F + (float)Math.PI) * 1.1F * f1;
        this.rightFoot.rotateAngleY = 0.0F;
        this.leftFoot.rotateAngleY = 0.0F;
        
        float f6 = MathHelper.sin(this.onGround * (float)Math.PI);
        float f7 = MathHelper.sin((1.0F - (1.0F - this.onGround) * (1.0F - this.onGround)) * (float)Math.PI);
        this.rightArm.rotateAngleZ = 0.0F;
        this.leftArm.rotateAngleZ = 0.0F;
        this.rightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
        this.leftArm.rotateAngleY = 0.1F - f6 * 0.6F;
        this.rightArm.rotateAngleX = -((float)Math.PI / 2F);
        this.leftArm.rotateAngleX = -((float)Math.PI / 2F);
        this.rightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
        this.leftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
        this.rightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        this.leftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        this.rightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        this.leftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;*/
        this.head.rotateAngleY = f3*0.8F / (180F / (float)Math.PI);
        this.head.rotateAngleX = f4*0.8F / (180F / (float)Math.PI);
        this.leftFoot.rotateAngleX = -1.5F * this.func_78172_a(f, 13.0F) * f1;
        this.rightFoot.rotateAngleX = 1.5F * this.func_78172_a(f, 13.0F) * f1;
        this.leftFoot.rotateAngleY = 0.0F;
        this.rightFoot.rotateAngleY = 0.0F;
    	
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    } 
    
    public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
    	super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
    	
      	EntityHulk entity = (EntityHulk)p_78086_1_;

      	int smash = entity.getAnimationSmash();
      	int fling = entity.getAttackFlingTimer();
      	
        if (fling > 0){
            this.rightArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)fling - p_78086_4_, 10.0F);
            this.leftArm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)fling - p_78086_4_, 10.0F);
        }else if(smash > 0){
            this.rightArm.rotateAngleX = -1 + 0.8F * this.func_78172_a((float)smash - p_78086_4_, 10.0F);
            this.leftArm.rotateAngleX = -1 + 0.8F * this.func_78172_a((float)smash - p_78086_4_, 10.0F);
      	}else{
            this.rightArm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_;
            this.leftArm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(p_78086_2_, 13.0F)) * p_78086_3_;
        }
    }

    private float func_78172_a(float p_78172_1_, float p_78172_2_)
    {
        return (Math.abs(p_78172_1_ % p_78172_2_ - p_78172_2_ * 0.5F) - p_78172_2_ * 0.25F) / (p_78172_2_ * 0.25F);
    }

}
