package view;

import simu.model.KassaTyyppi;

public interface ISimulaattorinUI {
	public void setNykyAika(double aika);
	public void setNykyViive(long viive);
	public long getViive();
	public double getAika();
	public int getNormiKassat();
	public int getPikaKassat();
	public int getIpKassat();
	double getKorttimaksunTN();
	int getRuuhkaMaara();
	void lisaaJonoon(int index, int maara);
	void poistaJonosta(int index, int maara);
	public void poistaKaikkiJonosta();
	public void asetaIkonit();
	public void asetaRuksit();
	void lisaaKassaJonoon(int id, int maara, KassaTyyppi kassatyyppi);
	public void poistaKassaJonosta(int id, int maara, KassaTyyppi kassatyyppi);
	public void otaKayttoonKaynnistys();
	public void simulaatioValmisIlmoitus();
}
