package simu.model;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Normal;

/**
 * 
 * Luokka <code>Ostoslista</code> sisältää Asiakkaan käyttämän ostoslistan tiedot
 *
 */

public class Ostoslista {
	private int tuotteita;
	private boolean lihatiski = false;
	private boolean kalatiski = false;
	private boolean juustotiski = false;
	
	//Generaattori ostoslistaan koon luomiseen
	ContinuousGenerator ostoslistanKoonGeneraattori = new Normal(25, 100);
	
	/**
	 * Vakio <code>TISKI</code> määrittää todennäköisyyden tiskivierailulle
	 */
	private final double TISKI = 10; 
	
	/**
	 * Luo <code>Ostoslista</code> -olion
	 */
	public Ostoslista() {
		tuotteita = generoiTuotteidenMaara();
		lihatiski = generoiTiskinVierailu();
		kalatiski = generoiTiskinVierailu();
		juustotiski = generoiTiskinVierailu();
	}
	
	/**
	 * Palauttaa generoitujen tuotteiden määrän
	 * @return tuotteiden määrä
	 */
	private int generoiTuotteidenMaara() {		
		int palautettava = 0;
		while(palautettava <= 0){
			palautettava = (int)ostoslistanKoonGeneraattori.sample();
		}
		
		//Varmistetaan, että ostoslistalla on vähintään tiskivierailujen mukainen määrä tuotteita.
		if(isLihatiski()) palautettava++;
		if(isKalatiski()) palautettava++;
		if(isJuustotiski()) palautettava++;
		
		return palautettava;	
	}
	
	/**
	 * Palauttaa arvon, käydäänkö palvelutiskillä
	 * @return käydäänkö palvelutiskillä
	 */
	private boolean generoiTiskinVierailu() {
		return ((Math.random()*100) <= TISKI);
	}
	
	/**
	 * Palauttaa tuotteiden määrän
	 * @return tuotteiden määrä
	 */
	public int getTuotteita() {
		return tuotteita;
	}

	/**
	 * Palauttaa onko listalla lihatiskillä käynti
	 * @return onko listalla lihatiskillä käynti
	 */
	public boolean isLihatiski() {
		return lihatiski;
	}

	/**
	 * Palauttaa onko listalla kalatiskillä käynti
	 * @return onko listalla kalatiskillä käynti
	 */
	public boolean isKalatiski() {
		return kalatiski;
	}

	/**
	 * Palauttaa onko listalla juustotiskillä käynti
	 * @return onko listalla juustotiskillä käynti
	 */
	public boolean isJuustotiski() {
		return juustotiski;
	}

}
