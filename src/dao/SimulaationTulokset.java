package dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import simu.model.KassaTyyppi;
import simu.model.PalvelupisteTyyppi;

/**
 * 
 * Luokka <code>SimulaationTulokset</code> määrittelee simulaation lopuksi tallennettavat tulokset
 *
 */

@Entity
@Table
public class SimulaationTulokset {
	
	/**
	 * Muuttuja <code>id</code> on uniikki lukuarvo SimulaationTulosten erittelemiseksi
	 */
	@Id @Column @GeneratedValue private int id;
	
	/**
	 * Muuttuja <code>pvm</code> on String-esitys simulaation ajonaikaisesta ajanhetkestä
	 */
	@Column private String pvm;
	
	/**
	 * Muuttuja <code>simuloinninKokonaisaika</code> on simulaatiossa kulunut kokonaisaika
	 */
	@Column private double simuloinninKokonaisaika;
	
	/**
	 * Muuttuja <code>normKassaLkm</code> on normaalien kassojen lukumäärä simulaatiossa
	 */
	@Column private int normKassaLkm;
	
	/**
	 * Muttuja <code>ipKassaLkm</code> on itsepalvelukassojen lukumäärä simulaatiossa
	 */
	@Column private int ipKassaLkm;
	
	/**
	 * Muuttuja <code>pikaKassaLkm</code> on pikakassojen lukumäärä simulaatiossa
	 */
	@Column private int pikaKassaLkm;
	
	/**
	 * Muuttuja <code>asiakkaatLkm</code> on asiakkaiden kokonaislukumäärä simulaatiossa
	 */
	@Column private int asiakkaatLkm;
	
	/**
	 * Muuttuja <code>korttimaksuTodNak</code> on korttimaksun todennäköisyys simulaatiossa
	 */
	@Column private double korttimaksuTodNak;
	
	/**
	 * Muuttuja <code>ruuhka</code> on String-esitys ruuhkan määrästä simulaatiossa (Hiljaista, Normaalia, Ruuhkaa)
	 */
	@Column private String ruuhka;
	
	
	/**
	 * Muuttuja <code>muokkain</code> on päivämäärän formatointia suorittava olio
	 */
	@Transient SimpleDateFormat muokkain = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	/**
	 * Muuttuja <code>palvelupisteet</code> on setti palvelupisteitä, jotka liittyvät tähän SimulaationTulokset -olioon
	 */
	@OneToMany(cascade = { CascadeType.ALL})
	private Set<PalvelupisteenTulos> palvelupisteet = new HashSet<PalvelupisteenTulos>();
	
	/**
	 * Parametriton konstruktori Hibernatea varten
	 */
	public SimulaationTulokset() {}
	
	
	/**
	 * Luo tietynlaisen SimulaationTulokset -olion
	 * @param simuloinninKokonaisaika ilmaisee simulaation kokonaisaikaa
	 * @param asiakkaatLkm ilmaisee simulaation kokonaisasiakasmäärää
	 * @param korttimaksuTodNak ilmaisee simulaatiossa ollutta korttimaksun todennäköisyyttä
	 * @param ruuhka ilmaisee ruuhkan määrää
	 */
	public SimulaationTulokset(double simuloinninKokonaisaika, int asiakkaatLkm, double korttimaksuTodNak, int ruuhka) {
		Date date = new Date();
		pvm = muokkain.format(date);
		this.simuloinninKokonaisaika = simuloinninKokonaisaika;
		this.asiakkaatLkm = asiakkaatLkm;
		this.korttimaksuTodNak = korttimaksuTodNak;
		this.ruuhka = ruuhkaStringiksi(ruuhka);
	}

	
	/**
	 * Lisää yhden palvelupisteen tuloksen SimulaationTulokset -olion palvelupisteet settiin
	 * @param tulos ilmaisee yhtä PalvelupisteenTulos oliota
	 */
	public void lisaaPalvelupisteenTulos(PalvelupisteenTulos tulos) {
		if(tulos.getPalvelupistetyyppi() != PalvelupisteTyyppi.HAAHUILU) palvelupisteet.add(tulos);
		if(tulos.getKassaTyyppi() == KassaTyyppi.NORMAALI) normKassaLkm++;
		if(tulos.getKassaTyyppi() == KassaTyyppi.ITSEPALVELU) ipKassaLkm++;
		if(tulos.getKassaTyyppi() == KassaTyyppi.PIKA) pikaKassaLkm++;	
	}
	
	/**
	 * Muuntaa ruuhkan arvon integeristä Stringiksi
	 * @param ruuhka on muunnettava ruuhkan arvo
	 * @return palauttaa ruuhkan String-muodossa
	 */
	private String ruuhkaStringiksi(int ruuhka) {
		String palautettava = "Ruuhkaa";
		if(ruuhka == 0) palautettava = "Hiljaista";
		if(ruuhka == 1) palautettava = "Normaalia";
		return palautettava;
	}
		
	/**
	 * Palauttaa SimulaationTulokset <code>id</code>
	 * @return SimulaationTulokset <code>id</code>
	 */
	public int getId() {
		return id;
	}

	/**
	 * Palauttaa SimulaationTulokset <code>pvm</code>
	 * @return SimulaationTulokset <code>pvm</code>
	 */
	public String getPvm() {
		return pvm;
	}

	/**
	 * Palauttaa SimulaationTulokset <code>simuloinninKokonaisaika</code>
	 * @return SimulaationTulokset <code>simuloinninKokonaisaika</code>
	 */
	public double getSimuloinninKokonaisaika() {
		return simuloinninKokonaisaika;
	}

	/**
	 * Palauttaa SimulaationTulokset <code>normKassaLkm</code>
	 * @return SimulaationTulokset <code>normKassaLkm</code>
	 */
	public int getNormKassaLkm() {
		return normKassaLkm;
	}

	/**
	 * Palauttaa SimulaationTulokset <code>ipKassaLkm</code>
	 * @return SimulaationTulokset <code>ipKassaLkm</code>
	 */
	public int getIpKassaLkm() {
		return ipKassaLkm;
	}

	/**
	 * Palauttaa SimulaationTulokset <code>pikaKassaLkm</code>
	 * @return SimulaationTulokset <code>pikaKassaLkm</code>
	 */
	public int getPikaKassaLkm() {
		return pikaKassaLkm;
	}

	/**
	 * palauttaa SimulaationTulokset <code>asiakkaatLkm</code>
	 * @return SimulaationTulokset <code>asiakkaatLkm</code>
	 */
	public int getAsiakkaatLkm() {
		return asiakkaatLkm;
	}

	/**
	 * Palauttaa SimulaationTulokset <code>korttimaksuTodNak</code>
	 * @return SimulaationTulokset <code>korttimaksuTodNak</code>
	 */
	public double getKorttimaksuTodNak() {
		return korttimaksuTodNak;
	}

	/**
	 * Palauttaa SimulaationTulokset <code>ruuhka</code>
	 * @return SimulaationTulokset <code>ruuhka</code>
	 */
	public String getRuuhka() {
		return ruuhka;
	}

	/** 
	 * Palauttaa SimulaationTulokset <code>muokkain</code>
	 * @return SimulaationTulokset <code>muokkain</code>
	 */
	public SimpleDateFormat getMuokkain() {
		return muokkain;
	}

	/**
	 * Palauttaaa SimulaationTulokset <code>palvelupisteet</code>
	 * @return SimulaationTulokset <code>palvelupisteet</code>
	 */
	public Set<PalvelupisteenTulos> getPalvelupisteet() {
		return palvelupisteet;
	}
}
