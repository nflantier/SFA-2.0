package noelflantier.sfartifacts.common.tileentities;

public interface ITileCanTakeRFonlyFromPillars {
	int receiveOnlyFromPillars(int maxReceive, boolean simulate);
	int extractOnlyFromPillars(int maxExtract, boolean simulate);
}
