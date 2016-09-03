package noelflantier.sfartifacts.common.handlers.capabilities;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class PlayerData implements IPlayerData{
	
	private Map<String, Integer> intMap = new HashMap<String, Integer>(){{
		put("tickMovingWithHammer", 0);
		put("tickToUpdate", 125);
		put("tickHasHulkFleshEffect", 0);
		put("tickJustEatHulkFlesh", 0);
		put("justBlockedAttack", 0);
	}};
	private Map<String, Float> floatMap = new HashMap<String, Float>(){{
		put("changeStep", 0F);
	}};
	private Map<String, Boolean> booleantMap = new HashMap<String, Boolean>(){{
		put("isMovingWithHammer", false);
		put("justStopMoving", false);
		put("justStartMoving", false);
	}};
	
	public PlayerData(){
		
	}

	@Override
	public Map<String, Integer> getIntegerMap() {
		return this.intMap;
	}

	@Override
	public Map<String, Float> getFloatMap() {
		return this.floatMap;
	}

	@Override
	public Map<String, Boolean> getBooleanMap() {
		return this.booleantMap;
	}
}
