package noelflantier.sfartifacts.client.gui.bases;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

public abstract class GuiComponentBase {

	public int x;
	public int y;
	public int width;
	public int height;
	public static Gui GUIH = new Gui();
    public static FontRenderer FR = Minecraft.getMinecraft().fontRendererObj;
	
	public GuiComponentBase(int x, int y){
		this.x = x;
		this.y = y;
	}
	public GuiComponentBase(int x, int y, int w, int  h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public abstract void draw(int x, int y);
	public abstract boolean isMouseHover(int mx, int my);
}
