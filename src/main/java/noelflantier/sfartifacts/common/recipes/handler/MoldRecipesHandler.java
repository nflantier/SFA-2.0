package noelflantier.sfartifacts.common.recipes.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import noelflantier.sfartifacts.common.recipes.AUsageRecipes;
import noelflantier.sfartifacts.common.recipes.CustomHandler;
import noelflantier.sfartifacts.common.recipes.RecipeBase;
import noelflantier.sfartifacts.common.recipes.RecipeMold;
import noelflantier.sfartifacts.common.recipes.RecipeParser;

public class MoldRecipesHandler extends AUsageRecipes{

	public static final String FILE_NAME_MOLD = "MoldRecipes.xml";
	public static final String USAGE_MOLD = "Mold";
	
	public MoldRecipesHandler() {
		super(FILE_NAME_MOLD, USAGE_MOLD);
	}

	static final MoldRecipesHandler instance = new MoldRecipesHandler();
	public static MoldRecipesHandler getInstance() {
		return instance;
	}

	@Override
	public Class<? extends RecipeBase> getClassOfRecipe() {
		return RecipeMold.class;
	}

	@Override
	public CustomHandler createCustomHandler(){
		return new MoldCustomHandler();
	}
	
	@Override
	public void loadRecipes() {
		super.loadRecipes();
	}

	private class MoldCustomHandler implements CustomHandler {

		private static final String ELEMENT_MOLD = "mold";
		private static final String ELEMENT_MOLDLINE = "moldline";
		private static final String AT_SHAPE = "shape";
		private static final String AT_META = "itemMeta";
		private int index = 0;
		
		@Override
		public boolean endElement(String uri, String localName, String qName, RecipeParser parser) throws SAXException {
			if(ELEMENT_MOLD.equals(localName)){
				index = 0;
			}
			return false;
		}

		@Override
		public boolean startElement(String uri, String localName, String qName, Attributes attributes, RecipeParser parser) throws SAXException {
			if(ELEMENT_MOLD.equals(localName)){
				if(parser.currentRecipe!=null && parser.classRecipe == getClassOfRecipe()){
					RecipeMold.class.cast(parser.currentRecipe).setMoldMeta(RecipeParser.getIntValue(AT_META, attributes, 0));
				}
			}
			if(ELEMENT_MOLDLINE.equals(localName)){
				if(parser.currentRecipe!=null && parser.classRecipe == getClassOfRecipe() && index<9){
					RecipeMold.class.cast(parser.currentRecipe).addLineShape(index, Integer.parseInt(RecipeParser.getStringValue(AT_SHAPE, attributes, "000000000"), 2));
					index+=1;
				}
			}
			return false;
		}

		@Override
		public void process() {
		}
		
	}
}
