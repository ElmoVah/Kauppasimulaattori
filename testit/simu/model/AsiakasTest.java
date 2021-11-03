package simu.model;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class AsiakasTest {

	private Asiakas asiakas;
	
	@BeforeEach
	public void setUp() {
		Asiakas.seuraavaId = 1;
	}
	
	@Test
	@DisplayName("Asiakkaan ID:n inkrementointi")
	public void asiakkaanId() {
		for(int i = 0; i < 5; i++) {
			@SuppressWarnings("unused")
			Asiakas a = new Asiakas(50);
		}
		Asiakas a = new Asiakas(50);
		assertEquals(6, a.getId(), "Asiakkaan ID:n inkrementointi ei toimi");
	}
	
	@Test
	@DisplayName("Asiakkaan luonti")
	public void testAsiakas() {
		try {
			asiakas = new Asiakas(50.0);
		} catch (Exception e) {
			fail("Asiakas-olion luonti ei onnistunut");
		}
	}
	
	@Test 
	@DisplayName("Korttimaksun todennäköisyys nolla")
	public void testKorttimaksuArvollaNollaOnFalse() {
		asiakas = new Asiakas(0);
		assertTrue("Korttimaksun arvo ei ollut false", !asiakas.isKorttimaksu());
	}
	
	@Test 
	@DisplayName("Korttimaksun todennäköisyys sata")
	public void testKorttimaksuArvollaSataOnTrue() {
		asiakas = new Asiakas(100);
		assertTrue("Korttimaksun arvoe ei ollut True", asiakas.isKorttimaksu());
	}
	

}
