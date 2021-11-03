package simu.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;
import simu.framework.Tapahtumalista;

public class KassaTesti {
	
	private ContinuousGenerator generaattori = new Normal(60, 10);
	private Tapahtumalista tapahtumalista = new Tapahtumalista();
	private ArrayList<Kassa> kassat;
	private Kassa normikassa;
	private Kassa pikakassa;
	private Kassa ipkassa;
	
	@BeforeEach
	public void setUp() {
		normikassa = new Kassa(generaattori, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN, KassaTyyppi.NORMAALI, PalvelupisteTyyppi.KASSA);
		ipkassa = new Kassa(generaattori, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN, KassaTyyppi.ITSEPALVELU, PalvelupisteTyyppi.KASSA);
		pikakassa = new Kassa(generaattori, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN, KassaTyyppi.PIKA, PalvelupisteTyyppi.KASSA);
		kassat = new ArrayList<Kassa>();
		Collections.addAll(kassat, normikassa, pikakassa, ipkassa);
	}
	
	@Test
	@DisplayName("Kassan luonti")
	public void luoKassa() {
		try {
			normikassa = new Kassa(generaattori, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN, KassaTyyppi.NORMAALI, PalvelupisteTyyppi.KASSA);
			ipkassa = new Kassa(generaattori, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN, KassaTyyppi.ITSEPALVELU, PalvelupisteTyyppi.KASSA);
			pikakassa = new Kassa(generaattori, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN, KassaTyyppi.PIKA, PalvelupisteTyyppi.KASSA);
		} catch(Exception e) {
			fail("Kassojen luonti ei onnistunut");
		}
	}
	
	@Test
	@DisplayName("Lyhimmän jonon valitseminen")
	public void lyhinJono() {
		for(int i = 0; i < 5; i++) {
			normikassa.lisaaJonoon(new Asiakas(50));
			ipkassa.lisaaJonoon(new Asiakas(50));
		}
		Collections.sort(kassat);
		assertEquals(pikakassa, kassat.get(0), "Lyhimmän jonon valitseminen ei toimi");
	}
}
