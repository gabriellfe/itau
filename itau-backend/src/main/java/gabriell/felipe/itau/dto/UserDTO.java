package gabriell.felipe.itau.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	
	@JsonProperty(value = "Name")
	private String name;
	@JsonProperty(value = "Role")
	private String role;
	@JsonProperty(value = "Seed")
	private String seed;

}