package controller;

import dao.PalvelupisteenTulos;
import dao.SimulaationTulokset;
import simu.model.KassaTyyppi;
import simu.model.Palvelupiste;

/**
 * 
 * Rajapinta simulaattorin kontrollerille 
 *
 */

public interface IKontrolleri {
	public boolean kaynnistaSimulointi();
	public void nopeuta();
	public void hidasta();
	public void naytaNykyaika(double aika);
	public void ajaLoppuun();
	public void otaKayttoonKaynnistys();
	public void visualisoiAsiakas(int index, int maara);
	public void poistaAsiakas(int index, int maara);
	public void poistaKaikkiJonosta();
	public void asetaIkonit();
	public void asetaRuksit();
	public void naytaNykyviive(long viive);
	public Palvelupiste[] viePalvelupisteet();
	void poistaKassanAsiakas(int index, int maara, KassaTyyppi kassatyyppi);
	void visualisoiKassanAsiakas(int index, int maara, KassaTyyppi kassatyyppi);
	public SimulaationTulokset[] lueTulokset();
	public PalvelupisteenTulos[] luePalvelupisteet(SimulaationTulokset st);
	public void poistaTulos(int id);
	public void simulaatioValmisIlmoitus();
}
