package noelflantier.sfartifacts.common.recipes.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import noelflantier.sfartifacts.common.recipes.AUsageRecipes;
import noelflantier.sfartifacts.common.recipes.CustomHandler;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeHammerUpgrades;
import noelflantier.sfartifacts.common.recipes.RecipeHammerUpgrades.NbtTagToAdd;
import noelflantier.sfartifacts.common.recipes.RecipeParser;

public class HammerUpgradesRecipesHandler extends AUsageRecipes{

	public static final String FILE_NAME_HAMMER_UPGRADES = "HammerUpgradesRecipes.xml";
	public static final String USAGE_HAMMER_UPGRADES = "HammerUpgrades";
	
	public HammerUpgradesRecipesHandler() {
		super(FILE_NAME_HAMMER_UPGRADES, USAGE_HAMMER_UPGRADES);
	}

	static final HammerUpgradesRecipesHandler instance = new HammerUpgradesRecipesHandler();
	public static HammerUpgradesRecipesHandler getInstance() {
		return instance;
	}

	@Override
	public CustomHandler createCustomHandler(){
		return new HammerUpgradesCustomHandler();
	}
	
	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeHammerUpgrades.class;
	}
	
	@Override
	public void loadRecipes() {
		super.loadRecipes();
	}
	
	private class HammerUpgradesCustomHandler implements CustomHandler {

	    private boolean inHammerTag = false;
	    private boolean isEnchantBook = false;
		public static final String ELEMENT_HAMMER = "hammer";
		public List<NbtTagToAdd> dummyNbtTag = null;
	    
		@Override
		public boolean endElement(String uri, String localName, String qName, RecipeParser parser) throws SAXException {
			if(ELEMENT_HAMMER.equals(localName)) {
				inHammerTag = false;
				if(parser.currentRecipe!=null && parser.classRecipe == getClassOfRecipe()){
					if(dummyNbtTag!=null && dummyNbtTag.size()>0)
						RecipeHammerUpgrades.class.cast(parser.currentRecipe).addAllNbtTag(dummyNbtTag);
					RecipeHammerUpgrades.class.cast(parser.currentRecipe).setEnchantBook(isEnchantBook);
					dummyNbtTag = null;
	        	}
				isEnchantBook = false;
		    }
			return false;
		}

		@Override
		public boolean startElement(String uri, String localName, String qName, Attributes attributes, RecipeParser parser)throws SAXException {
			
			if(ELEMENT_HAMMER.equals(localName)){
		        if(parser.currentRecipe!=null){
		        	isEnchantBook = parser.currentRecipe.getUid().equals("Enchanted Books");
		        	inHammerTag = true;
		        	dummyNbtTag = new ArrayList<NbtTagToAdd>();
		        }
			}
			if(RecipeParser.ELEMENT_NBT.equals(localName)){
				if(!inHammerTag){
					return false;
				}
				if(parser.classRecipe == getClassOfRecipe()){
					dummyNbtTag.add(getNbtTag(attributes));
	        	}
		    }
			return inHammerTag;
		}
		
		public NbtTagToAdd getNbtTag(Attributes attributes){
			NbtTagToAdd tag = null;
			String type = RecipeParser.getStringValue(RecipeParser.AT_NBT_TYPE, attributes, null);
		    String name = RecipeParser.getStringValue(RecipeParser.AT_NBT_NAME, attributes, null);
		    String value = RecipeParser.getStringValue(RecipeParser.AT_NBT_VALUE, attributes, null);
		    String process = RecipeParser.getStringValue(RecipeParser.AT_NBT_PROCESS, attributes, "default");
		    if(type==null || name==null || value==null)
		    	return tag;
		    tag = new NbtTagToAdd(type, value, name, process);	
			return tag;
		}
		
		@Override
		public void process() {
			
		}
	
	}
}
