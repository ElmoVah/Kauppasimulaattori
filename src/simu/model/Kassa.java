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
 * Luokka <code>Kassa</code> määrittelee Simulaatiossa käytettävät kassat
 *
 */

public class Kassa extends Palvelupiste implements Comparable<Kassa> {
	/**
	 * <code>KassaTyyppi</code> määrittää kassan tyypin
	 */
	KassaTyyppi kassatyyppi;
	
	/**
	 * Muuttuja <code>jono</code> on lista asiakkaista jotka ovat tällä hetkellä tällä kassalla
	 */
	private LinkedList<Asiakas> jono = new LinkedList<Asiakas>();
	
	/**
	 * Muuttuja <code>varattu</code> on boolean arvo, joka kertoo onko kassa varattu
	 */
	private boolean varattu = false;
	
	/**
	 * Luo tietynalisen Kassan annetuilla parametreillä
	 * 
	 * @param generator ilmaisee satunnaislukugeneraattorin
	 * @param tapahtumalista ilmaisee simulaattorissa käytettävän tapahtumalistan
	 * @param tyyppi ilmaisee luotavan tapahtuman tyypin
	 * @param kassatyyppi ilmaisee Kassan tyypin
	 * @param pptyyppi ilmaisee Palvelupisteen tyypin
	 */
	public Kassa(ContinuousGenerator generator, Tapahtumalista tapahtumalista, TapahtumanTyyppi tyyppi, KassaTyyppi kassatyyppi, PalvelupisteTyyppi pptyyppi) {
		super(generator, tapahtumalista, tyyppi, pptyyppi);
		this.kassatyyppi = kassatyyppi;
	}
	
	/**
	 * Aloitetaan asiakkaalle palvelu, asiakas on jonossa palvelun aikana
	 */
	public void aloitaPalvelu(){
		
		Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + jono.peek().getId() + " kassalla "+super.getId()+" "+kassatyyppi);
		Trace.out(Trace.Level.INFO, "Ostoksien lkm: "+jono.peek().getOstoslista().getTuotteita()+" "+" Korttimaksu: "+jono.peek().isKorttimaksu());
		Trace.out(Trace.Level.INFO, "Jonossa: "+jono.size());
		
		varattu = true;
		double palveluaika = getPalveluaika();
		kayttoaika += palveluaika;
		kokonaisoleskeluaika += Kello.getInstance().getAika() - jono.peek().getJonoonsaapumisaika() + palveluaika;
		super.getTapahtumalista().lisaa(new Tapahtuma(super.getSkeduloitavanTapahtumanTyyppi(),Kello.getInstance().getAika()+palveluaika, this));
	}
	
	/**
	 * Palauttaa KassaTyypin
	 * @return Kassan <code>KassaTyyppi</code>
	 */
	public KassaTyyppi getKassaTyyppi() {
		return kassatyyppi;
	}
	
	/**
	 * Palauttaa boolean arvon, joka kertoo onko kassa varattu
	 * @return Kassan <code>varattu</code> -booleanin
	 */
	@Override
	public boolean onVarattu(){
		return varattu;
	}
	
	/**
	 * Poistaa kassalta palvelussa olleen asiakkaan jonosta ja palauttaa sen
	 * @return Kassalta poistettu <code>Asiakas</code>
	 */
	@Override
	public Asiakas otaJonosta(){  // Poistetaan palvelussa ollut
		varattu = false;
		super.kasvataPalveltuja();
		return jono.poll();
	}
	
	/**
	 * Palauttaa boolean arvon, joka kertoo onko kassan jonossa ketään
	 * @return Kassan jonosta totuusarvo
	 */
	@Override
	public boolean onJonossa() {
		return jono.size() != 0;
	}
	
	/**
	 * Palauttaa Kassan jonon pituuden
	 * @return Kassan jonon pituus
	 */
	@Override
	public int getJononPituus() {
		return jono.size();
	}
	
	/**
	 * Comparable-rajapinnan metodi, joka asettaa kassat jonon pituuden mukaan järjestykseen
	 * @return Int-arvo, joka kuvaa kahden jonon pituuksien vertailua
	 */
	public int compareTo(Kassa kassa) {
		return jono.size() - kassa.getJononPituus();
	}
	
	/**
	 * Lisää Kassan jonoon asiakkaan
	 * @param a lisättävä Asiakas
	 */
	@Override
	public void lisaaJonoon(Asiakas a){   // Jonon 1. asiakas aina palvelussa
		jono.add(a);
	}

	/**
	 * Palauttaa Asiakkaalle asetettavan palveluajan ostoslistalla olevien tuotteiden
	 * määrän perusteella
	 * @return palveluaika asiakkaalle
	 */
	private double getPalveluaika() {
		final int PALVELUAIKA = 15;
		double palautettava;
		do{
			palautettava = PALVELUAIKA + jono.peek().getOstoslista().getTuotteita() * super.getGenerator().sample();
		}
		while(palautettava <= 0);
		return palautettava;
	}
	
	/**
	 * Palauttaa yhden palvelupisteen suorituskykysuureita
	 * @param s SimulaationTulokset, johon tämä PalvelupisteenTulos liitetään
	 * @return PalvelupisteenTulos
	 */
	public PalvelupisteenTulos getPalvelupisteenTulos(SimulaationTulokset s) {
		return new PalvelupisteenTulos(
				super.palvelupistetyyppi, 
				super.getPalvellut(), 
				laskeKeskimaarainenJononpituus(), 
				laskeKeskimaarainenPalveluaika(), 
				laskeKeskimaarainenLapimenoaika(), 
				laskeKayttoAste(), kassatyyppi, s.getId());
	}
	
	/**
	 * Palauttaa palvelupisteen tietoja String-muodossa
	 * @return String palvelupisteen tiedoista
	 */
	public String toString() {
		return String.format("Kassan id: %d\tKassan tyyppi: "+kassatyyppi+"\nKeskimääräinen jononpituus: %.2f\nKeskimääräinen palveluaika: %.2f\nKäyttöaste: %.2f\n", super.getId(), super.laskeKeskimaarainenJononpituus(), super.laskeKeskimaarainenPalveluaika(), super.laskeKayttoAste());
	}
}
