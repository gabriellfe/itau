package gabriell.felipe.itau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import gabriell.felipe.itau.dto.JwtRequestDTO;
import gabriell.felipe.itau.dto.JwtResponseDTO;
import gabriell.felipe.itau.service.JwtService;
import jakarta.validation.Valid;

@RestController(value = "/jwt")
public class JwtController {
	
	@Autowired
	private JwtService jwtService; 
	
	@PostMapping(value = "/generate")
	public ResponseEntity<JwtResponseDTO> generate(@Valid @RequestBody JwtRequestDTO jwtRequestDTO){
		return ResponseEntity.ok(jwtService.generate(jwtRequestDTO));
	}
	
	@PostMapping(value = "/validate")
	public ResponseEntity<Boolean> validate(@RequestHeader(name = "Authorization", required = true) String authorization) throws Exception {
		return ResponseEntity.ok(jwtService.validate(authorization));
	}
	
	@PostMapping(value = "/validate/secret")
	public ResponseEntity<Boolean> validateWithSecret(@RequestHeader(name = "Authorization", required = true) String authorization) throws Exception {
		return ResponseEntity.ok(jwtService.validateWithSecret(authorization));
	}

}
