package gabriell.felipe.itau.constant;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum RoleEnum {

	ADMIN("Admin"), MEMBER("Member"), EXTERNAL("External");

	private String value;

	RoleEnum(String value) {
		this.value = value;
	}

	public static RoleEnum findByValue(String value) {
		for (RoleEnum roleEnum : RoleEnum.values()) {
			if (roleEnum.value.equalsIgnoreCase(value)) {
				return roleEnum;
			}
		}
		return null;
	}
	
    public static List<String> getAllValues() {
    	List<String> values = new ArrayList<String>();
    	for (RoleEnum roleEnum : RoleEnum.values()) {
    		values.add(roleEnum.getValue());
		}
        return values;
    }
	
}
