package noelflantier.sfartifacts.common.world.village;

import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import noelflantier.sfartifacts.common.blocks.SFAProperties.EnumPillarMaterial;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig;
import noelflantier.sfartifacts.common.recipes.handler.PillarsConfig.Pillar;

public class ComponentPillar extends StructureVillagePieces.Village{

    private int averageGroundLevel = -1;
	public ComponentPillar(){
    }

    public ComponentPillar(StructureVillagePieces.Start start, int type, Random rand, StructureBoundingBox structureBoundingBox, EnumFacing facing){
        super(start, type);
        this.setCoordBaseMode(facing);
        this.boundingBox = structureBoundingBox;
    }
	
    public static ComponentPillar buildComponent(Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5){
    	StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, -5 ,0, -5, 5, 20, 5, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new ComponentPillar(startPiece, p5, random, structureboundingbox, facing) : null;
    }
    
	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
		EnumPillarMaterial material = EnumPillarMaterial.values()[randomIn.nextInt(EnumPillarMaterial.values().length)];
		Pillar pillar = PillarsConfig.getInstance().nameToPillar.get(PillarsConfig.getInstance().nameOrderedBySize.get(randomIn.nextInt(PillarsConfig.getInstance().nameOrderedBySize.size())));
        
		//material = EnumPillarMaterial.ASGARDITE;
		//pillar = PillarsConfig.getInstance().nameToPillar.get("Advanced");
		
		if (this.averageGroundLevel < 0){
            this.averageGroundLevel = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (this.averageGroundLevel < 0){
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 18, 0);
        }
        
        if(pillar!=null && material!=null){
	        for(Entry<String, BlockPos> entry : pillar.mapStructure.entrySet()){
	        	if(randomIn.nextFloat()<0.70)
	        		this.fillWithBlocks(worldIn, structureBoundingBoxIn, entry.getValue().getX(), entry.getValue().getY(), entry.getValue().getZ(), entry.getValue().getX(), entry.getValue().getY(), entry.getValue().getZ(), material.block.getDefaultState(), material.block.getDefaultState(), false);
	        }
        }
        
    	return true;
	}
}
