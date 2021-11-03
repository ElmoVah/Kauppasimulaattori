package simu.model;

import java.util.LinkedList;

import dao.PalvelupisteenTulos;
import dao.SimulaationTulokset;
import eduni.distributions.ContinuousGenerator;
import simu.framework.Kello;
import simu.framework.Tapahtuma;
import simu.framework.Tapahtumalista;
import simu.framework.Trace;

/**
 * 
 * Luokka <code>Palvelupiste</code> määrittelee Simulaatiossa käytettävät palvelupisteet
 *
 */

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class Palvelupiste {
	/**
	 * Muuttuja <code>jono</code> on lista asiakkaista jotka ovat tällä hetkellä tällä palvelupisteellä
	 */
	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>(); // Tietorakennetoteutus
	
	/**
	 * Muuttuja <code>generator</code> luo pseudosatunnaisia lukuja joita tämä luokka käyttää palveluajan määrittämisessä
	 */
	private ContinuousGenerator generator;
	
	//TODO tarkistakaa onks täs mitään järkeä, tähän voisi lisätä linkin noihin tapahtumiin
	/**
	 * <code>tapahtumalista</code> -olio, johon <code>Palvelupiste</code> lisää tulevia B-tapahtumia, eli poistumisia
	 */
	private Tapahtumalista tapahtumalista;
	
	/**
	 * <code>skeduloitavanTapahtumanTyyppi</code> määrittää minkä tyyppisiä tapahtumia Palvelupiste tuottaa
	 */
	private TapahtumanTyyppi skeduloitavanTapahtumanTyyppi; 
	
	/**
	 * <code>palvelupistetyyppi</code> määrittää minkä tyyppinen Palvelupiste on
	 */
	protected PalvelupisteTyyppi palvelupistetyyppi;
	
	/**
	 * Muuttuja <code>palvellut</code> on Palvelupistekohtainen asiakkaiden kokonaismäärä
	 */
	private int palvellut = 0; 
	
	/**
	 * Muuttuja <code>kayttoaika</code> on kokonaisaika jonka Palvelupiste on ollut käytössä
	 */
	protected int kayttoaika = 0;
	
	/**
	 * Muuttuja <code>kokonaisoleskeluaika</code> on kokonaisaika jonka Palvelupiste on käyttänyt asiakkaiden palveluun
	 */
	protected int kokonaisoleskeluaika = 0;
	
	/**
	 * Muuttuja <code>id</code> on uniikki lukuarvo Palvelupisteen tunnistamiseksi
	 */
	private volatile int id;
	
	/**
	 * Muuttuja <code>seuraavaid</code> on staattinen muuttuja jota käytetään uuden Palvelupisteen <code>id</code> määrittämisessä
	 */
	public static int seuraavaid = 0;
	
	/**
	 * Muuttuja <code>varattu</code> kertoo, onko Palvelupisteellä asiakas palveltavana
	 */
	private boolean varattu = false;

	/**
	 * Luo tietynalisen Palvelupisteen annetuilla parametreillä
	 * 
	 * @param generator ilmaisee satunnaislukugeneraattorin
	 * @param tapahtumalista ilmaisee simulaattorissa käytettävän tapahtumalistan
	 * @param tyyppi ilmaisee tapahtuman tyypin
	 * @param pptyyppi ilmaisee Palvelupisteen tyypin
	 */
	public Palvelupiste(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi, PalvelupisteTyyppi pptyyppi){
		this.tapahtumalista = tapahtumalista;
		this.generator = generator;
		this.skeduloitavanTapahtumanTyyppi = tyyppi;
		this.palvelupistetyyppi = pptyyppi;	
		this.id = seuraavaid++;
	}

	/**
	 * Lisää jonoon asiakkaan
	 * 
	 * @param a Asiakas joka lisätään jonoon
	 */
	public void lisaaJonoon(Asiakas a){   // Jonon 1. asiakas aina palvelussa
		jono.add(a);
		
	}

	/**
	 * Palauttaa ja poistaa palvelupisteen jonosta asiakkaan, vaihtaa <code>varattu</code> -muuttujan epätodeksi ja lisää <code>palvellut</code> -muuttujaa yhdellä. 
	 * 
	 * @return poistettu asiakas
	 */
	public Asiakas otaJonosta(){  // Poistetaan palvelussa ollut
		varattu = false;
		palvellut++;
		return jono.poll();
	}

	/**
	 * Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
	 */
	public void aloitaPalvelu(){  
		
		Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + jono.peek().getId());
		Trace.out(Trace.Level.INFO, "Jonossa: "+jono.size());
		double palveluaika;
		
		varattu = true;
		if(skeduloitavanTapahtumanTyyppi == TapahtumanTyyppi.HAAHUILUNPAATTYMINEN) {
			palveluaika = jono.peek().getHaahuiluaika();
		}
		else {
			palveluaika = generator.sample();
		}
		kayttoaika += palveluaika;
		kokonaisoleskeluaika += Kello.getInstance().getAika() - jono.peek().getJonoonsaapumisaika() + palveluaika;
		tapahtumalista.lisaa(new Tapahtuma(skeduloitavanTapahtumanTyyppi,Kello.getInstance().getAika()+palveluaika));
	}


	/**
	 * Kertoo, onko palvelupiste varattu
	 * @return onko varattu
	 */
	public boolean onVarattu(){
		return varattu;
	}

	/**
	 * Kertoo, onko jonossa asiakkaita
	 * @return onko jonossa asiakkaita
	 */
	public boolean onJonossa(){
		return jono.size() != 0;
	}
	
	/**
	 * Palauttaa jonossa olevien asiakkaiden määrän
	 * @return jonossa olevien asiakkaiden määrä
	 */
	public int getJononPituus() {
		return jono.size();
	}
	
	/**
	 * Palauttaa Palvelupisteen <code>id</code>
	 * @return Palvelupisteen <code>id</code>
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Palauttaa palveltujen asiakkaiden kokonaismäärän
	 * @return palveltujen asiakkaiden kokonaismäärä
	 */
	public int getPalvellut() {
		return palvellut;
	}
	
	/**
	 * Palauttaa Palvelupisteen käyttöasteen
	 * @return Palvelupisteen käyttöaste
	 */
	public double laskeKayttoAste() {
		// TODO: try catch (ei voi jakaa nollalla)
		return kayttoaika / Kello.getInstance().getAika();
	}
	
	/**
	 * Palauttaa Palvelupisteen keskimääräisen jonon pituuden
	 * @return Palvelupisteen keskimääräinen jonon pituus
	 */
	public double laskeKeskimaarainenJononpituus() {
		// TODO: try catch (ei voi jakaa nollalla)
		return kokonaisoleskeluaika / Kello.getInstance().getAika();
	}
	
	/**
	 * Palauttaa Palvelupisteen keskimääräisen palveluajan
	 * @return Pavelupisteen keskimääräinen palveluaika
	 */
	public double laskeKeskimaarainenPalveluaika() {
		if(palvellut != 0) {
			return kayttoaika / palvellut;
		} else {
			return 0;
		}
	}

	/**
	 * Palauttaa Palvelupisteen keskimääräisen läpimenoajan
	 * @return Palvelupisteen keskimääräinen läpimenoaika
	 */
	public double laskeKeskimaarainenLapimenoaika() {
		if(palvellut != 0) {
			return kokonaisoleskeluaika / palvellut;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * Palauttaa Palvelupisteen käyttämän satunnaislukugeneraattorin
	 * @return Palvelupisteen satunnaislukugeneraattori
	 */
	public ContinuousGenerator getGenerator() {
		return generator;
	}

	/**
	 * Palauttaa Tapahtumalistan
	 * @return tapahtumalista
	 */
	public Tapahtumalista getTapahtumalista() {
		return tapahtumalista;
	}

	/**
	 * Palauttaa Palvelupisteen tuottamien tapahtumien tyypin
	 * @return tuotettavien tapahtumien tyyppi
	 */
	public TapahtumanTyyppi getSkeduloitavanTapahtumanTyyppi() {
		return skeduloitavanTapahtumanTyyppi;
	}
	
	/**
	 * Kasvattaa palveltujen asiakkaiden lukumäärää yhdellä
	 */
	public void kasvataPalveltuja() {
		palvellut++;
	}
	
	/**
	 * Palauttaa Palvelupisteen suorituskykysuureista laskettuja tuloksia
	 * @param s Simulaatiosta saadut tulokset
	 * @return Palvelupisteen tulokset
	 */
	public PalvelupisteenTulos getPalvelupisteenTulos(SimulaationTulokset s) {
		return new PalvelupisteenTulos(palvelupistetyyppi, palvellut, 
				laskeKeskimaarainenJononpituus(), 
				laskeKeskimaarainenPalveluaika(), 
				laskeKeskimaarainenLapimenoaika(), 
				laskeKayttoAste(), s.getId());
	}
	
	/**
	 * Palauttaa Palvelupisteen tietoja String muodossa
	 * @return tietoja Palvelupisteestä
	 */
	public String toString() {
		return String.format("Palvelupisteen tyyppi: "+palvelupistetyyppi+"\nKeskimääräinen jononpituus: %.2f\nKeskimääräinen palveluaika: %.2f\nKäyttöaste: %.2f\n", laskeKeskimaarainenJononpituus(), laskeKeskimaarainenPalveluaika(), laskeKayttoAste());
	}
	
	/**
	 * Palautaa Palvelupisteen tyypin
	 * @return Palvelupisteen tyyppi
	 */
	public PalvelupisteTyyppi getPalveluPisteenTyyppi() {
		return palvelupistetyyppi;
	}

}
