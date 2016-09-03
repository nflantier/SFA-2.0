package noelflantier.sfartifacts.common.world.village;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class ComponentPillar extends StructureVillagePieces.Village{
	
	private int averageGroundLevel = -1;
	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 18, 0);
        }
        
        Random rd = new Random();
        /*int material = rd.nextInt(PillarMaterials.values().length);
        int pillar = rd.nextInt(PillarsConfig.getInstance().nameOrderedBySize.size());
        String name = PillarsConfig.getInstance().nameOrderedBySize.get(pillar);
		if(name!=null && PillarsConfig.getInstance().nameToPillar.containsKey(name)){
			Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
			for(Entry<String, Coord4> entry : p.mapStructure.entrySet()){
	        	if(randomIn.nextFloat()<0.80)
	        		this.placeBlockAtCurrentPosition(worldIn, PillarMaterials.values()[material].block, 0, entry.getValue().x, entry.getValue().y, entry.getValue().z, sbb);
			}
		}*/
    	return true;
	}

    /*private int averageGroundLevel = -1;
    public ComponentPillar()
    {
    }

    public ComponentPillar(Start villagePiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super();
        this.coordBaseMode = par5;
        this.boundingBox = par4StructureBoundingBox;
    }
    
	public static Object buildComponent(Start startPiece, List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
		//test seed 104079552
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 10,0, 10, 10, 20, 10, p4);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new ComponentPillar(startPiece, p5, random, structureboundingbox, p4) : null;
	}

    @Override
    public boolean addComponentParts (World world, Random random, StructureBoundingBox sbb)
    {
    	
        if (this.averageGroundLevel < 0)
        {
            this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);

            if (this.averageGroundLevel < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 18, 0);
        }
        
        Random rd = new Random();
        int material = rd.nextInt(PillarMaterials.values().length);
        int pillar = rd.nextInt(PillarsConfig.getInstance().nameOrderedBySize.size());
        String name = PillarsConfig.getInstance().nameOrderedBySize.get(pillar);
		if(name!=null && PillarsConfig.getInstance().nameToPillar.containsKey(name)){
			Pillar p = PillarsConfig.getInstance().nameToPillar.get(name);
			for(Entry<String, Coord4> entry : p.mapStructure.entrySet()){
	        	if(random.nextFloat()<0.80)
	        		this.placeBlockAtCurrentPosition(world, PillarMaterials.values()[material].block, 0, entry.getValue().x, entry.getValue().y, entry.getValue().z, sbb);
			}
		}
    	return true;
    }*/
}
