package dao;

/**
 * 
 * Rajapinta <code>ISimulaationTuloksetDAO</code> toimii rajapintana
 * luokalle <code>SimulaationTuloksetAccessObject</code>, ja määrittelee tälle
 * metodit
 *
 */

public interface ISimulaationTuloksetDAO {
	public boolean tallennaTulos(SimulaationTulokset tulos);
	public SimulaationTulokset[] lueTulokset();
	public boolean poistaTulos(int id);
	public PalvelupisteenTulos[] luePalvelupisteenTulokset(SimulaationTulokset s);
}
