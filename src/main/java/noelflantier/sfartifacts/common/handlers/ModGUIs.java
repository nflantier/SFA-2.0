package noelflantier.sfartifacts.common.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import noelflantier.sfartifacts.client.gui.GuiControlPanel;
import noelflantier.sfartifacts.client.gui.GuiHammerConfig;
import noelflantier.sfartifacts.client.gui.GuiHammerStandNonInvoked;
import noelflantier.sfartifacts.client.gui.GuiInductor;
import noelflantier.sfartifacts.client.gui.GuiInjector;
import noelflantier.sfartifacts.client.gui.GuiLightningRodStand;
import noelflantier.sfartifacts.client.gui.GuiLiquefier;
import noelflantier.sfartifacts.client.gui.GuiMightyFoundry;
import noelflantier.sfartifacts.client.gui.GuiMoldMaking;
import noelflantier.sfartifacts.client.gui.GuiMrFusion;
import noelflantier.sfartifacts.client.gui.GuiRecharger;
import noelflantier.sfartifacts.client.gui.GuiSoundEmitter;
import noelflantier.sfartifacts.client.gui.GuiTeleport;
import noelflantier.sfartifacts.client.gui.manual.NGuiManual;
import noelflantier.sfartifacts.common.container.ContainerControlPanel;
import noelflantier.sfartifacts.common.container.ContainerHammerConfig;
import noelflantier.sfartifacts.common.container.ContainerHammerStandInvoked;
import noelflantier.sfartifacts.common.container.ContainerHammerStandNonInvoked;
import noelflantier.sfartifacts.common.container.ContainerInductor;
import noelflantier.sfartifacts.common.container.ContainerInjector;
import noelflantier.sfartifacts.common.container.ContainerLightningRodStand;
import noelflantier.sfartifacts.common.container.ContainerLiquefier;
import noelflantier.sfartifacts.common.container.ContainerMightyFoundry;
import noelflantier.sfartifacts.common.container.ContainerMoldMaking;
import noelflantier.sfartifacts.common.container.ContainerMrFusion;
import noelflantier.sfartifacts.common.container.ContainerRecharger;
import noelflantier.sfartifacts.common.container.ContainerSoundEmitter;
import noelflantier.sfartifacts.common.container.ContainerTeleport;
import noelflantier.sfartifacts.common.tileentities.TileControlPanel;
import noelflantier.sfartifacts.common.tileentities.TileHammerStand;
import noelflantier.sfartifacts.common.tileentities.TileInductor;
import noelflantier.sfartifacts.common.tileentities.TileInjector;
import noelflantier.sfartifacts.common.tileentities.TileLightningRodStand;
import noelflantier.sfartifacts.common.tileentities.TileLiquefier;
import noelflantier.sfartifacts.common.tileentities.TileMightyFoundry;
import noelflantier.sfartifacts.common.tileentities.TileMrFusion;
import noelflantier.sfartifacts.common.tileentities.TileRecharger;
import noelflantier.sfartifacts.common.tileentities.TileSoundEmiter;

public class ModGUIs implements IGuiHandler{

	public static final int guiIDControlPanel = 0;
	public static final int guiIDHammerStandNonInvoked = 1;
	public static final int guiIDHammerStandInvoked = 2;
	public static final int guiIDHammerConfig = 3;
	public static final int guiIDLiquefier = 4;
	public static final int guiIDInjector = 5;
	public static final int guiIDLightningRodStand = 6;
	public static final int guiIDManual= 7;
	public static final int guiIDTeleport= 9;
	public static final int guiIDMightyFoundry = 10;
	public static final int guiIDMold= 11;
	public static final int guiIDSoundEmiter= 12;
	public static final int guiIDMrFusion= 13;
	public static final int guiIDInductor= 14;
	public static final int guiIDRecharger= 15;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x,y,z));
		if(tile!=null){
			switch(ID){
				case ModGUIs.guiIDControlPanel :
					if(tile instanceof TileControlPanel){
						return new ContainerControlPanel(player.inventory, (TileControlPanel)tile);
					}
					return null;
				case ModGUIs.guiIDHammerStandNonInvoked :
					if(tile instanceof TileHammerStand){
						return new ContainerHammerStandNonInvoked(player.inventory, (TileHammerStand)tile);
					}
					return null;
				case ModGUIs.guiIDHammerStandInvoked :
					if(tile instanceof TileHammerStand){
						return new ContainerHammerStandInvoked(player.inventory, (TileHammerStand)tile);
					}
					return null;
				case ModGUIs.guiIDLiquefier :
					if(tile instanceof TileLiquefier){
						return new ContainerLiquefier(player.inventory, (TileLiquefier)tile);
					}
					return null;
				case ModGUIs.guiIDInjector :
					if(tile instanceof TileInjector){
						return new ContainerInjector(player.inventory, (TileInjector)tile);
					}
					return null;
				case ModGUIs.guiIDLightningRodStand :
					if(tile instanceof TileLightningRodStand){
						return new ContainerLightningRodStand(player.inventory, (TileLightningRodStand)tile);
					}
					return null;
				case ModGUIs.guiIDMightyFoundry :
					if(tile instanceof TileMightyFoundry){
						return new ContainerMightyFoundry(player.inventory, (TileMightyFoundry)tile);
					}
					return null;
				case ModGUIs.guiIDSoundEmiter :
					if(tile instanceof TileSoundEmiter){
						return new ContainerSoundEmitter(player.inventory, (TileSoundEmiter)tile);
					}
					return null;
				case ModGUIs.guiIDMrFusion :
					if(tile instanceof TileMrFusion){
						return new ContainerMrFusion(player.inventory, (TileMrFusion)tile);
					}
					return null;
				case ModGUIs.guiIDInductor :
					if(tile instanceof TileInductor){
						return new ContainerInductor(player.inventory, (TileInductor)tile);
					}
					return null;
				case ModGUIs.guiIDRecharger :
					if(tile instanceof TileRecharger){
						return new ContainerRecharger(player.inventory, (TileRecharger)tile);
					}
					return null;
			}
		}else if(ID == ModGUIs.guiIDMold){
			return new ContainerMoldMaking(player);
		}else if(ID == ModGUIs.guiIDTeleport){
			return new ContainerTeleport(player.inventory);
		}else if(ID == ModGUIs.guiIDHammerConfig){
			return new ContainerHammerConfig(player.inventory);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x,y,z));
		if(tile!=null){
			switch(ID){
				case ModGUIs.guiIDControlPanel :
					if(tile instanceof TileControlPanel){
						return new GuiControlPanel(player.inventory, (TileControlPanel)tile);
					}
					return null;
				case ModGUIs.guiIDHammerStandNonInvoked :
					if(tile instanceof TileHammerStand){
						return new GuiHammerStandNonInvoked(player.inventory, (TileHammerStand)tile);
					}
					return null;
				case ModGUIs.guiIDHammerStandInvoked :
					if(tile instanceof TileHammerStand){
						return new GuiHammerConfig(player.inventory, (TileHammerStand)tile);
					}
					return null;
				case ModGUIs.guiIDLiquefier :
					if(tile instanceof TileLiquefier){
						return new GuiLiquefier(player.inventory, (TileLiquefier)tile);
					}
					return null;
				case ModGUIs.guiIDInjector :
					if(tile instanceof TileInjector){
						return new GuiInjector(player.inventory, (TileInjector)tile);
					}
					return null;
				case ModGUIs.guiIDLightningRodStand :
					if(tile instanceof TileLightningRodStand){
						return new GuiLightningRodStand(player.inventory, (TileLightningRodStand)tile);
					}
					return null;
				case ModGUIs.guiIDMightyFoundry :
					if(tile instanceof TileMightyFoundry){
						return new GuiMightyFoundry(player.inventory, (TileMightyFoundry)tile);
					}
					return null;
				case ModGUIs.guiIDSoundEmiter :
					if(tile instanceof TileSoundEmiter){
						return new GuiSoundEmitter(player.inventory, (TileSoundEmiter)tile);
					}
					return null;
				case ModGUIs.guiIDMrFusion :
					if(tile instanceof TileMrFusion){
						return new GuiMrFusion(player.inventory, (TileMrFusion)tile);
					}
					return null;
				case ModGUIs.guiIDInductor :
					if(tile instanceof TileInductor){
						return new GuiInductor(player.inventory, (TileInductor)tile);
					}
					return null;
				case ModGUIs.guiIDRecharger :
					if(tile instanceof TileRecharger){
						return new GuiRecharger(player.inventory, (TileRecharger)tile);
					}
					return null;
			}
		}else if(ID == ModGUIs.guiIDHammerConfig){
			return new GuiHammerConfig(player.inventory);
		}else if(ID == ModGUIs.guiIDManual){
			return new NGuiManual(player);
		}else if(ID == ModGUIs.guiIDTeleport){
			return new GuiTeleport(player);
		}else if(ID == ModGUIs.guiIDMold){
			return new GuiMoldMaking(player);
		}
		return null;
	}

}
