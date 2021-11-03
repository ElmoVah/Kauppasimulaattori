package dao;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import simu.model.PalvelupisteTyyppi;
import simu.model.KassaTyyppi;

class SimulaationTuloksetTest {

	private SimulaationTulokset stulos;
	SimpleDateFormat muokkain = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	@Test
	@DisplayName("Parametrittoman Konstruktorin testaus") 
	void testParametritonSimultaationTulokset() {
		try {
			stulos = new SimulaationTulokset();
		} catch (Exception e) {
			fail("Parametrittoman SimulaationTulokset-olion luonti ei onnistunut");
		}
	}

	@Test
	@DisplayName("Parametrillisen Konstruktorin testaus") 
	void testParametrilleneSimulaationTulokset() {
		try {
			stulos = new SimulaationTulokset(100, 1, 2, 2);
		} catch (Exception e) {
			fail("Parametrillsen SimulaationTulokset-olion luonti ei onnistunut");
		}
	}
	
	@Test
	@DisplayName("Päivämäärän testaus")
	void testPvmSimultaationTulokset() {
		stulos = new SimulaationTulokset(100, 1, 2, 2);
		Date date = new Date();
		String pvm = muokkain.format(date);
		assertTrue("Päivämäärät eivät ole samat", pvm.equals(stulos.getPvm()));
	}
	
	@Test
	@DisplayName("RuuhkaStringiksi metodin testaus")
	void testRuuhkaStringiksi() {
		stulos = new SimulaationTulokset(100, 1, 2, 0);
		assertTrue("Ei palauttanut hiljaista", stulos.getRuuhka().equals("Hiljaista"));
		
		stulos = new SimulaationTulokset(100, 1, 2, 1);
		assertTrue("Ei palauttanut hiljaista", stulos.getRuuhka().equals("Normaalia"));
		
		stulos = new SimulaationTulokset(100, 1, 2, 2);
		assertTrue("Ei palauttanut hiljaista", stulos.getRuuhka().equals("Ruuhkaa"));
	}
	
	@Test
	@DisplayName("LisaaPalvelupisteenTulos metodin testaus")
	void testLisaaPalvelupisteenTulo() {
		stulos = new SimulaationTulokset(100, 1, 2, 0);
		stulos.lisaaPalvelupisteenTulos(new PalvelupisteenTulos(PalvelupisteTyyppi.HAAHUILU, 1, 1, 1, 1, 1, 1));
		assertTrue("Kassojen määrän ei olisi pitänyt muuttua", stulos.getNormKassaLkm() + stulos.getPikaKassaLkm() + stulos.getIpKassaLkm() == 0);
		assertTrue("Set<PalveluPisteen> tulos ei ollut 0", stulos.getPalvelupisteet().size() == 0);
		
		stulos.lisaaPalvelupisteenTulos(new PalvelupisteenTulos(PalvelupisteTyyppi.LIHATISKI, 1, 1, 1, 1, 1, 1));
		assertTrue("Kassojen määrän ei olisi pitänyt muuttua", stulos.getNormKassaLkm() + stulos.getPikaKassaLkm() + stulos.getIpKassaLkm() == 0);
		assertTrue("Set<PalveluPisteen> tulos ei ollut 1", stulos.getPalvelupisteet().size() == 1);
		
		stulos.lisaaPalvelupisteenTulos(new PalvelupisteenTulos(PalvelupisteTyyppi.KASSA, 1, 1, 1, 1, 1, KassaTyyppi.NORMAALI, 1));
		assertTrue("Normaalikassojen määrä on väärä", stulos.getNormKassaLkm() == 1);
		assertTrue("Muiden kassojen yhteismäärän pitisi 3", stulos.getNormKassaLkm() + stulos.getPikaKassaLkm() + stulos.getIpKassaLkm() == 1);
		assertTrue("Set<PalveluPisteen> tulos ei ollut 2", stulos.getPalvelupisteet().size() == 2);
		
		stulos.lisaaPalvelupisteenTulos(new PalvelupisteenTulos(PalvelupisteTyyppi.KASSA, 1, 1, 1, 1, 1, KassaTyyppi.PIKA, 1));
		assertTrue("Normaalikassojen määrä on väärä", stulos.getPikaKassaLkm() == 1);
		assertTrue("Muiden kassojen yhteismäärän pitäisi olla 2", stulos.getNormKassaLkm() + stulos.getPikaKassaLkm() + stulos.getIpKassaLkm() == 2);
		assertTrue("Set<PalveluPisteen> tulos ei ollut 3", stulos.getPalvelupisteet().size() == 3);
		
		stulos.lisaaPalvelupisteenTulos(new PalvelupisteenTulos(PalvelupisteTyyppi.KASSA, 1, 1, 1, 1, 1, KassaTyyppi.ITSEPALVELU, 1));
		assertTrue("Normaalikassojen määrä on väärä", stulos.getIpKassaLkm() == 1);
		assertTrue("Muiden kassojen yhteismäärän pitäisi olla 3", stulos.getNormKassaLkm() + stulos.getPikaKassaLkm() + stulos.getIpKassaLkm() == 3);
		assertTrue("Set<PalveluPisteen> tulos ei ollut 4", stulos.getPalvelupisteet().size() == 4);
	}
}
