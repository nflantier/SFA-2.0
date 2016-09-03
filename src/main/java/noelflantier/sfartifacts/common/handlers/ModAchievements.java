package noelflantier.sfartifacts.common.handlers;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.AchievementPage;
import noelflantier.sfartifacts.Ressources;

public class ModAchievements {
	private static AchievementPage achievementsPage;
	private static HashMap<String, Achievement> achievementsList = new HashMap<String, Achievement>();
	public static final Achievement GETTING_MANUAL = (new Achievement(Ressources.MODID+".achievement.gettingManual", "gettingManual", 0, 0, ModItems.itemManual, null)).registerStat();

	public static Achievement getAchievement (String name){
		return achievementsList.get(name);
	}

	public static void addModAchievements(){
		achievementsList.put(GETTING_MANUAL.statId, GETTING_MANUAL);
	}

	public static void registerAchievementPane (){
		achievementsPage = new AchievementPage(Ressources.NAME, achievementsList.values().toArray(new Achievement[achievementsList.size()]));
		AchievementPage.registerAchievementPage(achievementsPage);
	}
}
