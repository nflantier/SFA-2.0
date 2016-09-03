package noelflantier.sfartifacts.compatibilities;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import noelflantier.sfartifacts.common.handlers.ModConfig;

public class IC2Handler {
	public static double[] voltageTier = new double[]{32*32,128*128,256*256,256*256};
	public static double convertRFtoEU(int rf, int maxTier){
		return rf/(double)ModConfig.oneEuToRf;
	}
	public static int convertEUtoRF(double eu){
		return (int)(eu*ModConfig.oneEuToRf);
	}
	public static boolean isEnergyStorage(TileEntity sink){
		return sink instanceof IEnergyStorage;
	}
	public static double injectEnergy(TileEntity sink, EnumFacing fd, double amount, double voltage, boolean simulate){
		double demanded = Math.max(0, ((IEnergySink)sink).getDemandedEnergy());
		double accepted = Math.min(demanded, amount);
		if(!simulate)
			((IEnergySink)sink).injectEnergy(fd, amount, voltage);
		return amount-accepted;
	}
	
	
	
	
	public static boolean isEnergySink(TileEntity sink){
		return sink instanceof IEnergySink;
	}/*
	public static double injectEnergy(TileEntity sink, EnumFacing fd, double amount, double voltage, boolean simulate){
		double demanded = Math.max(0, ((IEnergySink)sink).getDemandedEnergy());
		double accepted = Math.min(demanded, amount);
		if(!simulate)
			((IEnergySink)sink).injectEnergy(fd, amount, voltage);
		return amount-accepted;
	}*/
	public static double injectEnergy(TileEntity sink, EnumFacing fd, double amount, boolean simulate){
		double demanded = Math.max(0, ((IEnergySink)sink).getDemandedEnergy());
		double accepted = Math.min(demanded, amount);
		if(!simulate)
			((IEnergySink)sink).injectEnergy(fd, amount, getVoltageByTier(sink));
		return amount-accepted;
	}
	public static double getVoltageByTier(TileEntity sink){
		return voltageTier[((IEnergySink)sink).getSinkTier()-1];
	}
	public static void loadIC2Tile(TileEntity tile){
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile)tile));
	}
	public static void unloadIC2Tile(TileEntity tile){
		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile)tile));
	}	
	public static boolean isAcceptingEnergySink(TileEntity sink, TileEntity tile, EnumFacing fd){
		return sink instanceof IEnergySink && tile instanceof IEnergyEmitter && ((IEnergySink)sink).acceptsEnergyFrom((IEnergyEmitter)tile, fd);
	}
	public static boolean isElectricItem(ItemStack stack){
		return stack!=null && stack.getItem() instanceof IElectricItem;
	}
	public static double getCharge(ItemStack stack){
		return ElectricItem.manager.getCharge(stack);
	}
	public static double getMaxCharge(ItemStack stack){
		return isElectricItem(stack)?((IElectricItem)stack.getItem()).getMaxCharge(stack):0;
	}
	public static double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate){
		return ElectricItem.manager.charge(stack, amount, tier, ignoreTransferLimit, simulate);
	}
}
