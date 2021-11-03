package simu.model;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OstoslistaTest {

	private Ostoslista ostoslista;
	
	@BeforeEach 
	public void setUp() {
		ostoslista = new Ostoslista();
	}
	
	@Test
	void testOstoslista() {
		try {
			ostoslista = new Ostoslista();
		} catch (Exception e) {
			fail("Ostoslista-olion luonti ei onnistunut");
		}
	}
	
	@Test
	public void testOstoslistanKoonAlaraja() {
		int koko = ostoslista.getTuotteita();
		int alaraja = 1;
		
		if(ostoslista.isLihatiski()) alaraja++;
		if(ostoslista.isKalatiski()) alaraja++;
		if(ostoslista.isJuustotiski()) alaraja++;
		
		assertTrue("Ostoslistan koko liian pieni", koko > alaraja);
	}
	
	@Test
	public void testOstoslistanKoonYlaraja() {
		int koko = ostoslista.getTuotteita();
		int ylaraja = 150;
		
		assertTrue("Ostoslistan koko liian pieni", koko < ylaraja);
	}
}
