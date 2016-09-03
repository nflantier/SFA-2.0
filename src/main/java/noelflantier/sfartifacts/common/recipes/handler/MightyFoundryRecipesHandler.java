package noelflantier.sfartifacts.common.recipes.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import noelflantier.sfartifacts.common.recipes.AUsageRecipes;
import noelflantier.sfartifacts.common.recipes.CustomHandler;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeMightyFoundry;
import noelflantier.sfartifacts.common.recipes.RecipeParser;

public class MightyFoundryRecipesHandler extends AUsageRecipes{
	public static final String FILE_NAME_MIGHTY_FOUNDRY = "MightyFoundryRecipes.xml";
	public static final String USAGE_MIGHTY_FOUNDRY= "MightyFoundry";
		
	public MightyFoundryRecipesHandler() {
		super(FILE_NAME_MIGHTY_FOUNDRY, USAGE_MIGHTY_FOUNDRY);
	}
	
	public static final MightyFoundryRecipesHandler instance = new MightyFoundryRecipesHandler();
	public static MightyFoundryRecipesHandler getInstance() {
		return instance;
	}

	@Override
	public CustomHandler createCustomHandler(){
		return new MightyFoundryCustomHandler();
	}
	
	@Override
	public void loadRecipes() {
		super.loadRecipes();
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeMightyFoundry.class;
	}

	private class MightyFoundryCustomHandler implements CustomHandler {

	    private boolean init = false;
		public static final String AT_TICKPERITEM = "tickPerItem";
		public static final String AT_GLOBALQUANTITY = "globalQuantity";
	    
		@Override
		public boolean endElement(String uri, String localName, String qName, RecipeParser parser) throws SAXException {

		    if(RecipeParser.ELEMENT_RECIPE.equals(localName)){
		    	init = false;
		    }
			return false;
		}

		@Override
		public boolean startElement(String uri, String localName, String qName, Attributes attributes, RecipeParser parser)throws SAXException {
		    if(RecipeParser.ELEMENT_ITEM_STACK.equals(localName)){
		        if(parser.currentRecipe!=null){
		        	if(parser.classRecipe == getClassOfRecipe()){
		        		String l = RecipeParser.getStringValue(RecipeParser.AT_LABEL, attributes, null);
		        		String n = RecipeParser.getStringValue(RecipeParser.AT_ITEM_NAME, attributes, null);
			        	if((l!=null && l.equals("mold")) || (n!=null && n.equals("itemMold"))){
			        		RecipeMightyFoundry.class.cast(parser.currentRecipe).setMold(parser.getItemStack(attributes));
			        		return true;
			        	}else{
			        		if(!this.init){
			        			RecipeMightyFoundry.class.cast(parser.currentRecipe).setTickPerItem(RecipeParser.getIntValue(AT_TICKPERITEM, attributes, 100));
			        			RecipeMightyFoundry.class.cast(parser.currentRecipe).setItemQuantity(RecipeParser.getIntValue(AT_GLOBALQUANTITY, attributes, 1));
			        			this.init=true;
			        		}
			        	}
		        	}
		        }
		    }
			return false;
		}

		@Override
		public void process() {
			// TODO Auto-generated method stub
			
		}
	
	}
	
}
