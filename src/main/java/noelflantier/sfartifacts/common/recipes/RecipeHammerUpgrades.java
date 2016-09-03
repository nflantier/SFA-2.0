package noelflantier.sfartifacts.common.recipes;

import java.util.ArrayList;
import java.util.List;

public class RecipeHammerUpgrades extends RecipeBase{

	private List<NbtTagToAdd> listNbt = new ArrayList<NbtTagToAdd>();
	private boolean isEnchantBook = false;

	public RecipeHammerUpgrades(String uid) {
		super(uid);
	}
	
	public List<NbtTagToAdd> getNbtTagList(){
		return this.listNbt;
	}
	public void addNbtTag(NbtTagToAdd t){
		this.listNbt.add(t);
	}
	public void addAllNbtTag(List<NbtTagToAdd> dummyNbtTag) {
		this.listNbt.addAll(dummyNbtTag);
	}
	
	public boolean isEnchantBook() {
		return isEnchantBook;
	}
	public void setEnchantBook(boolean isEnchantBook) {
		this.isEnchantBook = isEnchantBook;
	}
	
	public static class NbtTagToAdd{
		public String type;
		public String value;
		public String name;
		public String process;
		
		public NbtTagToAdd(String type, String value, String name, String process){
			this.type = type;
			this.value = value;
			this.name = name;
			this.process = process;
		}
		
	}
}
