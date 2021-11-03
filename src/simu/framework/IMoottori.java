package simu.framework;

import simu.model.Palvelupiste;

public interface IMoottori {
	public void setSimulointiaika(double aika);
	public void setViive(long aika);
	public long getViive();
	public Palvelupiste[] getPalvelupisteet();
}
