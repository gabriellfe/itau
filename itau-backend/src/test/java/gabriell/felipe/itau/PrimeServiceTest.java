package gabriell.felipe.itau;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import gabriell.felipe.itau.service.PrimeNumberService;

@SpringBootTest
public class PrimeServiceTest {
	
	@Autowired
	private PrimeNumberService primeNumberService;
	
	@Test
	public void testInvalidPrimeNumber() throws Exception {
		assertFalse(primeNumberService.isPrimeNumber(999));
	}
	
	@Test
	public void testValidPrimeNumber() throws Exception {
		assertTrue(primeNumberService.isPrimeNumber(7));
	}
	@Test
	public void testGenerateValidPrimeNumber() throws Exception {
		assertTrue(primeNumberService.isPrimeNumber(primeNumberService.generatePrimeNumber(8888)));
	}


}
