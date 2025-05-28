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

import gabriell.felipe.itau.service.JwtService;
import gabriell.felipe.itau.service.PrimeNumberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
public class JwtServiceTest {
	
	private final String SECRET = "itau_jwt";
	private final String SECRET_ENCODED = Base64.getEncoder().encodeToString(Hashing.sha256().hashString(SECRET, StandardCharsets.UTF_8).asBytes());
	
	@Autowired
	private JwtService jwtService;
	
	@MockBean
	private PrimeNumberService primeNumberService;

	@Test
	public void testValidJwtFalse() throws Exception {
		String jwt = Jwts.builder().claim("Name", "JohnDoe").claim("Role", "Admin").claim("Seed", 7)
				.signWith(SignatureAlgorithm.HS256, SECRET_ENCODED).compact();
		when(primeNumberService.isPrimeNumber(7)).thenReturn(Boolean.TRUE);
		assertTrue(jwtService.validate(jwt));
	}
	
	@Test
	public void testValidRoleInvalid() throws Exception {
		String jwt = Jwts.builder().claim("Name", "JohnDoe").claim("Role", "Admi").claim("Seed", 7)
				.signWith(SignatureAlgorithm.HS256, SECRET_ENCODED).compact();
		
		assertFalse(jwtService.validate(jwt));
	}
	
	@Test
	public void testValidSeedInvalid() throws Exception {
		String jwt = Jwts.builder().claim("Name", "JohnDoe").claim("Role", "Admin").claim("Seed", 6)
				.signWith(SignatureAlgorithm.HS256, SECRET_ENCODED).compact();
		
		when(primeNumberService.isPrimeNumber(6)).thenReturn(Boolean.FALSE);
		assertFalse(jwtService.validate(jwt));
	}
	
	@Test
	public void testValidNameInvalid() throws Exception {
		String jwt = Jwts.builder().claim("Name", "JohnD22oe").claim("Role", "Admin").claim("Seed", 7)
				.signWith(SignatureAlgorithm.HS256, SECRET_ENCODED).compact();
		
		assertFalse(jwtService.validate(jwt));
	}
	
	@Test
	public void testInvalidJWTClains() throws Exception {
		String jwt = Jwts.builder().claim("Name", "JohnDoe").claim("Role", "Admin").claim("Seed", 7).claim("EXP", 7)
				.signWith(SignatureAlgorithm.HS256, SECRET_ENCODED).compact();
		
		assertFalse(jwtService.validate(jwt));
	}

}
