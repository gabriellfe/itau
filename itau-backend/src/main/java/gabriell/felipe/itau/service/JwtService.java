package gabriell.felipe.itau.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;

import gabriell.felipe.itau.constant.Constant;
import gabriell.felipe.itau.constant.RoleEnum;
import gabriell.felipe.itau.dto.JwtRequestDTO;
import gabriell.felipe.itau.dto.JwtResponseDTO;
import gabriell.felipe.itau.dto.UserDTO;
import gabriell.felipe.itau.exception.JwtException;
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
		this.validateJwt(user);
		return Boolean.TRUE;
	}

	public Boolean validateWithSecret(String authorization) throws Exception {
		try {
			log.info("Initialize validateJwtWithSecret: [{}]", authorization);
			UserDTO user = this.decodeJwt(authorization);
			this.validateJwt(user);
			JWT.require(Algorithm.HMAC256(SECRET_ENCODED)).build().verify(authorization);
		} catch (JWTVerificationException exception) {
			log.error("Error: [{}]", exception);
			throw new JwtException(Constant.INVALID_JWT_0001_MESSAGE, Constant.INVALID_JWT_0001);
		} catch (Exception exception) {
			log.error("Error: [{}]", exception);
			throw exception;
		}
		return true;
	}
	
    public static DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }
    
    private UserDTO decodeJwt(String authorization) throws Exception {
    	String base64EncodedBody = authorization.split("\\.")[1];
		String body = new String(Base64.getDecoder().decode(base64EncodedBody));
		ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		UserDTO user = objectMapper.readValue(body, UserDTO.class);
		log.info("Decoded UserDTO: [{}]", objectMapper.writeValueAsString(user));
		return user;
	}

	public boolean isAlpha(String name) {
	    return name.matches("[a-zA-Z]+");
	}
	
	private void validateJwt(UserDTO user) {
		if (!RoleEnum.getAllValues().contains(user.getRole())) {
			throw new JwtException(Constant.INVALID_JWT_0003_MESSAGE, Constant.INVALID_JWT_0003);
		}
		if (!isAlpha(user.getName().replaceAll("\\s", ""))) {
			throw new JwtException(Constant.INVALID_JWT_0002_MESSAGE, Constant.INVALID_JWT_0002);
		}
		if (user.getName().length() > 256) {
			throw new JwtException(Constant.INVALID_JWT_0002_MESSAGE, Constant.INVALID_JWT_0002);
		}
		if (!primeNumberService.isPrimeNumber(Integer.parseInt(user.getSeed()))) {
			throw new JwtException(Constant.INVALID_JWT_0004_MESSAGE, Constant.INVALID_JWT_0004);
		}
	}

}
