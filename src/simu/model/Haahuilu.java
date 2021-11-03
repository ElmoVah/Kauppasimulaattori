package simu.model;

import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;
import simu.framework.Trace;

/**
 * Luokka, jolla mallinnetaan asiakkaiden ostosten keräilyä, ennen kassalle tai palvelutiskeille siirtymistä. Alaluokka luokalle <code>Tapahtuma</code>
 * @see simu.framework.Tapahtuma
 */

public class Haahuilu extends Palvelupiste {
	/**
	 * Olio <code>jono</code> on lista asiakkaista, jotka ovat palvelupisteellä
	 */
	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();

	/**
	 * Muuttuja <code>varattu</code> ilmaisee, onko asiakas palveltavana tällä hetkellä
	 */
	private boolean varattu = false;
	/**
	 * Muuttuja <code>kokonaishaahuiluaika</code> ilmaisee asiakkaiden kokonaisajan, joka tällä palvelupisteellä on käytetty
	 */
	private double kokonaishaahuiluaika;
	/**
	 * Muuttuja <code>asiakkaidenkokonaismaara</code> ilmaisee kokonaismäärän asiakkaista, jotka ovat saapuneet tälle palvelupisteelle
	 */
	private int asiakkaidenkokonaismaara;
	
	/**
	 * Luo <code>Haahuilu</code> -olion annetuilla paremetreillä
	 * @param generator satunnaislukugeneraattori
	 * @param tapahtumalista tapahtumalista
	 * @param tyyppi tapahtuman tyyppi
	 * @param pptyyppi palvelupisteen tyyppi
	 */
	public Haahuilu(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi, PalvelupisteTyyppi pptyyppi) {
		super(generator, tapahtumalista, tyyppi, pptyyppi);	
	}
	

	@Override
	public void aloitaPalvelu(){
		
		if(!jono.getLast().isHaahuilee()) {
			jono.getLast().setHaahuileeTrue();
			Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + jono.getLast().getId());
			Trace.out(Trace.Level.INFO, "Haahuilemassa: "+jono.size());
			double palveluaika;
			
			palveluaika = jono.getLast().getHaahuiluaika();
			kokonaishaahuiluaika += palveluaika;
			asiakkaidenkokonaismaara++;

			super.getTapahtumalista().lisaa(new Tapahtuma(super.getSkeduloitavanTapahtumanTyyppi(),Kello.getInstance().getAika()+palveluaika));
		}
	}
	
	@Override
	public int getJononPituus() {
		return jono.size();
	}
	
	public void lisaaJonoon(Asiakas a){
		jono.add(a);
		
	}

	/**
	 * Palauttaa ja poistaa jonosta seuraavaksi valmiin asiakkaan
	 */
	public Asiakas otaJonosta(){
		Asiakas kopio = jono.peek();
		int i = 0;
		for(Asiakas a : jono) {
			if(a.getSaapumisaika()+a.getHaahuiluaika()-Kello.getInstance().getAika() < kopio.getHaahuiluaika()+kopio.getSaapumisaika()-Kello.getInstance().getAika()) {
				kopio = a;
				i = jono.indexOf(a);
			}
		}
		jono.remove(i);
		return kopio;
	}
	
	public boolean onVarattu(){
		return varattu;
	}


	public boolean onJonossa(){
		return jono.size() != 0;
	}
	/**
	 * Palauttaa asiakkaiden kokonaismäärän
	 * @return asiakkaidenkokonaismaara
	 */
	public int getAsiakkaidenKokonoisamaara() {
		return asiakkaidenkokonaismaara;
	}
	
	public String toString() {
		return String.format("\nKeskimääräinen itsenäinen ostostenkeruuaika: %.2f\n",(kokonaishaahuiluaika / asiakkaidenkokonaismaara));
	}
}
