package noelflantier.sfartifacts.common.handlers;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import noelflantier.sfartifacts.Ressources;

public class ModKeyBindings {

	public static KeyBinding hammerConfig;
	
	public static void loadBindings(){
    	hammerConfig = new KeyBinding("Hammer Config Panel", Keyboard.KEY_H, Ressources.MODID);
    	ClientRegistry.registerKeyBinding(hammerConfig);
	}
}
