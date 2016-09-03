package noelflantier.sfartifacts.common.recipes;

public class RecipeMold  extends RecipeBase{

	private int[] tabShape = new int[9];
	private int moldMeta = 0;

	public RecipeMold(String uid) {
		super(uid);
	}


	public int getMoldMeta() {
		return moldMeta;
	}
	public void setMoldMeta(int moldMeta) {
		this.moldMeta = moldMeta;
	}
	public int[] getTabShape() {
		return tabShape;
	}
	public void setTabShape(int[] tabShape) {
		this.tabShape = tabShape;
	}

	public void addLineShape(int index, int shape) {
		this.tabShape[index] = shape;
	}

	public boolean isShapeEquals(int[] shape) {
		if(this.tabShape.length!=shape.length)
			return false;
		for(int i = 0 ; i<this.tabShape.length ; i++){
			if(this.tabShape[i]!=shape[i])
				return false;
		}
		return true;
	}

}
