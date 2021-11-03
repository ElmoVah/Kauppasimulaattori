package dao;

import javax.persistence.*;

import simu.model.KassaTyyppi;
import simu.model.PalvelupisteTyyppi;

/**
 * 
 * Luokka <code>PalvelupisteenTulos</code> määrittelee yhden palvelupisteen
 * suorituskykysuureet
 *
 */

@Entity
@Table
public class PalvelupisteenTulos {
	/**
	 * Muuttuja <code>id</code> on PalvelupisteenTuloksia erittelevä luku
	 */
	@Id @Column @GeneratedValue private int id;
	
	/**
	 * Muuttuja <code>palvelupistetyyppi</code> on palvelupisteen tyyppi
	 */
	@Enumerated(EnumType.STRING) @Column private PalvelupisteTyyppi palvelupistetyyppi;
	
	/**
	 * Muuttuja <code>kassatyyppi</code> on palvelupisteen kassatyyppi, jos palvelupiste on kassa
	 */
	@Enumerated(EnumType.STRING) @Column private KassaTyyppi kassatyyppi;
	
	/**
	 * Muuttuja <code>simulaationId</code> on SimulaatoinTulokset olion id, johon tämä PalvelupisteenTulos liittyy
	 */
	@Column private int simulaationId;
	
	/**
	 * Muuttuja <code>palvellut</code> on tämän palvelupisteen palvelemien asiakkaiden lukumäärä
	 */
	@Column private int palvellut;
	
	/**
	 * Muuttuja <code>keskJononPituus</code> on tämän palvelupisteen keskimääräinen jonon pituus
	 */
	@Column private double keskJononPituus;
	
	/**
	 * Muuttuja <code>keskPalveluaika</code> on tämän palvelupisteen keskimääräinen palveluaika
	 */
	@Column private double keskPalveluaika;
	
	/**
	 * Muuttuja <code>keskLapimenoaika</code> on tämän palvelupisteen keskimääräinen läpimenoaika
	 */
	@Column private double keskLapimenoaika;
	
	/**
	 * Muuttuja <code>kayttoaste</code> on tämän palvelupisteen käyttöaste
	 */
	@Column private double kayttoaste;
	
	/**
	 * Parametriton konstruktori Hibernatea varten
	 */
	public PalvelupisteenTulos() {}
	
	/**
	 * Luo tietynlaisen PalvelupisteenTulos olion
	 * @param tyyppi Palvelupisteen tyyppi
	 * @param palvellut Palvelupisteen palveltujen määrä
	 * @param jononPituus Palvelupisteen keskimääräinen jonon pituus
	 * @param palveluaika Palvelupisteen keskimääräinen palveluaika
	 * @param lapimenoaika Palvelupisteen keskimääräinen lapiemnoaika
	 * @param kayttoaste Palvelupisteen käyttöaste
	 * @param simulaationId Palvelupisteeseen liittyvä SimulaationTulokset id
	 */
	//Kostruktori palvelupisteille, jotka eivät ole kassoja
	public PalvelupisteenTulos(PalvelupisteTyyppi tyyppi, int palvellut, double jononPituus, double palveluaika, double lapimenoaika, double kayttoaste, int simulaationId) {
		this.palvelupistetyyppi = tyyppi;
		this.palvellut = palvellut;
		this.keskJononPituus = jononPituus;
		this.keskPalveluaika = palveluaika;
		this.keskLapimenoaika = lapimenoaika;
		this.kayttoaste = kayttoaste;
		this.kassatyyppi = null;
		this.simulaationId = simulaationId;
	}
	
	/**
	 * Luo tietynlaisen PalvelupisteenTulos olion, jos palvelupiste on Kassa
	 * @param ptyyppi Palvelupisteen tyyppi
	 * @param palvellut Palvelupisteen palveltujen määrä
	 * @param jononPituus Palvelupisteen keskimääräinen jonon pituus
	 * @param palveluaika Palvelupisteen keskimääräinen palveluaika
	 * @param kayttoaste Palvelupisteen käyttöaste
	 * @param lapimenoaika Palvelupisteen keskimääräinen lapiemnoaika
	 * @param ktyyppi Kassan tyyppi
	 * @param simulaationId Palvelupisteeseen liittyvä SimulaationTulokset id
	 */
	//Konstruktori Kassoille
	public PalvelupisteenTulos(PalvelupisteTyyppi ptyyppi, int palvellut, double jononPituus, double palveluaika, double lapimenoaika, double kayttoaste, KassaTyyppi ktyyppi, int simulaationId) {
		this.palvelupistetyyppi = ptyyppi;
		this.palvellut = palvellut;
		this.keskJononPituus = jononPituus;
		this.keskPalveluaika = palveluaika;
		this.keskLapimenoaika = lapimenoaika;
		this.kayttoaste = kayttoaste;
		this.kassatyyppi = ktyyppi;
		this.simulaationId = simulaationId;
	}

	/**
	 * Palauttaa PalvelupisteenTulos <code>palvelupisteentyyppi</code>
	 * @return PalvelupisteenTulos <code>palvelupisteentyyppi</code>
	 */
	public PalvelupisteTyyppi getPalvelupistetyyppi() {
		return palvelupistetyyppi;
	}

	/**
	 * Palauttaa PalvelupisteenTulos <code>keskJononPituus</code>
	 * @return PalvelupisteenTulos <code>keskJononPituus</code>
	 */
	public double getKeskJononPituus() {
		return keskJononPituus;
	}

	/**
	 * Palauttaa PalvelupisteenTulos <code>keskPalveluaika</code>
	 * @return PalvelupisteenTulos <code>keskPalveluaika</code>
	 */
	public double getKeskPalveluaika() {
		return keskPalveluaika;
	}
	
	/**
	 * Palauttaa PalvelupisteenTulos <code>keskLapimenoaika</code>
	 * @return PalvelupisteenTulos <code>keskLapimenoaika</code>
	 */
	public double getKeskLapimenoaika() {
		return keskLapimenoaika;
	}

	/**
	 * Palauttaa PalvelupisteenTulos <code>kayttoaste</code>
	 * @return PalvelupisteenTulos <code>kayttoaste</code>
	 */
	public double getKayttoaste() {
		return kayttoaste;
	}
	
	/**
	 * Palauttaa PalvelupisteenTulos <code>kassatyyppi</code>
	 * @return PalvelupisteenTulos <code>kassatyyppi</code>
	 */
	public KassaTyyppi getKassaTyyppi() {
		return kassatyyppi;
	}
	
	/**
	 * Palauttaa PalvelupisteenTulos <code>palvellut</code>
	 * @return PalvelupisteenTulos <code>palvellut</code>
	 */
	public int getPalvellut() {
		return palvellut;
	}
	
	
	
}
