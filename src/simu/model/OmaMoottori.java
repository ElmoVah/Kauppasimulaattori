package simu.model;

import java.util.ArrayList;
import java.util.Collections;

import controller.IKontrolleri;
import dao.ISimulaationTuloksetDAO;
import dao.SimulaationTulokset;
import dao.SimulaationTuloksetAccessObject;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.Kello;
import simu.framework.Moottori;
import simu.framework.Saapumisprosessi;
import simu.framework.Tapahtuma;

/**
 * 
 * Luokka <code>OmaMoottori</code> luo mallin, ajaa mallia ja välittää tietoja kontrollerin kautta
 * tietokannalle sekä käyttöliittymälle
 *
 */

public class OmaMoottori extends Moottori {

	/**
	 * Muuttuja <code>saapumisprosessi</code> on olio, joka luo asiakkaiden saapumisia
	 */
	private Saapumisprosessi saapumisprosessi;
	
	/**
	 * Muuttuja <code>kassat</code> on ArrayList kaikista simulaation kassoista
	 */
	private ArrayList<ArrayList<Kassa>> kassat;
	
	/**
	 * Muuttuja <code>tuloksetDAO</code> on simulaation tietokantavastaava olio
	 */
	private ISimulaationTuloksetDAO tuloksetDAO;
	
	/**
	 * Muuttuja <code>ruuhkanmaara</code> on kokonaislukuesitys ruuhkan tasosta (0, 1, 2)
	 */
	private int ruuhkanmaara;
	
	/**
	 * Muuttuja <code>NORMITAIIP</code> on final määreinen muuttuja, joka esittää kassanvalinnassa vaihtoehtoa 
	 * normaali kassa tai itsepalvelukassa
	 */
	private final int NORMITAIIP = 1;
	
	/**
	 * Muuttuja <code>MIKATAHANSA</code> on final määreinen muuttuja, joka esittää kassanvalinnassa vaihtoehtoa
	 * mikä tahansa kassa
	 */
	private final int MIKATAHANSA = 2;
	
	/**
	 * Muuttuja <code>NORMITAIPIKA</code> on final määreinen muuttuja, joka esittää kassanvalinnassa vaihtoehtoa
	 * normaali kassa tai pikakassa
	 */
	private final int NORMITAIPIKA = 3;
	
	/**
	 * Muuttuja <code>NORMI</code> on final määreinen muuttuja, joka esittää kassanvalinnassa vaihtoehtoa
	 * normaali kassa
	 */
	private final int NORMI = 4;
	
	/**
	 * Muttuja <code>HILJAISTA</code> on ruuhkan astetta kuvaava muuttuja
	 */
	private final int HILJAISTA = 0;
	
	/**
	 * Muuttuja <code>NORMAALIA</code> on ruuhkan astetta kuvaava muuttuja
	 */
	private final int NORMAALIA = 1;
	
	/**
	 * Muuttuja <code>HAAHUILU</code> on haahuilu-palvelupisteen indeksiä vastaava final määreinen muuttuja
	 */
	private final int HAAHUILU = 0;
	
	/**
	 * Muuttuja <code>LIHATISKI</code> on lihatiski-palvelupisteen indeksiä vastaava final määreinen muuttuja
	 */
	private final int LIHATISKI = 1;
	
	/**
	 * Muuttuja <code>KALATISKI</code> on kalatiski-palvelupisteen indeksiä vastaava final määreinen muuttuja
	 */
	private final int KALATISKI = 2;
	
	/**
	 * Muuttuja <code>JUUSTOTISKI</code> on juustotiski-palvelupisteen indeksiä vastaava final määreinen muuttuja
	 */
	private final int JUUSTOTISKI = 3;
	
	/**
	 * Muuttuja <code>IPKASSAMAX</code> on itsepalvelukassojen maksimimäärää vastaava muuttuja
	 */
	private final int IPKASSAMAX = 5;
	
	/**
	 * Muuttuja <code>KORTTIMAKSUNTN</code> on korttimaksun todennäköisyys
	 */
	private final double KORTTIMAKSUNTN;
	
	/**
	 * Muuttuja <code>TISKIAIKA</code> on palvelutiskeille (liha, kala, juusto) palveluajan arpova generaattori
	 */
	private final ContinuousGenerator TISKIAIKA = new Normal(60, 200);
	
	/**
	 * Muuttuja <code>KASSAAIKA</code> on kassoille, joissa työskentelee henkilökuntaa, ostoskohtaisen palveluajan arpova generaattori
	 */
	private final ContinuousGenerator KASSAAIKA = new Normal(5, 2);
	
	/**
	 * Muuttuja <code>IPKASSAAIKA</code> on itsepalvelukassoille ostoskohtaisen palveluajan arpova generaattori
	 */
	private final ContinuousGenerator IPKASSAAIKA = new Normal(7, 3);
	

	/**
	 * Luo OmaMoottori olion, alustaa sille argumentteina annetut muuttujat ja luo kaikki palvelupisteet, sekä lisää ne palvelupisteet -listaan.
	 * Luo myös ArrayListin kassat, johon luodaan kolme eri ArrayListiä, yksi jokaiselle kassatyypille.
	 * @param kontrolleri Simulaation kontrolleri
	 * @param normikassat Normaalien kassojen lukumäärä
	 * @param pikakassat Pikakassojen lukumäärä
	 * @param ipkassat Itsepalvelukassojen lukumäärä
	 * @param ruuhkanmaara Ruuhkan astetta kuvaava muuttuja
	 * @param korttimaksunTN Korttimaksun todennäköisyys
	 */
	public OmaMoottori(IKontrolleri kontrolleri, int normikassat, int pikakassat, int ipkassat, int ruuhkanmaara, double korttimaksunTN, ISimulaationTuloksetDAO tuloksetDAO) {
		super(kontrolleri);

		KORTTIMAKSUNTN = korttimaksunTN;
		this.ruuhkanmaara = ruuhkanmaara;
		this.tuloksetDAO = tuloksetDAO;
		kassat = new ArrayList<ArrayList<Kassa>>();

		ArrayList<Kassa> normiKassat = new ArrayList<Kassa>();
		ArrayList<Kassa> pikaKassat = new ArrayList<Kassa>();
		ArrayList<Kassa> ipKassat = new ArrayList<Kassa>();
		Collections.addAll(kassat, normiKassat, pikaKassat, ipKassat);

		final int PALVELUTISKIENSUMMA = 4;
		final int PALVELUPISTEIDENSUMMA = PALVELUTISKIENSUMMA + normikassat + pikakassat + ipkassat;
		palvelupisteet = new Palvelupiste[PALVELUPISTEIDENSUMMA];

		palvelupisteet[HAAHUILU] = new Haahuilu(new Normal(10, 6), tapahtumalista, TapahtumanTyyppi.HAAHUILUNPAATTYMINEN,
				PalvelupisteTyyppi.HAAHUILU);
		palvelupisteet[LIHATISKI] = new Palvelupiste(TISKIAIKA, tapahtumalista, TapahtumanTyyppi.LIHATISKIPOISTUMINEN,
				PalvelupisteTyyppi.LIHATISKI);
		palvelupisteet[KALATISKI] = new Palvelupiste(TISKIAIKA, tapahtumalista, TapahtumanTyyppi.KALATISKIPOISTUMINEN,
				PalvelupisteTyyppi.KALATISKI);
		palvelupisteet[JUUSTOTISKI] = new Palvelupiste(TISKIAIKA, tapahtumalista,
				TapahtumanTyyppi.JUUSTOTISKIPOISTUMINEN, PalvelupisteTyyppi.JUUSTOTISKI);

		// Aloitetaan palvelupisteiden täyttäminen neljännestä indeksistä (Neljä ensimmäistä palvelupistettä ovat staattisia)
		//Luodaan normikassat
		for (int i = PALVELUTISKIENSUMMA; i < PALVELUTISKIENSUMMA + normikassat; i++) {
			palvelupisteet[i] = new Kassa(KASSAAIKA, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN,
					KassaTyyppi.NORMAALI, PalvelupisteTyyppi.KASSA);
		}
		//luodaan pikakassat
		for (int i = PALVELUTISKIENSUMMA + normikassat; i < PALVELUTISKIENSUMMA + normikassat + pikakassat; i++) {
			palvelupisteet[i] = new Kassa(KASSAAIKA, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN,
					KassaTyyppi.PIKA, PalvelupisteTyyppi.KASSA);
		}
		//luodaan itsepalvelukassat
		for (int i = PALVELUTISKIENSUMMA + normikassat + pikakassat; i < PALVELUPISTEIDENSUMMA; i++) {
			palvelupisteet[i] = new Kassa(IPKASSAAIKA, tapahtumalista, TapahtumanTyyppi.KAUPASTAPOISTUMINEN,
					KassaTyyppi.ITSEPALVELU, PalvelupisteTyyppi.KASSA);
		}
		
		if(ruuhkanmaara == HILJAISTA) {
			saapumisprosessi = new Saapumisprosessi(new Negexp(100), tapahtumalista, TapahtumanTyyppi.KAUPPAANSAAPUMINEN);
		} else if(ruuhkanmaara == NORMAALIA) {
			saapumisprosessi = new Saapumisprosessi(new Negexp(50), tapahtumalista, TapahtumanTyyppi.KAUPPAANSAAPUMINEN);
		} else {
			saapumisprosessi = new Saapumisprosessi(new Negexp(20), tapahtumalista, TapahtumanTyyppi.KAUPPAANSAAPUMINEN);
		}

		// Lisätään kassat -listaan kaikki luodut kassat tyypin perusteella
		for (Palvelupiste kassa : palvelupisteet) {
			if (kassa instanceof Kassa) {
				KassaTyyppi tyyppi = ((Kassa) kassa).getKassaTyyppi();
				switch (tyyppi) {
				case NORMAALI:
					normiKassat.add((Kassa) kassa);
					break;
				case ITSEPALVELU:
					ipKassat.add((Kassa) kassa);
					break;
				case PIKA:
					pikaKassat.add((Kassa) kassa);
					break;
				}
			}
		}
	}

	/**
	 * Alustaa simulaation poistamalla graafisesta käyttöliittymästä visualisoinnit ja asettaa uudet
	 * kassojen kuvat sekä ruksit käytöstä poistettujen kassojen päälle. Luo ensimmäisen asiakkaan.
	 */
	@Override
	protected void alustukset() {
		kontrolleri.poistaKaikkiJonosta();
		kontrolleri.asetaIkonit();
		kontrolleri.asetaRuksit();
		saapumisprosessi.generoiSeuraava();
	}

	/**
	 * B-tapahtumia suorittava metodi. Valitsee tapahtuman tyypin perusteella mitä tehdä. Poistaa tapahtuman luoneelta
	 * palvelupisteeltä jonossa ensimmäisenä olevan ja lisää saman asiakkaan seuraavalle palvelupisteelle.
	 * @param t Suoritettava b-tapahtuma
	 */
	@Override
	protected void suoritaTapahtuma(Tapahtuma t) {

		Asiakas a;
		switch (t.getTyyppi()) {

		case KAUPPAANSAAPUMINEN:
			a = new Asiakas(KORTTIMAKSUNTN);
			kontrolleri.visualisoiAsiakas(0, palvelupisteet[HAAHUILU].getJononPituus());
			palvelupisteet[HAAHUILU].lisaaJonoon(a);
			saapumisprosessi.generoiSeuraava();
			break;
		case HAAHUILUNPAATTYMINEN:
			a = tapahtumanPaattyminen(HAAHUILU);
			if (a.getOstoslista().isLihatiski()) {
				visualisoiJaLisaaJonon(a, LIHATISKI);
			} else if (a.getOstoslista().isKalatiski()) {
				visualisoiJaLisaaJonon(a, KALATISKI);
			} else if (a.getOstoslista().isJuustotiski()) {
				visualisoiJaLisaaJonon(a, JUUSTOTISKI);
			} else {
				siirraKassalle(a);
			}
			break;
		case LIHATISKIPOISTUMINEN:
			a = tapahtumanPaattyminen(LIHATISKI);
			if (a.getOstoslista().isKalatiski()) {
				visualisoiJaLisaaJonon(a, KALATISKI);
			} else if (a.getOstoslista().isJuustotiski()) {
				visualisoiJaLisaaJonon(a, JUUSTOTISKI);
			} else {
				siirraKassalle(a);
			}
			break;
		case KALATISKIPOISTUMINEN:
			a = tapahtumanPaattyminen(KALATISKI);
			if (a.getOstoslista().isJuustotiski()) {
				visualisoiJaLisaaJonon(a, JUUSTOTISKI);
			} else {
				siirraKassalle(a);
			}
			break;
		case JUUSTOTISKIPOISTUMINEN:
			a = tapahtumanPaattyminen(JUUSTOTISKI);
			siirraKassalle(a);
			break;
		case KAUPASTAPOISTUMINEN:
			Kassa kassa = t.getKassa();
			kontrolleri.poistaKassanAsiakas(kassa.getId(), kassa.getJononPituus(), ((Kassa)palvelupisteet[kassa.getId()]).getKassaTyyppi());
			a = kassa.otaJonosta();
			a.setPoistumisaika(Kello.getInstance().getAika());
			a.raportti();
		}
	}

	/**
	 * Poistaa asiakkaan palvelupisteeltä ja asettaa saapumisajan seuraavalle palvelupisteelle 
	 * sekä käskee kontrollerin päivittää grafiikka käyttöliittymässä
	 * @param palvelupiste Palvelupiste, jolta poistetaan asiakas
	 * @return asiakas 
	 */
	private Asiakas tapahtumanPaattyminen(int palvelupiste) {
		kontrolleri.poistaAsiakas(palvelupiste, palvelupisteet[palvelupiste].getJononPituus());
		Asiakas asiakas = palvelupisteet[palvelupiste].otaJonosta();
		asiakas.setJonoonsaapumisaika(Kello.getInstance().getAika());
		return asiakas;
	}
	
	/**
	 * Lisää asiakkaan seuraavalle palvelupisteelle ja käskee kontrollerin päivittää grafiikka 
	 * käyttöliittymässä
	 * @param asiakas Asiakas, joka lisätään seuraavalle palvelupisteelle
	 * @param palvelupiste Palvelupisteen indeksi, jolle asiakas lisätään	
	 */
	private void visualisoiJaLisaaJonon(Asiakas asiakas, int palvelupiste) {
		kontrolleri.visualisoiAsiakas(palvelupiste, palvelupisteet[palvelupiste].getJononPituus());
		palvelupisteet[palvelupiste].lisaaJonoon(asiakas);
	}
	
	/**
	 * Ensin valitsee sopivan kassan, sitten käskee kontrollerin päivittää grafiikka käyttöliittymässä
	 * ja viimeiseksi lisää asiakkaan valitulle kassalle
	 * @param asiakas Kassalle siirrettävä asiakas
	 */
	private void siirraKassalle(Asiakas asiakas) {
		Kassa valikassa = valitseKassa(asiakas, kassat);
		kontrolleri.visualisoiKassanAsiakas(valikassa.getId(), palvelupisteet[valikassa.getId()].getJononPituus(), ((Kassa)palvelupisteet[valikassa.getId()]).getKassaTyyppi());
		valikassa.lisaaJonoon(asiakas);
	}
	
	/**
	 * Valitsee kassan sen perusteella, onko asiakkaalla edellytykset asioida pikakassalla tai 
	 * itsepalvelukassalla ja lopuksi jonojen pituuksia vertaillen
	 * @param a Asiakas, jolle kassa valitaan
	 * @param kassat Kaikki simulaation kassat
	 * @return <code>Kassa</code>, joka valittiin
	 */
	private Kassa valitseKassa(Asiakas a, ArrayList<ArrayList<Kassa>> kassat) {

		ArrayList<Kassa> normiKassat = kassat.get(0);
		Collections.sort(normiKassat);
		Kassa normiKassa = normiKassat.get(0);

		ArrayList<Kassa> pikaKassat = kassat.get(1);
		Collections.sort(pikaKassat);
		Kassa pikaKassa = pikaKassat.get(0);

		ArrayList<Kassa> ipKassat = kassat.get(2);
		Collections.sort(ipKassat);
		Kassa ipKassa = ipKassat.get(0);

		switch (valitseKassaTyyppi(a)) {

		case NORMITAIIP: // Normikassa tai IP
			if (normiKassa.getJononPituus() < ipKassa.getJononPituus()) {
				return normiKassa;
			}
			return ipKassa;
			
		case MIKATAHANSA: // Mikä tahansa kassa
			if (normiKassa.getJononPituus() < pikaKassa.getJononPituus()
					&& normiKassa.getJononPituus() < ipKassa.getJononPituus()) {
				return normiKassa;
			} else if (pikaKassa.getJononPituus() < ipKassa.getJononPituus()) {
				return pikaKassa;
			}
			return ipKassa;

		case NORMITAIPIKA: // Pikakassa tai normikassa
			if (normiKassa.getJononPituus() < pikaKassa.getJononPituus()) {
				return normiKassa;
			}
			return pikaKassa;
			
		default: // Normikassa
			return normiKassa;

		}
	}

	/**
	 * Tarkistaa minkä tyyppisille kassoille asiakkaalla on edyllytykset siirtyä
	 * @param a Tarkasteltava asiakas
	 * @return Int joka vastaa mahdollisia kassoja
	 */
	private int valitseKassaTyyppi(Asiakas a) {

		if (a.getOstoslista().getTuotteita() > IPKASSAMAX && a.isKorttimaksu()) {
			return NORMITAIIP; // Normakassa tai IP
		} else if (a.getOstoslista().getTuotteita() <= IPKASSAMAX && a.isKorttimaksu()) {
			return MIKATAHANSA; // Mikä tahansa kassa
		} else if (a.getOstoslista().getTuotteita() < IPKASSAMAX && !a.isKorttimaksu()) {
			return NORMITAIPIKA; // Pikakassa tai normikassa
		} 
		return NORMI; // Normikassa
	}
	
	public Palvelupiste[] getPalvelupisteet() {
		return palvelupisteet;
	}

	/**
	 * Ajetaan simulaation päättyessä. Tallentaa tiedot tietokantaan, ottaa käyttöön GUI:ssa
	 * simulaationkäynnistysnapin, resetoi palvelupisteiden ID:t, asettaa ajan alkuun ja
	 * luo ilmoituksen käyttöliittymään simulaation päättymisestä
	 */
	public void loppuToimenpiteet() {
		tulokset();
		kontrolleri.otaKayttoonKaynnistys();
		Palvelupiste.seuraavaid = 0;
		Kello.getInstance().setAika(0);
		kontrolleri.simulaatioValmisIlmoitus();
	}
	
	/**
	 * Tulostaa tulokset konsoliin sekä tallentaa simulaation tulokset tietokantaan
	 */
	@Override
	protected void tulokset() {
		SimulaationTulokset tulokset = new SimulaationTulokset(
				Kello.getInstance().getAika(), 
				((Haahuilu)palvelupisteet[0]).getAsiakkaidenKokonoisamaara(), 
				KORTTIMAKSUNTN, 
				ruuhkanmaara);

		tuloksetDAO.tallennaTulos(tulokset);
		for (Palvelupiste p : palvelupisteet) {
			tulokset.lisaaPalvelupisteenTulos(p.getPalvelupisteenTulos(tulokset));
			System.out.println(p);
		}
		tuloksetDAO.tallennaTulos(tulokset);
	}

}
