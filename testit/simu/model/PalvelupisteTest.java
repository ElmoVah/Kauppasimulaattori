package simu.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Tapahtumalista;

public class PalvelupisteTest {
	
	private ContinuousGenerator generaattori = new Normal(60, 10);
	private Tapahtumalista tapahtumalista = new Tapahtumalista();
	private Palvelupiste lihatiski;

	
	@BeforeEach
	private void setUp() {
		lihatiski = new Palvelupiste(generaattori, tapahtumalista, TapahtumanTyyppi.LIHATISKIPOISTUMINEN, PalvelupisteTyyppi.LIHATISKI);
	}
	
	@Test
	@DisplayName("Palvelupisteen luominen")
	public void palvelupisteenLuominen() {
		try {
			Palvelupiste kalatiski = new Palvelupiste(generaattori, tapahtumalista, TapahtumanTyyppi.KALATISKIPOISTUMINEN, PalvelupisteTyyppi.KALATISKI);
		} catch(Exception e) {
			fail();
		}
	}
	
	@Test
	@DisplayName("Jonon pituuden tarkistaminen")
	public void jononPituus() {
		for(int i = 0; i < 5; i++) {
			lihatiski.lisaaJonoon(new Asiakas(50));
		}
		assertEquals(lihatiski.getJononPituus(), 5, "Jonon pituuden tarkistaminen ei toimi oikein");
	}
	
	@Test
	@DisplayName("Asiakkaan jonoon lisääminen")
	public void jonoonLisaaminen() {
		Asiakas a = new Asiakas(95);
		lihatiski.lisaaJonoon(a);
		assertEquals(lihatiski.otaJonosta(), a, "Jonoon lisääminen ei toimi oikein");
	}
	
	@Test
	@DisplayName("Palvelupisteen varaaminen palvelun aikana")
	public void palvelunAloittaminen() {
		Asiakas a = new Asiakas(95);
		lihatiski.lisaaJonoon(a);
		lihatiski.aloitaPalvelu();
		assertTrue(lihatiski.onVarattu(), "Palvelupiste ei ole varattu palvelun aikana");
	}
	
	@Test
	@DisplayName("Palvelupisteeltä asiakkaan poistaminen")
	public void jonostaPoistaminen() {
		for(int i = 0; i < 5; i++) {
			lihatiski.lisaaJonoon(new Asiakas(50));
		}
		lihatiski.otaJonosta();
		assertEquals(lihatiski.getJononPituus(), 4, "Asiakkaan poistaminen jonosta ei onnistu");
	}
}
