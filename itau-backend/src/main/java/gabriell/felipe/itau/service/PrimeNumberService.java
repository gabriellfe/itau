package gabriell.felipe.itau.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class PrimeNumberService {
	
	public int generatePrimeNumber(int limiteSuperior) {
		Random random = new Random();
		int numeroPrimo;
		List<Integer> primos = new ArrayList<>();
		for (int i = 1; i <= limiteSuperior; i++) {
			if (this.isPrimeNumber(i)) {
				primos.add(i);
			}
		}
		if (!primos.isEmpty()) {
			numeroPrimo = primos.get(random.nextInt(primos.size()));
			return numeroPrimo;
		} else {
			return -1;
		}
	}

	public boolean isPrimeNumber(int num) {
		if (num <= 1) {
			return false;
		}
		for (int i = 2; i <= Math.sqrt(num); i++) {
			if (num % i == 0) {
				return false;
			}
		}
		return true;
	}

}
