package gabriell.felipe.itau.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import gabriell.felipe.itau.constant.RoleEnum;
import gabriell.felipe.itau.dto.JwtRequestDTO;
import gabriell.felipe.itau.dto.JwtResponseDTO;
import gabriell.felipe.itau.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {
	
	private final String SECRET = "itau_jwt";
	private final String SECRET_ENCODED = Base64.getEncoder().encodeToString(Hashing.sha256().hashString(SECRET, StandardCharsets.UTF_8).asBytes());
	
	@Autowired
	private PrimeNumberService primeNumberService;

	public JwtResponseDTO generate(JwtRequestDTO jwtRequestDTO) {
		JWTCreator.Builder jwtBuilder = JWT.create();
		Algorithm algo = Algorithm.HMAC256(SECRET_ENCODED);
		log.info("Success on client login [{}]", jwtRequestDTO.getName());
		String jwt = jwtBuilder
				.withClaim("Name", jwtRequestDTO.getName())
				.withClaim("Seed", primeNumberService.generatePrimeNumber(9999))
				.withClaim("Role", jwtRequestDTO.getRole())
				.sign(algo);
		return new JwtResponseDTO(jwt);
	}

	public Boolean validate(String authorization) throws Exception {
		log.info("Initialize validateJwt");
		UserDTO user = this.decodeJwt(authorization);
		return this.validateJwt(user);
	}

	public Boolean validateWithSecret(String authorization) throws Exception {
		try {
			log.info("Initialize validateJwtWithSecret: [{}]", authorization);
			UserDTO user = this.decodeJwt(authorization);
			this.validateJwt(user);
			JWT.require(Algorithm.HMAC256(SECRET_ENCODED)).build().verify(authorization);
		} catch (Exception exception) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
    public static DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }
    
    private UserDTO decodeJwt(String authorization) throws Exception {
    	UserDTO user = null;
    	try {
    		String base64EncodedBody = authorization.split("\\.")[1];
    		String body = new String(Base64.getDecoder().decode(base64EncodedBody));
    		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		user = objectMapper.readValue(body, UserDTO.class);
    		log.info("Decoded UserDTO: [{}]", objectMapper.writeValueAsString(user));
		} catch (Exception e) {
			log.error("Error to decode JWT [{}]", e);
		}
		return user;
	}

	public boolean isAlpha(String name) {
	    return name.matches("[a-zA-Z]+");
	}
	
	private Boolean validateJwt(UserDTO user) {
		Boolean result = Boolean.TRUE;
		
		if (user == null) {
			return Boolean.FALSE; 
		}
		if (!RoleEnum.getAllValues().contains(user.getRole())) {
			result = Boolean.FALSE;
		}
		if (!isAlpha(user.getName().replaceAll("\\s", ""))) {
			result = Boolean.FALSE;
		}
		if (user.getName().length() > 256) {
			result = Boolean.FALSE;
		}
		if (!primeNumberService.isPrimeNumber(Integer.parseInt(user.getSeed()))) {
			result = Boolean.FALSE;
		}
		return result;
	}

}
