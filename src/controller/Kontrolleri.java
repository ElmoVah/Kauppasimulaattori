package controller;

import dao.ISimulaationTuloksetDAO;
import dao.PalvelupisteenTulos;
import dao.SimulaationTulokset;
import dao.SimulaationTuloksetAccessObject;
import javafx.application.Platform;
import simu.framework.IMoottori;
import simu.model.KassaTyyppi;
import simu.model.OmaMoottori;
import simu.model.Palvelupiste;
import view.ISimulaattorinUI;
import view.SimulaattorinGUI;

/**
 * 
 * Luokka <code>Kontrolleri</code> ohjaa simulaattorin graafisen käyttöliittymän ja mallin
 * välistä toimintaa
 *
 */

public class Kontrolleri implements IKontrolleri {
	
	/**
	 * Muuttuja <code>moottori</code> on simulaation mallin ajaja
	 */
	private IMoottori moottori;
	
	/**
	 * Muuttuja <code>ui</code> on simulaation graafinen käyttöliittymä
	 */
	private ISimulaattorinUI ui;
	
	/**
	 * Muuttuja <code>tuloksetDAO</code> on simulaation tietokantavastaava olio
	 */
	private ISimulaationTuloksetDAO tuloksetDAO = new SimulaationTuloksetAccessObject();
	
	/**
	 * Luo simulaation kontrollerin
	 * @param gui Simulaation graafinen käyttöliittymä
	 */
	public Kontrolleri(SimulaattorinGUI gui) {
		this.ui = gui;
		
	}

	/**
	 * Käynnistää simulaation aloittamalla moottorisäikeen
	 * @return boolean arvo, joka kertoo onnistuiko simulaation käynnistäminen
	 */
	@Override
	public boolean kaynnistaSimulointi() {
		if(ui.getNormiKassat() != -1 && ui.getPikaKassat() != -1 && ui.getIpKassat() != -1
		&& ui.getAika() != -1 && ui.getViive() != -1 && ui.getKorttimaksunTN() != -1 && ui.getRuuhkaMaara() != -1) {
			moottori = new OmaMoottori(this, ui.getNormiKassat(), ui.getPikaKassat(), ui.getIpKassat(), ui.getRuuhkaMaara(), ui.getKorttimaksunTN(), tuloksetDAO);
			moottori.setSimulointiaika(ui.getAika());
			moottori.setViive(ui.getViive());
			((Thread) moottori).start();
			return true;
		}
		return false;
	}

	/**
	 * Nopeuttaa simulaatiota pienentämällä viivettä 0.9-kertaiseksi
	 */
	@Override
	public void nopeuta() {
		if(moottori.getViive() < 50 && moottori.getViive() >= 5) {
			moottori.setViive(moottori.getViive() - 5);
		}
		else {
			moottori.setViive((long)(moottori.getViive()*0.9));
		}
	}

	/**
	 * Hidastaa simulaatiota suurentamalla viivettä 1.1-kertaiseksi
	 */
	@Override
	public void hidasta() {
		if(moottori.getViive() < 50) {
			moottori.setViive(moottori.getViive() + 5);
		}
		else {
			moottori.setViive((long)(moottori.getViive()*1.10));
		}
	}
	
	/**
	 * Ajaa simulaation nopeasti loppuun asettamalla viiveen nollaksi
	 */
	@Override
	public void ajaLoppuun() {
		moottori.setViive(0);
	}

	/**
	 * Käskee GUIn asettaamaan nykyajan graafiseen käyttöliittymään näkyville
	 */
	@Override
	public void naytaNykyaika(double aika) {
		Platform.runLater(()->ui.setNykyAika(aika));
	}
	
	/**
	 * Käskee GUIn asettaamaan nykyisen viiveen graafiseen käyttöliittymään näkyville
	 */
	@Override
	public void naytaNykyviive(long viive) {
		Platform.runLater(()->ui.setNykyViive(viive));
	}

	/**
	 * Käskee GUIn visualisoimaan yhden asiakkaan tiettyyn jonoon
	 * @param index Jono, johon asiakas visualisoidaan
	 * @param maara Nykyinen asiakasmäärä jonossa
	 */
	@Override
	public void visualisoiAsiakas(int index, int maara) {
		Platform.runLater(()->ui.lisaaJonoon(index, maara));
	}
	
	/**
	 * Käskee GUIn visualisoimaan yhden asikkaan tiettyyn kassajonoon
	 * @param index Kassajono, johon asiakas visualisoidaan
	 * @param maara Nykyinen asiakasmäärä kassajonossa
	 * @param kassatyyppi Kassan tyyppi, jolle asiakas visualisoidaan
	 */
	@Override
	public void visualisoiKassanAsiakas(int index, int maara, KassaTyyppi kassatyyppi) {
		Platform.runLater(()->ui.lisaaKassaJonoon(index, maara, kassatyyppi));
	}
	
	/**
	 * Käskee GUIn poistamaan yhden asiakkaan tietystä jonosta
	 * @param index Jono, josta asiakas poistetaan
	 * @param maara Nykyinen asiakasmäärä jonossa
	 */
	@Override
	public void poistaAsiakas(int index, int maara) {
		Platform.runLater(()->ui.poistaJonosta(index, maara));
	}
	
	/**
	 * Käskee GUIn poistamaan kaikki asiakkaat kaikista jonoista
	 * ja nollaamaan laskurit jonojen alla
	 */
	@Override
	public void poistaKaikkiJonosta() {
		Platform.runLater(()->ui.poistaKaikkiJonosta());
	}
	
	/**
	 * Käskee GUIn poistamaan yhden asiakkaan kassan jonosta
	 * @param index Kassajono, josta asiakas poistetaan
	 * @param maara Nykyinen asiakasmäärä kassajonossa
	 * @param kassatyyppi kassatyyppi, jolta asiakas poistetaan
	 */
	@Override
	public void poistaKassanAsiakas(int index, int maara, KassaTyyppi kassatyyppi) {
		Platform.runLater(()->ui.poistaKassaJonosta(index, maara, kassatyyppi));
	}
	
	/**
	 * Käskee GUIn luomaan ilmoituksen simulaation päättymisestä
	 */
	@Override
	public void simulaatioValmisIlmoitus() {
		Platform.runLater(()->ui.simulaatioValmisIlmoitus());
	}
	
	/**
	 * Välittää palvelupisteet GUI:lle
	 * @return Palvelupisteet listana
	 */
	@Override
	public Palvelupiste[] viePalvelupisteet() {
		return moottori.getPalvelupisteet();
	}

	/**
	 * Välittää kaikki SimulaationTulokset arrayna tietokannasta GUI:lle
	 * @return <code>SimulaationTulokset[]</code>
	 */
	@Override
	public SimulaationTulokset[] lueTulokset() {
		SimulaationTulokset[] tulokset = null;
		tulokset = tuloksetDAO.lueTulokset();
		return tulokset;
	}
	
	/**
	 * Välittää kaikki PalvelupisteenTulos oliot arrayna tietokannsta GUI:lle
	 * @param st SimulaationTulokset, joihin PalvelupisteenTulos[] liittyy
	 * @return PalvelupisteenTulos oliot arrayna
	 */
	@Override
	public PalvelupisteenTulos[] luePalvelupisteet(SimulaationTulokset st) {
		PalvelupisteenTulos[] tulokset = null;
		tulokset = tuloksetDAO.luePalvelupisteenTulokset(st);
		return tulokset;
	}
	
	/**
	 * Käskee DAO:n poistaa tietokannasta yhden SimulaationTulokset olion
	 * @param id Poistettavan SimulaationTulokset olion id
	 */
	@Override
	public void poistaTulos(int id) {
		tuloksetDAO.poistaTulos(id);		
	}
	
	/**
	 * Käskeen GUIn asettaa oletusikonit
	 */
	@Override
	public void asetaIkonit() {
		Platform.runLater(()->ui.asetaIkonit());
	}
	
	/**
	 * Käskee GUIn asettaa ruksit käytöstä poistettujen kassojen päälle
	 */
	@Override
	public void asetaRuksit() {
		Platform.runLater(()->ui.asetaRuksit());
	}
	
	/**
	 * Käskee GUI:n poistaa käynnistysnapin esto
	 */
	@Override
	public void otaKayttoonKaynnistys() {
		ui.otaKayttoonKaynnistys();
	}
}
