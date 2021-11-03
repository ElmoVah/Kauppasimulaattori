package dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import simu.model.PalvelupisteTyyppi;
import simu.model.KassaTyyppi;

class PalvelupisteenTulosTest {
	
	private PalvelupisteenTulos ptulos;

	@Test
	@DisplayName("Parametrittoman Konstruktorin testaus") 
	void testParametritonPalvelupisteenTulos() {
		try {
			ptulos = new PalvelupisteenTulos();
		} catch (Exception e) {
			fail("Parametrittoman PalvelupisteenTulos-olion luonti ei onnistunut");
		}
	}
	
	@Test
	@DisplayName("Ei kassa tyyppisten palvelupisteiden Konstruktorin testaus") 
	void testEiKassaPalvelupisteenTulos() {
		try {
			ptulos = new PalvelupisteenTulos(PalvelupisteTyyppi.LIHATISKI, 1, 1, 1, 1, 1, 1 );
		} catch (Exception e) {
			fail("Ei kassa tyyppisen PalvelupisteenTulos-olion luonti ei onnistunut");
		}
	}
	
	@Test
	@DisplayName("Kassa tyyppisten palvelupisteiden Konstruktorin testaus") 
	void testKassaPalvelupisteenTulos() {
		try {
			ptulos = new PalvelupisteenTulos(PalvelupisteTyyppi.KASSA, 1, 1, 1, 1, 1, KassaTyyppi.NORMAALI, 1 );
		} catch (Exception e) {
			fail("Ei kassa tyyppisen PalvelupisteenTulos-olion luonti ei onnistunut");
		}
	}

}
