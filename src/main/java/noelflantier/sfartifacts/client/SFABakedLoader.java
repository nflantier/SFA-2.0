package noelflantier.sfartifacts.client;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import noelflantier.sfartifacts.Ressources;
import noelflantier.sfartifacts.client.model.ExampleModel;
import noelflantier.sfartifacts.client.model.InductorModel;

public class SFABakedLoader implements ICustomModelLoader {

	    public static Map<String, IModel> nameToModel = new HashMap<String, IModel>(){{
	    	put("bakedmodelblock", new ExampleModel());
	    }};

	    @Override
	    public boolean accepts(ResourceLocation modelLocation) {
	        return modelLocation.getResourceDomain().equals(Ressources.MODID) && nameToModel.containsKey(modelLocation.getResourcePath());
	    }

	    @Override
	    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
	        return nameToModel.get(modelLocation.getResourcePath());
	    }

	    @Override
	    public void onResourceManagerReload(IResourceManager resourceManager) {

	    }
	}
