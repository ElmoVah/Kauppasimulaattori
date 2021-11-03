package simu.framework;

import simu.model.Palvelupiste;
import controller.IKontrolleri;
import simu.model.Haahuilu;

public abstract class Moottori extends Thread implements IMoottori {
	
	private double simulointiaika = 0;
	private long viive = 0;
	
	private Kello kello;
	
	protected Tapahtumalista tapahtumalista;
	protected Palvelupiste[] palvelupisteet;
	protected IKontrolleri kontrolleri;
	

	public Moottori(IKontrolleri kontrolleri) {
		this.kontrolleri = kontrolleri;
		kello = Kello.getInstance();
		tapahtumalista = new Tapahtumalista();
	}

	public void setSimulointiaika(double aika) {
		simulointiaika = aika;
	}
	
	
	public void run(){
		alustukset();
		while (simuloidaan()){
			viive();
			Trace.out(Trace.Level.INFO, "\nA-vaihe: kello on " + nykyaika());
			kello.setAika(nykyaika());
			kontrolleri.naytaNykyaika(nykyaika());
			Trace.out(Trace.Level.INFO, "\nB-vaihe:" );
			suoritaBTapahtumat();
			Trace.out(Trace.Level.INFO, "\nC-vaihe:" );
			yritaCTapahtumat();
		}
		loppuToimenpiteet();
	}
	
	private void suoritaBTapahtumat(){
		while (tapahtumalista.getSeuraavanAika() == kello.getAika()){
			suoritaTapahtuma(tapahtumalista.poista());
		}
	}

	private void yritaCTapahtumat(){
		for (Palvelupiste p: palvelupisteet){
			if (!p.onVarattu() && p.onJonossa() || p instanceof Haahuilu && p.onJonossa()){
				p.aloitaPalvelu();
			}
		}
	}

	@Override
	public void setViive(long viive) {
		this.viive = viive;
		kontrolleri.naytaNykyviive(viive);
	}
	
	@Override
	public long getViive() {
		return viive;
	}
	
	private double nykyaika(){
		return tapahtumalista.getSeuraavanAika();
	}
	
	private boolean simuloidaan(){
		return kello.getAika() < simulointiaika;
	}
	
	private void viive() {
		Trace.out(Trace.Level.INFO, "Viive " + viive);
		try {
			sleep(viive);
		} catch (InterruptedException e) {
			Trace.out(Trace.Level.ERR, "Viiveessä tapahtui virhe.");
		}
	}

	protected abstract void alustukset(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
	protected abstract void suoritaTapahtuma(Tapahtuma t);  // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
	protected abstract void tulokset(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa	
	
	protected abstract void loppuToimenpiteet();
}