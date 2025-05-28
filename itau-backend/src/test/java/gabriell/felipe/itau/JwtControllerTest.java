package gabriell.felipe.itau;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.google.common.hash.Hashing;

import gabriell.felipe.itau.controller.JwtController;
import gabriell.felipe.itau.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
public class JwtControllerTest {
	
	private final String SECRET = "itau_jwt";
	private final String SECRET_ENCODED = Base64.getEncoder().encodeToString(Hashing.sha256().hashString(SECRET, StandardCharsets.UTF_8).asBytes());
	
	@MockBean
	private JwtService jwtService;
	
	@Autowired
	private JwtController jwtController;
	

	@Test
	public void testValidJwtFalse() throws Exception {
		String jwt = Jwts.builder().claim("Name", "JohnDoe").claim("Role", "Admin").claim("Seed", 7)
				.signWith(SignatureAlgorithm.HS256, SECRET_ENCODED).compact();
		
		when(jwtService.validate(jwt)).thenReturn(Boolean.TRUE);
		assertTrue(jwtController.validate(jwt).getBody());
	}
	
	@Test
	public void testValidJwtTrue() throws Exception {
		String jwt = Jwts.builder().claim("Name", "JohnDoe").claim("Role", "Admin").claim("Seed", 7)
				.signWith(SignatureAlgorithm.HS256, SECRET_ENCODED).compact();
		
		when(jwtService.validate(jwt)).thenReturn(Boolean.FALSE);
		assertFalse(jwtController.validate(jwt).getBody());
	}

}
