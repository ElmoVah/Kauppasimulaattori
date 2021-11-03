package dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * 
 * Luokka <code>SimulaationTuloksetAccessObject</code> toimii välikätenä ohjelman ja tietokannan välillä
 *
 */

public class SimulaationTuloksetAccessObject implements ISimulaationTuloksetDAO {
	
	/**
	 * Muuttuja <code>istuntotehdas</code> määrittää DAO:lle istuntotehtaan
	 */
	private SessionFactory istuntotehdas = null;
	
	/**
	 * Parametriton konstruktori, joka luo luokalle istuntotehtaan
	 */
	public SimulaationTuloksetAccessObject() {
		try {
			istuntotehdas = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			System.err.println("Instuntotehtaan luonti epäonnistui: " + e.getMessage());
			System.exit(-1);
		}
	}
	
	/**
	 * Destruktori, joka sulkee istuntotehtaan
	 */
	@Override
	protected void finalize() {
		try {
			if(istuntotehdas != null) istuntotehdas.close();
		} catch (Exception e) {
			System.err.println("Istuntotehtaan sulkeminen epäonnistui: " + e.getMessage());
		}
	}

	/**
	 * Tallentaa parametrina välitetyn SimulaationTulokset -olion tietokantaan
	 * @param tulos on tallennettava olio
	 * @return boolean arvo, joka kertoo onnistuiko tallentaminen
	 */
	@Override
	public boolean tallennaTulos(SimulaationTulokset tulos) {
		Transaction transaktio = null;
		try(Session istunto = istuntotehdas.openSession()){
			transaktio = istunto.beginTransaction();
			istunto.saveOrUpdate(tulos);
			transaktio.commit();
			System.out.println("Tallentui!");
			return true;
		} catch (Exception e) {
			if(transaktio != null) transaktio.rollback();
		}
		return false;
	}

	/**
	 * Lukee tietokannasta kaikki tallennetut SimulaationTulokset oliot
	 * @return SimulaationTulokset listana
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SimulaationTulokset[] lueTulokset() {
		List<SimulaationTulokset> lista;
		
		Transaction transaktio = null;
		try(Session istunto = istuntotehdas.openSession()){
			transaktio = istunto.beginTransaction();
			lista = istunto.createQuery("from SimulaationTulokset").getResultList();
			transaktio.commit();
		} catch (Exception e) {
			if (transaktio != null) transaktio.rollback();
			throw e;
		}
		
		SimulaationTulokset[] returnArray = new SimulaationTulokset[lista.size()];
		return lista.toArray(returnArray);
	}
	
	/**
	 * Lukee kaikkien palvelupisteiden tulokset, jotka liittyvät parametrina annettuun
	 * SimulaationTulokset olioon
	 * @param s SimulaatoinTulokset, jonka palvelupisteet halutaan lukea
	 * @return Palvelupisteiden tulokset listana
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PalvelupisteenTulos[] luePalvelupisteenTulokset(SimulaationTulokset s) {
		List<PalvelupisteenTulos> lista;
		
		Transaction transaktio = null;
		try(Session istunto = istuntotehdas.openSession()){
			transaktio = istunto.beginTransaction();
			lista = istunto.createQuery("from PalvelupisteenTulos WHERE simulaationId="+s.getId()).getResultList();
			transaktio.commit();
		} catch (Exception e) {
			if (transaktio != null) transaktio.rollback();
			throw e;
		}
		
		PalvelupisteenTulos[] returnArray = new PalvelupisteenTulos[lista.size()];
		return lista.toArray(returnArray);
	}

	/**
	 * Poistaa tietokannasta yhden tuloksen
	 * @param id Poistettavan tuloksen id
	 * @return boolean arvo, joka kertoo onnistuiko poisto
	 */
	@Override
	public boolean poistaTulos(int id) {
		Transaction transaktio = null;
		try(Session istunto = istuntotehdas.openSession()){
			transaktio = istunto.beginTransaction();
			SimulaationTulokset poistettava = (SimulaationTulokset)istunto.get(SimulaationTulokset.class, id);
			if (poistettava != null) {
				istunto.delete(poistettava);
				transaktio.commit();
				return true;
			} else {
				transaktio.rollback();
				return false;
			}
		} catch (Exception e) {
			if(transaktio != null) transaktio.rollback();
		}
		return false;
	}

}
