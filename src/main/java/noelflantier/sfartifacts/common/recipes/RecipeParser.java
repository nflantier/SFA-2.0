package noelflantier.sfartifacts.common.recipes;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.common.handlers.ModConfig;
import noelflantier.sfartifacts.common.handlers.RecipeHandler;
import noelflantier.sfartifacts.common.helpers.ItemNBTHelper;

public class RecipeParser extends DefaultHandler {

	public static final String ELEMENT_ROOT_RECIPES = "recipes";
	public static final String ELEMENT_RECIPE = "recipe";
	public static final String ELEMENT_INPUT = "input";
	public static final String ELEMENT_OUTPUT = "output";
	public static final String ELEMENT_ITEM_STACK = "itemStack";
	public static final String ELEMENT_FLUID_STACK = "fluidStack";
	
	public static final String ELEMENT_ITEM_STACK_WITH_NBT = "itemStackWithNbt";
	public static final String ELEMENT_NBT = "nbt";
	public static final String AT_NBT_TYPE = "nbtType";
	public static final String AT_NBT_NAME = "nbtName";
	public static final String AT_NBT_VALUE = "nbtValue";
	public static final String AT_NBT_PROCESS = "nbtProcess";

	public static final String AT_HAS_OUTPUTS = "hasOutputs";
	public static final String AT_NAME = "name";
	public static final String AT_LABEL = "label";
	public static final String AT_ENERGY_COST = "energyCost";
	public static final String AT_FLUID_COST = "fluidCost";
	public static final String AT_ORE_DICT = "oreDictionary";
	public static final String AT_ITEM_DAMAGE = "itemMeta";
	public static final String AT_QUANTITY = "itemQuantity";
	public static final String AT_ITEM_NAME = "itemName";
	public static final String AT_MOD_ID = "modID";
	public static final String AT_AMOUNT = "amount";
	

	public static RecipeHandler parse(String str, CustomHandler customHandler, Class<? extends RecipeBase> classRecipe) throws Exception {

		StringReader reader = new StringReader(str);
	    InputSource is = new InputSource(reader);
	    try {
	    	return parse(is, customHandler, classRecipe);
	    } finally {
	    	reader.close();
	    }
	}
	public static RecipeHandler parse(InputSource is, CustomHandler customHandler, Class<? extends RecipeBase> classRecipe) throws Exception {
		RecipeParser parser = new RecipeParser(customHandler, classRecipe);
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setContentHandler(parser);
		xmlReader.parse(is);

		return parser.result;
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		System.out.println("Warning while loading recipes from config : " + e.getMessage());
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		System.out.println("Error while loading recipes from config : " + e.getMessage());
		e.printStackTrace();
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		System.out.println("Error while loading recipes from config : " + e.getMessage());
		e.printStackTrace();
	}

	public RecipeHandler result;
	public RecipeHandler currentRoot;
	public ISFARecipe currentRecipe;
	public boolean outputTagOpen = false;
	public boolean inputTagOpen = false;
	public ItemStack currentItemStack;
	public CustomHandler customHandler = null;
	public Class<? extends RecipeBase> classRecipe;
	
	RecipeParser(CustomHandler customHandler) {
		this.customHandler = customHandler;
		this.classRecipe = RecipeBase.class;
	}

	RecipeParser(CustomHandler customHandler, Class<? extends RecipeBase> classRecipe) {
		this.customHandler = customHandler;
		if(classRecipe==null)
			classRecipe = RecipeBase.class;
		this.classRecipe = classRecipe;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//System.out.println("...................................................... START"+localName);
		if(ELEMENT_ROOT_RECIPES.equals(localName)){
			if(currentRoot != null) {
				System.out.println("Multiple root elements found.");
		    } else {
		    	currentRoot = new RecipeHandler();
		    }
		    return;
		}
		
	    if(currentRoot == null) {
			System.out.println("Result created on the fly.");
			currentRoot = new RecipeHandler();
	    }
	    
	    if(ELEMENT_RECIPE.equals(localName)){
	    	if(currentRoot==null){
	    		System.out.println("No recipes root element");
	    		return;
	    	}
	    	if(currentRecipe != null) {
	    		System.out.println("Recipe " + currentRecipe.getUid() + " not closed before encountering a new recipe.");
	        }
	    	String name = getStringValue(AT_NAME, attributes, null);

			try {
				currentRecipe = classRecipe.getDeclaredConstructor(String.class).newInstance(name);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}

	    	//new RecipeBase(name);
	    	
	    	String ho = getStringValue(AT_HAS_OUTPUTS, attributes, null);
	    	if(ho!=null && ho.equals("false"))
	    		classRecipe.cast(currentRecipe).setHasOuputs(false);
	    	classRecipe.cast(currentRecipe).setLabel(getStringValue(AT_LABEL, attributes, null));
	    	classRecipe.cast(currentRecipe).setEnergyCost((getIntValue(AT_ENERGY_COST, attributes, 0)));
	    	classRecipe.cast(currentRecipe).setFluidCost((getIntValue(AT_FLUID_COST, attributes, 0)));
	    	if(name==null){
	    		System.out.println("Invalid recipe name : "+attributes.getValue(AT_NAME));
	    		currentRecipe = null;
	    	}
	    	return;
	    }

	    
	    if(currentRecipe == null) {
	    	System.out.println("Found element <" + localName + "> with no recipe decleration.");
	        return;
	    }
	    
	    if(customHandler != null) {
	        if(customHandler.startElement(uri, localName, qName, attributes, this)) {
	        	return;
	        }
	    }
	    
	    if(ELEMENT_OUTPUT.equals(localName)) {
	    	if(inputTagOpen) {
	    		System.out.println("<output> encounterd before <input> closed.");
	    		inputTagOpen = false;
	    	}
	    	if(outputTagOpen) {
	    		System.out.println("<output> encounterd before previous <output> closed.");
	    	}
	    	outputTagOpen = true;
	    	return;
	    }
	    
	    if(ELEMENT_INPUT.equals(localName)) {
	    	if(outputTagOpen) {
	    		System.out.println("<ouput> has not been closed.");
	    		outputTagOpen = false;
	    	}
	    	if(inputTagOpen) {
	    		System.out.println("<input> encounterd before previous <input> closed.");
	     	}
	        inputTagOpen = true;
	    	return;
	    }

	    boolean isFluidStack = ELEMENT_FLUID_STACK.equals(localName);
	    if(ELEMENT_ITEM_STACK.equals(localName)  || isFluidStack) {
	        if(!inputTagOpen && !outputTagOpen) {
	        	System.out.println("Encounterd an item stack outside of either an <input> or <output> tag.");
	          return;
	        }
	        if(inputTagOpen && outputTagOpen) {
	        	System.out.println("Encounterd an item stack within both an <input> and <output> tag.");
	        	return;
	        }
	        if(inputTagOpen) {
	            if(isFluidStack){ 
	            	classRecipe.cast(currentRecipe).addInputFluidStack(getFluidStack(attributes));
	            }else{
	            	classRecipe.cast(currentRecipe).addInputStack(getItemStack(attributes));
	            }
	        } else {
	            if(isFluidStack){
	            	classRecipe.cast(currentRecipe).addOutputFluidStack(getFluidStack(attributes));
	            }else{
	            	classRecipe.cast(currentRecipe).addOutputStack(getItemStack(attributes));
	            }
	        }
	        return;
	    }
	    
	    if(ELEMENT_ITEM_STACK_WITH_NBT.equals(localName)){
	    	if( currentItemStack!=null ){
	        	System.out.println("Encounterd an itemstack with nbt inside an itemstack with nbt");
	    		currentItemStack = null;
	    	}
	        if(!inputTagOpen && !outputTagOpen) {
	        	System.out.println("Encounterd an item stack with nbt outside of either an <input> or <output> tag.");
	        	return;
	        }
	        if(inputTagOpen && outputTagOpen) {
	        	System.out.println("Encounterd an item stack with nbt within both an <input> and <output> tag.");
	        	return;
	        }

        	currentItemStack = getItemStack(attributes);
        	return;
	    }
	    
	    if(ELEMENT_NBT.equals(localName)){
	    	if(currentItemStack==null){
	        	System.out.println("Encounterd an nbt ouside an itemstack tag.");
	        	return;
	    	}
	    	currentItemStack = getStackWithNbt(attributes, currentItemStack);
        	return;
	    }
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(ELEMENT_ROOT_RECIPES.equals(localName)){
			result = currentRoot;
			currentRoot = null;
		    return;
		}

	    if(customHandler != null) {
	        if(customHandler.endElement(uri, localName, qName, this)) {
	          	return;
	        }
	    }
	    
	    if(ELEMENT_RECIPE.equals(localName)){
	        if(currentRecipe != null) {
	        	currentRoot.addRecipe(currentRecipe);
	        } else {
	        	System.out.println("Could not add recipe " + currentRecipe + " to group root");
	        }
	        currentRecipe = null;
	        return;
	    }
	    
	    if(ELEMENT_OUTPUT.equals(localName)) {
	    	outputTagOpen = false;
	    	return;
	    }
	    if(ELEMENT_INPUT.equals(localName)) {
	    	inputTagOpen = false;
	    	return;
	    }
	    
	    if(ELEMENT_ITEM_STACK_WITH_NBT.equals(localName)){
	    	if(currentItemStack==null){
	        	System.out.println("Could not add stack with nbt");
	    		return;
	    	}
	    	
	        if(inputTagOpen)
	        	classRecipe.cast(currentRecipe).addInputStack(currentItemStack);
	        else
	        	classRecipe.cast(currentRecipe).addOutputStack(currentItemStack);
	           
	    	currentItemStack = null;
	    	return;
	    }
	}

	public ItemStack getStackWithNbt(Attributes attributes, ItemStack stack){
		String type = getStringValue(AT_NBT_TYPE, attributes, null);
	    String name = getStringValue(AT_NBT_NAME, attributes, null);
	    String value = getStringValue(AT_NBT_VALUE, attributes, null);
	    String orename = getStringValue(AT_ORE_DICT, attributes, null);
	    if(orename == null && (type==null || name==null || value==null) )
	    	return stack;
	    
		if(type.equals("boolean")){
			stack = ItemNBTHelper.setBoolean(stack, name, value.equals("true"));
		}else if(type.equals("int")){
			stack = ItemNBTHelper.setInteger(stack, name, Integer.parseInt(value));
		}else if(type.equals("double")){
			stack = ItemNBTHelper.setDouble(stack, name, Double.parseDouble(value));
		}else if(type.equals("long")){
			stack = ItemNBTHelper.setLong(stack, name, Long.parseLong(value));
		}else if(type.equals("string")){
			stack = ItemNBTHelper.setString(stack, name, value);
		}
		return stack;
	}
	
	public FluidStack getFluidStack(Attributes attributes){
		int amount = getIntValue(AT_AMOUNT, attributes, FluidContainerRegistry.BUCKET_VOLUME);
	    String name = getStringValue(AT_ITEM_NAME, attributes, null);
	    String modid = getStringValue(AT_MOD_ID, attributes, null);
	    if(name == null) {
	    	return null;
	    }
	    Fluid fluid = FluidRegistry.getFluid(name);
	    if(fluid == null) {
	    	System.out.println("When parsing recipes could not find fluid with name: "+ name);
	    	return null;
	    }
		return new FluidStack(fluid, amount);
	}
	
	public ItemStack getItemStack(Attributes attributes){
	    ItemStack stack = null;
	    int stackSize = getIntValue(AT_QUANTITY, attributes, 1);
	    int itemMeta = getIntValue(AT_ITEM_DAMAGE, attributes, 0);
	    String modId = getStringValue(AT_MOD_ID, attributes, null);
	    String name = getStringValue(AT_ITEM_NAME, attributes, null);
	    if(ModConfig.useOldRegistration && modId.equals(Ressources.MODID))
	    	name = Ressources.MODID+"_"+name;

	    String orename = getStringValue(AT_ORE_DICT, attributes, null);
	    if(orename!=null && OreDictionary.doesOreNameExist(orename)){
	    	List<ItemStack> ores = OreDictionary.getOres(orename);
	        if(!ores.isEmpty() && ores.get(0) != null) {
	        	stack = ores.get(0).copy();
	        	if(stack!=null){
	        		stack.stackSize = stackSize;
	        		stack.setItemDamage(itemMeta);
	        	}
	        }
	    }
	    
	    if(modId != null && name != null) {
	    	Item i = Item.REGISTRY.getObject(new ResourceLocation(modId,name));
	    	//Item i = GameRegistry.findItem(modId, name);
	    	if(i != null) {
	    		stack = new ItemStack(i, stackSize,itemMeta);
	    	} else {
	    		Block b = Block.REGISTRY.getObject(new ResourceLocation(modId,name));
	    		//Block b = GameRegistry.findBlock(modId, name);
	    		if(b != null) {
	    			stack = new ItemStack(b, stackSize, itemMeta);
	    		}
	    	}	
	    }
	    if(stack == null) {
	    	System.out.println("Could not create an item stack");
	        return null;
	    }
		return stack;
	}
	
	public static boolean getBooleanValue(String qName, Attributes attributes, boolean def) {
		String val = attributes.getValue(qName);
	    if(val == null) {
	    	return def;
	    }
	    val = val.toLowerCase().trim();
	    return val.equals("false") ? false : val.equals("true") ? true : def;
	}

	public static int getIntValue(String qName, Attributes attributes, int def) {
		try {
			return Integer.parseInt(getStringValue(qName, attributes, def + ""));
		} catch (Exception e) {
			System.out.println("Could not parse a valid int for attribute " + qName + " with value " + getStringValue(qName, attributes, null));
			return def;
		}
	}

	public static float getFloatValue(String qName, Attributes attributes, float def) {
		try {
			return Float.parseFloat(getStringValue(qName, attributes, def + ""));
		} catch (Exception e) {
			System.out.println("Could not parse a valid float for attribute " + qName + " with value " + getStringValue(qName, attributes, null));
			return def;
		}
	}

	public static String getStringValue(String qName, Attributes attributes, String def) {
		String val = attributes.getValue(qName);
		if(val == null) {
			return def;
		}
		val = val.trim();
		if(val.length() <= 0) {
			return null;
		}
		return val;
	}
}
