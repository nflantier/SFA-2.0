package noelflantier.sfartifacts.client.gui.bases;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonSFA extends GuiButton{

	public boolean pressed = false;
	
    public GuiButtonSFA(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_, String p_i1020_4_)
    {
        this(p_i1020_1_, p_i1020_2_, p_i1020_3_, 200, 20, p_i1020_4_);
    }
	public GuiButtonSFA(int p_i1021_1_, int p_i1021_2_, int p_i1021_3_, int p_i1021_4_, int p_i1021_5_, String p_i1021_6_) {
		super(p_i1021_1_, p_i1021_2_, p_i1021_3_, p_i1021_4_, p_i1021_5_, p_i1021_6_);
	}

	@Override
    public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_){
		this.pressed = true;
		return super.mousePressed(p_146116_1_, p_146116_2_, p_146116_3_);
	}
	@Override
    public void mouseReleased(int p_146118_1_, int p_146118_2_) {
    	this.pressed = false;
    }
} 
