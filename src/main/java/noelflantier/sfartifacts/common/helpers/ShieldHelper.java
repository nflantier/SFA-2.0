package noelflantier.sfartifacts.common.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import noelflantier.sfartifacts.common.entities.EntityHammerMinning;
import noelflantier.sfartifacts.common.entities.EntityShieldThrow;

public class ShieldHelper {

	public static void startThrowing(World worldIn, EntityPlayer playerIn, ItemStack shield, int type) {
		EntityShieldThrow entitys = new EntityShieldThrow(worldIn, playerIn, shield, type);
		entitys.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 0.0F);
        worldIn.spawnEntityInWorld(entitys);
		return;
	}
}
