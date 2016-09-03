package noelflantier.sfartifacts.common.entities.vanilla;

import java.util.Calendar;

import javax.annotation.Nullable;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeSnow;

public class EntitySkeletonWither extends EntitySkeleton{

	SkeletonType typeSkeleton =SkeletonType.NORMAL;
	
	public EntitySkeletonWither(World worldIn) {
		super(worldIn);
	}

	public EntitySkeletonWither(World worldIn,SkeletonType type) {
		super(worldIn);
		typeSkeleton = type;
	}
	
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata)
    {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (typeSkeleton == SkeletonType.WITHER)
        {
            this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.2D, false));
            this.func_189768_a(SkeletonType.WITHER);
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
            this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        }
        else
        {
            Biome biome = this.worldObj.getBiomeGenForCoords(new BlockPos(this));

            if (typeSkeleton == SkeletonType.STRAY)
            {
                this.func_189768_a(SkeletonType.STRAY);
            } 
            this.tasks.addTask(4, new EntityAIAttackRangedBow(this, 1.0D, 20, 15.0F));
            this.setEquipmentBasedOnDifficulty(difficulty);
            this.setEnchantmentBasedOnDifficulty(difficulty);
        }

        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty());

        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null)
        {
            Calendar calendar = this.worldObj.getCurrentDate();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
            {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return livingdata;
    }
}
