package simu.model;

import simu.framework.Kello;
import simu.framework.Trace;

/** 
 * Luokka <code>Asiakas</code> toimii simulaation Palvelupisteiden käyttäjinä
 */


public class Asiakas{
	/**
	 * Muuttuja <code>saapumisaika</code> ilmaisee ajanhetken simulaatiossa, jolloin asiakas saapuu simulaatioon	 
	 */
	private double saapumisaika;
	/**
	 * Muuttuja <code>poistumisaika</code> ilmaisee ajanhetken simulaatiossa, jolloin asiakas poistuu simulaatiosta 
	 */
	private double poistumisaika;
	/**
	 * Muuttuja <code>haahuiluaika</code> ilmaisee ajan jonka <code>Asiakas</code> käyttää ennen kassalle tai palvelutiskille siirtymistä 
	 */
	private double haahuiluaika;
	/**
	 * Muuttuja <code>jonoonsaapumisaika</code> määrittelee saapumisajan seuraavalle palvelupisteelle 
	 */
	private double jonoonsaapumisaika;
	/**
	 * Muuttuja <code>id</code> on asiakkaan yksilöllinen tunniste
	 */
	private int id;
	/**
	 * <code>ostoslista</code> -olio kuvastaa asiakkaan ostoslistaa jonka avulla määritellään asiakkaan käytöstä simulaatiossa 
	 */
	private Ostoslista ostoslista;
	/**
	 * Muuttuja <code>korttimaksu</code> määrittää, voiko asiakas käyttää korttimaksua
	 */
	private boolean korttimaksu;
	/**
	 * Muuttuja <code>haahuilee</code> ilmaisee, onko asiakas haahuilemassa (keräämässä ostoksia)
	 */
	private boolean haahuilee = false;
	/**
	 * Vakio <code>KORTTIMAKSUNTN</code> määrittää todennäkköisyyden, että <code>korttimaksu</code> on <code>true</code>
	 */
	private final double KORTTIMAKSUNTN; 
	/**
	 * Vakio <code>KERAILYKERROIN</code> määrittää, kuinka pitkään yhden tuotteen kerääminen kestää kaupassa sekuntteina
	 */
	private final int KERAILYKERROIN = 30; 
	/**
	 * Staattinen muuttuja <code>seuraavaId</code> määrittää, kuinka pitkään yhden tuotteen kerääminen kestää kaupassa sekuntteina
	 */
	public static int seuraavaId = 1;
	/**
	 * Staattinen muuttuja <code>sum</code> ilmaisee asiakkaiden simulaatiossaoloajan
	 */
	private static long kokonaisAsiointiAika = 0;
	/**
	 * Staattinen muuttuja <code>valmiit</code> ilmaisee simulaatiosta läpimenneiden asiakkaiden määrän
	 */
	private static int valmiit = 0;
	
	/**
	 * Luo <code>Asiakas</code>-olion jolle annetaan korttimaksun todennäköisyys
	 * @param korttimaksunTN prosenttiarvo todennäköisyydelle että <code>korttimaksu</code> on <code>true</code> 
	 */
	public Asiakas(double korttimaksunTN){
	    id = seuraavaId++;
	    
	    KORTTIMAKSUNTN = korttimaksunTN;
	    ostoslista = new Ostoslista();
	    korttimaksu = generoiKorttimaksu();
	    haahuiluaika = ostoslista.getTuotteita() * KERAILYKERROIN;
	    
		saapumisaika = Kello.getInstance().getAika();
		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+saapumisaika);
	}

	/**
	 * Palauttaa poistumisajan
	 * @return poistumisaika
	 */
	public double getPoistumisaika() {
		return poistumisaika;
	}

	/**
	 * Asettaa poistumisajan
	 * @param poistumisaika
	 */
	public void setPoistumisaika(double poistumisaika) {
		this.poistumisaika = poistumisaika;
	}

	/**
	 * Palauttaa saapumisajan
	 * @return saapumisaika
	 */
	public double getSaapumisaika() {
		return saapumisaika;
	}

	// TODO: tarkista
	// Tämä on käyttämätön
	public void setSaapumisaika(double saapumisaika) {
		this.saapumisaika = saapumisaika;
	}
	
	/**
	 * Palauttaa id:n
	 * @return id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Palauttaa ostoslistan
	 * @return ostoslista
	 */
	public Ostoslista getOstoslista() {
		return ostoslista;
	}
	
	/**
	 * Palauttaa haahuiluajan
	 * @return haahuiluaika
	 */
	public double getHaahuiluaika() {
		return haahuiluaika;
	}
	
	/**
	 * Palauttaa korttimaksun
	 * @return korttimaksu
	 */
	public boolean isKorttimaksu() {
		return korttimaksu;
	}
	
	/**
	 * Palauttaa arvon, onko asiakas haahuilemassa (keräilemässä ostoksia)
	 * @return haahuilee
	 */
	public boolean isHaahuilee() {
		return haahuilee;
	}
	
	/**
	 * Asettaa <code>haahuile</code> -arvon -> <code>false</code>
	 */
	public void setHaahuileeFalse() {
		this.haahuilee = false;
	}
	
	/**
	 * Asettaa <code>haahuile</code> -arvon -> <code>true</code>
	 */
	public void setHaahuileeTrue() {
		this.haahuilee = true;
	}
	
	/**
	 * Palauttaa jonoonsaapumisajan
	 * @return
	 */
	public double getJonoonsaapumisaika() {
		return jonoonsaapumisaika;
	}

	/**
	 * Asettaa jonoonsaapumisajan
	 * @param jonoonsaapumisaika
	 */
	public void setJonoonsaapumisaika(double jonoonsaapumisaika) {
		this.jonoonsaapumisaika = jonoonsaapumisaika;
	}

	/**
	 * Tulostaa raportin consoleen ja päivittää arvoja
	 */
	public void raportti(){
		Trace.out(Trace.Level.INFO, "\nAsiakas " + id + " valmis! ");
		Trace.out(Trace.Level.INFO, "Asiakkas " + id + " osti " + ostoslista.getTuotteita() + " tuotetta");
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " saapui: " + saapumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas " + id + " poistui: " + poistumisaika);
		Trace.out(Trace.Level.INFO,"Asiakas " + id + " viipyi: " + (poistumisaika-saapumisaika));
		Trace.out(Trace.Level.INFO, "Asiakas " + id + " maksoi kortilla: "+korttimaksu);
		kokonaisAsiointiAika += (poistumisaika-saapumisaika);
		double keskiarvo = kokonaisAsiointiAika/++valmiit;
		Trace.out(Trace.Level.INFO, "Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ keskiarvo);
	}

	/**
	 * Arpoo arvon, voiko asiakas käyttää korttimaksua
	 * @return bool
	 */
	private boolean generoiKorttimaksu() {
		return ((Math.random()*100) < KORTTIMAKSUNTN);
	}
}
