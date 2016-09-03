package noelflantier.sfartifacts.common.blocks;

import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.property.IUnlistedProperty;
import noelflantier.sfartifacts.common.handlers.ModBlocks;

public class SFAProperties {

	public static class UnlistedPropertyInductor implements IUnlistedProperty<Boolean> {

	    private final String name;

	    public UnlistedPropertyInductor(String name) {
	        this.name = name;
	    }

	    @Override
	    public String getName() {
	        return name;
	    }

	    @Override
	    public boolean isValid(Boolean value) {
	        return true;
	    }

	    @Override
	    public Class<Boolean> getType() {
	        return Boolean.class;
	    }

	    @Override
	    public String valueToString(Boolean value) {
	        return value.toString();
	    }
	}
	
	public static enum EnumPillarBlockType implements IStringSerializable{
		NO_PILLAR_NORMAL("normal"),
		PILLAR_NORMAL("pillar_normal"),
		PILLAR_INTERFACE("pillar_interface"),
		PILLAR_MASTER("pillar_master"),
		PILLAR_RENDER("pillar_render");
		
		private String name;
		
		private EnumPillarBlockType(String name)
	    {
	        this.name = name;
	    }
		@Override
		public String getName() {
			return name;
		}
	}
	public static class PropertyPillarBlockType extends PropertyEnum<EnumPillarBlockType>{

		protected PropertyPillarBlockType(String name, Collection<EnumPillarBlockType> allowedValues) {
			super(name, EnumPillarBlockType.class, allowedValues);
		}
		public static PropertyPillarBlockType create(String name)
	    {
	        return create(name, Predicates.<EnumPillarBlockType>alwaysTrue());
	    }
	    public static PropertyPillarBlockType create(String name, Predicate<EnumPillarBlockType> filter)
	    {
	        return create(name, Collections2.<EnumPillarBlockType>filter(Lists.newArrayList(EnumPillarBlockType.values()), filter));
	    }
	    public static PropertyPillarBlockType create(String name, Collection<EnumPillarBlockType> values)
	    {
	        return new PropertyPillarBlockType(name, values);
	    }
	}
	
	public static enum EnumStatusVibranium implements IStringSerializable{
		S0("0"),
		S1("6"),
		S2("12"),
		S3("18"),
		S4("24"),
		S5("30"),
		S6("36"),
		S7("42"),
		S8("56"),
		S9("62"),
		S10("68"),
		S11("74"),
		S12("80"),
		S13("86"),
		S14("92"),
		S15("100");
		
		private String name;
		
		private EnumStatusVibranium(String name)
	    {
	        this.name = name;
	    }
		@Override
		public String getName() {
			return name;
		}
		
	}
	public static class PropertyStatusVibranium extends PropertyEnum<EnumStatusVibranium>{

		protected PropertyStatusVibranium(String name, Collection<EnumStatusVibranium> allowedValues) {
			super(name, EnumStatusVibranium.class, allowedValues);
		}
		public static PropertyStatusVibranium create(String name)
	    {
	        return create(name, Predicates.<EnumStatusVibranium>alwaysTrue());
	    }
	    public static PropertyStatusVibranium create(String name, Predicate<EnumStatusVibranium> filter)
	    {
	        return create(name, Collections2.<EnumStatusVibranium>filter(Lists.newArrayList(EnumStatusVibranium.values()), filter));
	    }
	    public static PropertyStatusVibranium create(String name, Collection<EnumStatusVibranium> values)
	    {
	        return new PropertyStatusVibranium(name, values);
	    }
	}
	
	public static enum EnumTypeTech implements IStringSerializable{
		BASIC("basic"),
		ADVANCED("advanced"),
		BASIC_E("basicenergized"),
		ADVANCED_E("advancedenergized");
		
		private String name;
		
		private EnumTypeTech(String name)
	    {
	        this.name = name;
	    }
		@Override
		public String getName() {
			return name;
		}
	}
	public static class PropertyTypeTech extends PropertyEnum<EnumTypeTech>{

		protected PropertyTypeTech(String name, Collection<EnumTypeTech> allowedValues) {
			super(name, EnumTypeTech.class, allowedValues);
		}
		public static PropertyTypeTech create(String name)
	    {
	        return create(name, Predicates.<EnumTypeTech>alwaysTrue());
	    }
	    public static PropertyTypeTech create(String name, Predicate<EnumTypeTech> filter)
	    {
	        return create(name, Collections2.<EnumTypeTech>filter(Lists.newArrayList(EnumTypeTech.values()), filter));
	    }
	    public static PropertyTypeTech create(String name, Collection<EnumTypeTech> values)
	    {
	        return new PropertyTypeTech(name, values);
	    }
	}

	public static enum EnumPillarMaterial implements IStringSerializable{
		ASGARDITE("asgardite",BlockAsgardite.class,2,256,3.9F,ModBlocks.blockAsgardite),
		STEEL("steel",BlockAsgardianSteel.class,10,256,1.5F,ModBlocks.blockAsgardianSteel),
		BRONZE("bronze",BlockAsgardianBronze.class,18,256,1.5F,ModBlocks.blockAsgardianBronze);
		
		public final int ID;
		public final Class<? extends Block> blockclass;
		public final float energyRatio;
		public final float heightRatio;
		public final float rainRatio;
		private String name;
		public static int globalID = 0;
		public Item item;
		public Block block;
		
		private EnumPillarMaterial(String name, Class <? extends Block> blockclass, int energyRatio, int maxHeightEfficiency, float rainEfficiency, Block bl){
			this.ID = nextGlobalID();
			this.blockclass = blockclass;
			this.energyRatio = energyRatio;
			this.rainRatio = rainEfficiency;
			this.heightRatio = maxHeightEfficiency;
			this.block = bl;
	        this.name = name;
			this.item = Item.getItemFromBlock(bl);
		}
		
		private int nextGlobalID(){
			globalID++;
			return globalID;
		}
		
		public static EnumPillarMaterial getMaterialFromId(int id){
			for(EnumPillarMaterial pm : EnumPillarMaterial.values()){
				if(pm.ID==id)return pm;
			}
			return null;
		} 
		public static EnumPillarMaterial getMaterialFromClass(Class <? extends Block> blockclass){
			for(EnumPillarMaterial pm : EnumPillarMaterial.values()){
				if(pm.blockclass==blockclass)return pm;
			}
			return null;
		}

		@Override
		public String getName() {
			return name;
		} 
		
	}
	public static class PropertyMaterial extends PropertyEnum<EnumPillarMaterial>{

		protected PropertyMaterial(String name, Collection<EnumPillarMaterial> allowedValues) {
			super(name, EnumPillarMaterial.class, allowedValues);
		}
		public static PropertyMaterial create(String name)
	    {
	        return create(name, Predicates.<EnumPillarMaterial>alwaysTrue());
	    }
	    public static PropertyMaterial create(String name, Predicate<EnumPillarMaterial> filter)
	    {
	        return create(name, Collections2.<EnumPillarMaterial>filter(Lists.newArrayList(EnumPillarMaterial.values()), filter));
	    }
	    public static PropertyMaterial create(String name, Collection<EnumPillarMaterial> values)
	    {
	        return new PropertyMaterial(name, values);
	    }
	}
}
