package view;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;

import controller.IKontrolleri;
import controller.Kontrolleri;
import dao.PalvelupisteenTulos;
import dao.SimulaationTulokset;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import simu.framework.Trace;
import simu.framework.Trace.Level;
import simu.model.KassaTyyppi;
import simu.model.PalvelupisteTyyppi;

public class SimulaattorinGUI extends Application implements ISimulaattorinUI {
	private IKontrolleri kontrolleri;
	private ListView<String> saapumisvalitListView;
	private ListView<String> simulaatiotListView;
	private Button kaynnistanappi;
	private Button nopeutaButton;
	private Button hidastaButton;
	private Button ajaloppuunButton;
	private TextField aikaField;
	private TextField viiveField;
	private TextField normikassatField;
	private TextField pikakassatField;
	private TextField ipkassatField;
	private TextField korttimaksuField;
	private Label aikaLabel;
	private Label viiveLabel;
	private Label nykyaikaLabel;
	private Label tulosLabel;
	private Label nykyviiveLabel;
	private Label nykyviiveTulos;
	private Label normikassatLabel;
	private Label pikakassatLabel;
	private Label ipkassatLabel;
	private Label korttimaksuLabel;
	private Label saapumisvalitLabel;
	private ObservableList<String> simulaatiot;
	
	//ikonit
		private Image ostoskori = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/ostoskori.png"));
		private Image liha = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/liha.png"));
		private Image kala = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/kala.png"));
		private Image juusto = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/juusto.png"));
		private Image normikassa = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/normikassa.png"));
		private Image ipkassa = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/ipkassa.png"));
		private Image pikakassa = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/pikakassa.png"));
		private Image ruksi = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/ruksi.png"));
		private Image ostoskori_mini = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/ostoskori_mini.png"));
		private Image liha_mini = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/liha_mini.png"));
		private Image kala_mini = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/kala_mini.png"));
		private Image juusto_mini = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/juusto_mini.png"));
		private Image normikassa_mini = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/normikassa_mini.png"));
		private Image pikakassa_mini = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/pikakassa_mini.png"));
		private Image ipkassa_mini = new Image(SimulaattorinGUI.class.getResourceAsStream("/images/ipkassa_mini.png"));
	//olio asiakasjonoille
		private FlowPane jono[] = null;
		private Label luvut[] = null;
		private ImageView ikonit[] = null;

	/**
	 * Alustaa ohjelman	
	 */
	@Override
	public void init(){	
		
		Trace.setTraceLevel(Level.INFO);	
		kontrolleri = new Kontrolleri(this);
	}
	
	/**
	 * Luo käyttöliittymän
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
						
			primaryStage.setTitle("Kauppasimulaattori");
			
			HBox hBox = createHBox();
			MenuBar menuBar = createMenuBar(primaryStage);
	        VBox vBox = new VBox(menuBar, hBox);
			Scene scene = new Scene(vBox);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        hBox.requestFocus();
	        	        
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Luo ja palauttaa valikkopalkin
	 * @param primaryStage käytössä oleva <code>Stage</code>
	 * @return valikkopalkki
	 */
	private MenuBar createMenuBar(Stage primaryStage) {
		MenuBar palautettavaMenuBar = new MenuBar();
		
		Menu info = new Menu("Tietopankki");
		MenuItem tietoja = luoTietoja(primaryStage);
		MenuItem tulokset = luoTulokset(primaryStage);
		
		info.getItems().add(tietoja);
		info.getItems().add(tulokset);
		
		palautettavaMenuBar.getMenus().add(info);
		
		return palautettavaMenuBar;
	}
	
	/**
	 * Luo valikkoon kohdan "Tulokset" ja palauttaa <code>MenuItem</code> -olion
	 * @param primaryStage käyttöliittymän <code>Stage</code>
	 * @return valikon kohta "Tulokset" <code>MenuItem</code> -oliona
	 */
	private MenuItem luoTulokset(Stage primaryStage) {
		MenuItem tulokset = new MenuItem("Tulokset");
		
		tulokset.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				StackPane tuloksetNakyma = new StackPane();

				Scene tuloksetScene = new Scene(tuloksetNakyma, 300, 500);

				Stage tuloksetIkkuna = new Stage();
				tuloksetIkkuna.setTitle("Simulaatioiden tulokset");
				tuloksetIkkuna.setScene(tuloksetScene);
				tuloksetIkkuna.initModality(Modality.WINDOW_MODAL);
				tuloksetIkkuna.initOwner(primaryStage);
				tuloksetIkkuna.setX(primaryStage.getX() + 600);
				tuloksetIkkuna.setY(primaryStage.getY() + 100);
				
				VBox vBox = new VBox();
				simulaatiotListView = new ListView<String>();
				simulaatiot = FXCollections.observableArrayList();
				simulaatiotListView.setPrefSize(100,500);
				lisaaSimulaatiot();
				
				simulaatiotListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			        @Override
			        public void handle(MouseEvent event) {
			        	int indeksi = simulaatiotListView.getSelectionModel().getSelectedIndex();
			        	SimulaationTulokset[] tulokset = kontrolleri.lueTulokset();
			        	simulaationTiedot(tuloksetIkkuna, tulokset[indeksi]);
			        }
			    });
				
				vBox.getChildren().addAll(new Label("Simulaatiot"), simulaatiotListView);
				tuloksetNakyma.getChildren().addAll(vBox);
				tuloksetIkkuna.show();
			}
		});
		
		return tulokset;
	}
	
	/**
	 * Lisää simulaation tulokset taulukkoon
	 */
	public void lisaaSimulaatiot() {
		SimulaationTulokset[] tulokset = kontrolleri.lueTulokset();
				
		for (SimulaationTulokset s : tulokset) {			
			simulaatiot.add("ID: "+s.getId()+" PVM ja aika: "+s.getPvm());
		}
		
		simulaatiotListView.setItems(simulaatiot);
	}
	
	/**
	 * Luo valikkoon kohdan "Tietoja" ja palauttaa <code>MenuItem</code> -olion
	 * @param primaryStage käyttöliittymän <code>Stage</code>
	 * @return valikon kohta "Tietoja" <code>MenuItem</code> -oliona
	 */
	private MenuItem luoTietoja(Stage primaryStage) {
		MenuItem tietoja = new MenuItem("Tietoja");
		
		tietoja.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				StackPane tietojaNakyma = new StackPane();

				Scene tietojaScene = new Scene(tietojaNakyma, 625, 600);

				Stage tietojaIkkuna = new Stage();
				tietojaIkkuna.setTitle("Simulaattorista tietoa");
				tietojaIkkuna.setScene(tietojaScene);
				tietojaIkkuna.setX(primaryStage.getX() + 600);
				tietojaIkkuna.setY(primaryStage.getY() + 100);

				VBox vBox = new VBox();
				Label tietojaOtsikkoLabel = new Label("Simulaattori-info");
				tietojaOtsikkoLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 25));
				tietojaOtsikkoLabel.setPadding(new Insets(10));
				
				Label infoLabel = new Label(
						"Tämän simulaattorin on tarkoitus mallintaa ruokakauppaa "
						+ "tavalla, johon käyttäjä voi vaikuttaa\nmuuttamalla mallille "
						+ "annettavia argumentteja, kuten simulaation kokonaisaikaa, "
						+ "kassojen määrää,\nkorttimaksun todennäköisyyttä ja ruuhkan "
						+ "määrää kaupassa.\n\nSimulaation käynnistyessä kauppaan saapuu "
						+ "asiakkaita, jotka alussa siirtyvät itsenäisesti keräilemään "
						+ "heidän\nostoslistalla olevia tuotteita. Jos ostoslistalla on "
						+ "joko lihaa, kalaa tai juustoa, joutuu asiakas myös\nvierailemaan "
						+ "vastaavalla palvelutiskillä. Kun asiakas on kerännyt kaiken tarvittavan, "
						+ "siirtyy hän kassalle,\njolle hänellä on edellytykset siirtyä, ja jolla "
						+ "on sillä hetkellä vähiten jonoa. Normaalille kassalle pääsee jokainen\nasiakas, "
						+ "pikakassalle pääsee ainoastaan jos on 5 ostosta tai vähemmän, ja itsepalvelu"
						+ "kassalle voi siirtyä vain jos\npystyy maksamaan kortilla."
						+ "\n\nSimulaattorilla voidaan mallintaa kononainen, "
						+ "noin 14 tunnin aukiolopäivä (50 000 sekuntia).\nTarkoituksena on myös "
						+ "pystyä tarkastelemaan eri ajojen välisiä tulosten muutoksia tietokantaan "
						+ "tallennetuista\ntuloksista. Näitä simulaattorin eri ajokertojen tuloksia "
						+ "voi tarkastella välilehdeltä 'Tulokset'.");
				infoLabel.setPadding(new Insets(10));
				
				GridPane infoGrid = new GridPane();
				infoGrid.setPadding(new Insets(10));
				infoGrid.setVgap(10);
				infoGrid.setHgap(10);
				
				ImageView kerailyKuva = new ImageView(ostoskori_mini);
				GridPane.setConstraints(kerailyKuva, 0, 1);
				
				Label kerailyLabel = new Label("Kuvastaa itsenäisesti ostoksia keräileviä asiakkaita");
				kerailyLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(kerailyLabel, 1, 1);
				
				ImageView lihaKuva = new ImageView(liha_mini);
				GridPane.setConstraints(lihaKuva, 0, 2);
				
				Label lihaLabel = new Label("Kuvastaa lihatiskillä asioivia asiakkaita");
				lihaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(lihaLabel, 1, 2);
				
				ImageView kalaKuva = new ImageView(kala_mini);
				GridPane.setConstraints(kalaKuva, 0, 3);
				
				Label kalaLabel = new Label("Kuvastaa kalatiskillä asioivia asiakkaita");
				kalaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(kalaLabel, 1, 3);
				
				ImageView juustoKuva = new ImageView(juusto_mini);
				GridPane.setConstraints(juustoKuva, 0, 4);
				
				Label juustoLabel = new Label("Kuvastaa juustotiskillä asioivia asiakkaita");
				juustoLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(juustoLabel, 1, 4);
				
				ImageView normikassaKuva = new ImageView(normikassa_mini);
				GridPane.setConstraints(normikassaKuva, 0, 5);
				
				Label normikassaLabel = new Label("Kuvastaa normaalilla kassalla asioivia asiakkaita");
				normikassaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(normikassaLabel, 1, 5);
				
				ImageView pikakassaKuva = new ImageView(pikakassa_mini);
				GridPane.setConstraints(pikakassaKuva, 0, 6);
				
				Label pikakassaLabel = new Label("Kuvastaa pikakassalla asioivia asiakkaita");
				pikakassaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(pikakassaLabel, 1, 6);
				
				ImageView ipkassaKuva = new ImageView(ipkassa_mini);
				GridPane.setConstraints(ipkassaKuva, 0, 7);
				
				Label ipkassaLabel = new Label("Kuvastaa itsepalvelukassalla asioivia asiakkaita");
				ipkassaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(ipkassaLabel, 1, 7);
				
				infoGrid.getChildren().addAll(
						kerailyKuva, lihaKuva, kalaKuva, juustoKuva, normikassaKuva,
						pikakassaKuva, ipkassaKuva, kerailyLabel, lihaLabel, kalaLabel,
						juustoLabel, normikassaLabel, pikakassaLabel, ipkassaLabel);

				vBox.getChildren().addAll(tietojaOtsikkoLabel, infoLabel, infoGrid);
				tietojaNakyma.getChildren().add(vBox);
				tietojaIkkuna.show();
			}
		});
		
		return tietoja;
	}
	
	/**
	 * Näyttää simulaation tiedot ponnahdusikkunassa
	 * @param listViewStage <code>Stage</code> jossa on lista simulaatioista
	 * @param tulokset näytettävän simulaation tulokset
	 */
	private void simulaationTiedot(Stage listViewStage, SimulaationTulokset tulokset) {
		VBox vBox = new VBox();
		StackPane simulaationTiedotNakyma = new StackPane();
		PalvelupisteenTulos[] palvelupisteet = kontrolleri.luePalvelupisteet(tulokset);

		Scene simulaationTiedotScene = new Scene(simulaationTiedotNakyma, 700, 800);
		GridPane yleisTiedotGrid = new GridPane();
		
		yleisTiedotGrid.setPadding(new Insets(10,10,10,10));
		yleisTiedotGrid.setVgap(10);
		yleisTiedotGrid.setHgap(0);
		
		Label yleistiedotLabel = new Label("Simulaation yleiset tiedot");
		yleistiedotLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		yleistiedotLabel.setPadding(new Insets(10));
		
		Label simulaationId = new Label("Simulaation ID: ");
		simulaationId.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationId, 0, 1);
		
		Label simulaationIdTulos = new Label(""+tulokset.getId());
		simulaationIdTulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationIdTulos, 1, 1);
		
		Label simulaationPvm = new Label("Simulaation PVM: ");
		simulaationPvm.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationPvm, 0, 2);
		
		Label simulaationPvmTulos = new Label(""+tulokset.getPvm());
		simulaationPvmTulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationPvmTulos, 1, 2);
		
		Label simulaationAsiakkaat = new Label("Asiakaslukumäärä: ");
		simulaationAsiakkaat.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationAsiakkaat, 0, 3);
		
		Label simulaationAsiakkaatTulos = new Label(""+tulokset.getAsiakkaatLkm());
		simulaationAsiakkaatTulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationAsiakkaatTulos, 1, 3);
		
		Label simulaationKokonaisaika = new Label("Kokonaisaika: ");
		simulaationKokonaisaika.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationKokonaisaika, 0, 4);
		
		Label simulaationKokonaisaikaTulos = new Label(String.format("%.2f",tulokset.getSimuloinninKokonaisaika()));
		simulaationKokonaisaikaTulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationKokonaisaikaTulos, 1, 4);
		
		Label simulaationRuuhka = new Label("Ruuhkan määrä: ");
		simulaationRuuhka.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationRuuhka, 0, 5);
		
		Label simulaationRuuhkaTulos = new Label(""+tulokset.getRuuhka());
		simulaationRuuhkaTulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(simulaationRuuhkaTulos, 1, 5);
		
		Button poistaTulosButton = new Button("Poista tämä tulos");
		GridPane.setConstraints(poistaTulosButton, 0, 6);
		poistaTulosButton.setOnAction(e -> {
			kontrolleri.poistaTulos(tulokset.getId());
			Stage suljettavaStage = (Stage) poistaTulosButton.getScene().getWindow();
			suljettavaStage.close();
			simulaatiotListView.getItems().clear();
			lisaaSimulaatiot();
		});
		
		GridPane palvelupisteTiedotGrid = new GridPane();
		palvelupisteTiedotGrid.setPadding(new Insets(10,10,10,10));
		palvelupisteTiedotGrid.setVgap(10);
		palvelupisteTiedotGrid.setHgap(10);
		
		Label palvelupisteTiedotLabel = new Label("Palvelupisteiden tiedot");
		palvelupisteTiedotLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		palvelupisteTiedotLabel.setPadding(new Insets(10));
		
		Label palvelupisteLabel = new Label("Tyyppi");
		palvelupisteLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
		GridPane.setConstraints(palvelupisteLabel, 0, 1);
		
		Label palvelupisteKassatyyppiLabel = new Label("Kassatyyppi");
		palvelupisteKassatyyppiLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
		GridPane.setConstraints(palvelupisteKassatyyppiLabel, 1, 1);
		
		Label palvelupistePalveluaikaLabel = new Label("Keskim.\npalveluaika");
		palvelupistePalveluaikaLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
		GridPane.setConstraints(palvelupistePalveluaikaLabel, 2, 1);
		
		Label palvelupisteJononpituusLabel = new Label("Keskim.\njonon pituus");
		palvelupisteJononpituusLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
		GridPane.setConstraints(palvelupisteJononpituusLabel, 3, 1);
		
		Label palvelupistekeskLapimenoaikaLabel = new Label("Keskim.\nlapimenoaika");
		palvelupistekeskLapimenoaikaLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
		GridPane.setConstraints(palvelupistekeskLapimenoaikaLabel, 4, 1);
		
		Label palvelupisteKayttoasteLabel = new Label("Käyttöaste");
		palvelupisteKayttoasteLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
		GridPane.setConstraints(palvelupisteKayttoasteLabel, 5, 1);
		
		Label palvelupisteAsiakkaatLabel = new Label("Palvellut\nasiakkaat");
		palvelupisteAsiakkaatLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 15));
		GridPane.setConstraints(palvelupisteAsiakkaatLabel, 6, 1);
		
		int l = 0;
		int j = 5;
		
		Arrays.sort(palvelupisteet, new Comparator<PalvelupisteenTulos>() {
			@Override
	        public int compare(PalvelupisteenTulos pt1, PalvelupisteenTulos pt2) {
				return pt1.getPalvelupistetyyppi().compareTo(pt2.getPalvelupistetyyppi());
			}
		});
		
		for (PalvelupisteenTulos pt : palvelupisteet) {
			
			Label ppTyyppiLabel = new Label(""+pt.getPalvelupistetyyppi());
			ppTyyppiLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
			GridPane.setConstraints(ppTyyppiLabel, l, j);
			palvelupisteTiedotGrid.getChildren().addAll(ppTyyppiLabel);
			
			l++;
			
			if(pt.getKassaTyyppi() != null) {
				Label kassatyyppiLabel = new Label(""+pt.getKassaTyyppi());
				kassatyyppiLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
				GridPane.setConstraints(kassatyyppiLabel, l, j);
				palvelupisteTiedotGrid.getChildren().addAll(kassatyyppiLabel);
			}
			
			l++;
			
			Label palveluAikaLabel = new Label(""+pt.getKeskPalveluaika()+"s");
			palveluAikaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
			GridPane.setConstraints(palveluAikaLabel, l, j);
			palvelupisteTiedotGrid.getChildren().addAll(palveluAikaLabel);
			
			l++;
			
			Label jononPituusLabel = new Label(String.format("%.2f",pt.getKeskJononPituus()));;
			jononPituusLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
			GridPane.setConstraints(jononPituusLabel, l, j);
			palvelupisteTiedotGrid.getChildren().addAll(jononPituusLabel);
			
			l++;
			
			Label jononKeskimaarainenKayttoaste = new Label(String.format("%.2fs",pt.getKeskLapimenoaika()));;
			jononKeskimaarainenKayttoaste.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
			GridPane.setConstraints(jononKeskimaarainenKayttoaste, l, j);
			palvelupisteTiedotGrid.getChildren().addAll(jononKeskimaarainenKayttoaste);
			
			l++;
			
			Label kayttoasteLabel = new Label(String.format("%.2f", pt.getKayttoaste()));
			kayttoasteLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
			GridPane.setConstraints(kayttoasteLabel, l, j);
			palvelupisteTiedotGrid.getChildren().addAll(kayttoasteLabel);
			
			l++;
			
			Label palvellutAsiakkaatLabel = new Label(""+pt.getPalvellut());
			palvellutAsiakkaatLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
			GridPane.setConstraints(palvellutAsiakkaatLabel, l, j);
			palvelupisteTiedotGrid.getChildren().addAll(palvellutAsiakkaatLabel);
			
			l = 0;
			j++;
		}
		
		Stage simulaationTiedotIkkuna = new Stage();
		simulaationTiedotIkkuna.setTitle("Simulaation tiedot");
		simulaationTiedotIkkuna.setScene(simulaationTiedotScene);
		simulaationTiedotIkkuna.setX(listViewStage.getX());
		simulaationTiedotIkkuna.setY(listViewStage.getY());
		
		simulaationTiedotIkkuna.initOwner(listViewStage);
		simulaationTiedotIkkuna.initModality(Modality.NONE);
		
		yleisTiedotGrid.getChildren().addAll(
				yleistiedotLabel, simulaationId, simulaationIdTulos,
				simulaationPvm, simulaationPvmTulos, simulaationAsiakkaat,
				simulaationAsiakkaatTulos, simulaationKokonaisaika,
				simulaationKokonaisaikaTulos, simulaationRuuhka, 
				simulaationRuuhkaTulos, poistaTulosButton);
		
		palvelupisteTiedotGrid.getChildren().addAll(
				palvelupisteLabel, palvelupisteKayttoasteLabel,
				palvelupistePalveluaikaLabel, palvelupisteJononpituusLabel, 
				palvelupistekeskLapimenoaikaLabel, palvelupisteKassatyyppiLabel, 
				palvelupisteAsiakkaatLabel);
		
		vBox.getChildren().addAll(yleistiedotLabel, yleisTiedotGrid, palvelupisteTiedotLabel, palvelupisteTiedotGrid);
		simulaationTiedotNakyma.getChildren().addAll(vBox);
		simulaationTiedotIkkuna.show();
	}
	
	/**
	 * Luo <code>HBox</code> -olion, joka sisältää komponentit käyttäjän syötteille ja asiakkaiden visualisoinnille
	 * @return 
	 */
	private HBox createHBox() {
		HBox palautettavahBox = new HBox();
		// Containeri kontrolleille ja syötteille
		VBox vBox = new VBox();
		palautettavahBox.setPadding(new Insets(15, 15, 15, 15));
		palautettavahBox.setSpacing(10);
		vBox.setPadding(new Insets(15, 15, 15, 15));
		vBox.setSpacing(10);

		// Tiedonsyöttöelementit
		GridPane tiedotGrid = new GridPane();
		tiedotGrid.setPadding(new Insets(10,10,10,10));
		tiedotGrid.setVgap(10);
		tiedotGrid.setHgap(10);
		
		aikaLabel = new Label("Simulointiaika(s):");
		aikaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		GridPane.setConstraints(aikaLabel, 0, 0);
		
        aikaField = new TextField();
        aikaField.setPromptText("Min 1 000, Max 50 000");
        aikaField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        aikaField.setPrefWidth(150);
        GridPane.setConstraints(aikaField, 1, 0);
        
        viiveLabel = new Label("Viive(ms):");
        viiveLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(viiveLabel, 0, 1);
        
        viiveField = new TextField();
        viiveField.setPromptText("Min 0, Max 3 000");
        viiveField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        viiveField.setPrefWidth(150);
        GridPane.setConstraints(viiveField, 1, 1);
        
        normikassatLabel = new Label("Normikassojen määrä:");
        normikassatLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(normikassatLabel, 0, 2);
        
        normikassatField = new TextField();
        normikassatField.setPromptText("Min 1, max 5");
        normikassatField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(normikassatField, 1, 2);
        
        pikakassatLabel = new Label("Pikakassojen määrä:");
        pikakassatLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(pikakassatLabel, 0, 3);
        
        pikakassatField = new TextField();
        pikakassatField.setPromptText("Min 1, max 2");
        pikakassatField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(pikakassatField, 1, 3);
        
        ipkassatLabel = new Label("Itsepalvelukassojen määrä:");
        ipkassatLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(ipkassatLabel, 0, 4);
        
        ipkassatField = new TextField();
        ipkassatField.setPromptText("Min 1, max 4");
        ipkassatField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(ipkassatField, 1, 4);
        
        korttimaksuLabel = new Label("Korttimaksun todennäköisyys(%):");
        korttimaksuLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(korttimaksuLabel, 0, 5);
        
        korttimaksuField = new TextField();
        korttimaksuField.setPromptText("Min 0, Max 100");
        korttimaksuField.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(korttimaksuField, 1, 5);
        
        saapumisvalitLabel = new Label("Ruuhkan määrä kaupassa:");
        saapumisvalitLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(saapumisvalitLabel, 0, 6);
        
        ObservableList<String> saapumisvalit = FXCollections.observableArrayList();
		saapumisvalit.add("Hiljaista");
		saapumisvalit.add("Normaalia");
		saapumisvalit.add("Ruuhkaa");
		
		saapumisvalitListView = new ListView<String>();
		saapumisvalitListView.setPrefSize(100, 72);
		saapumisvalitListView.setItems(saapumisvalit);
		GridPane.setConstraints(saapumisvalitListView, 1, 6);
        
        nykyaikaLabel = new Label("Kulunut aika sekunteina:");
        nykyaikaLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(nykyaikaLabel, 0, 7);
        
        tulosLabel = new Label();
        tulosLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        tulosLabel.setPrefWidth(150);
        GridPane.setConstraints(tulosLabel, 1, 7);
        
        nykyviiveLabel = new Label("Nykyinen viive(ms):");
        nykyviiveLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(nykyviiveLabel, 0, 8);
        
        nykyviiveTulos = new Label();
        nykyviiveTulos.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
        GridPane.setConstraints(nykyviiveTulos, 1, 8);
        
        
		// Kaikki simulaattorin napit ovat tässä gridissä
		GridPane kontrollitGrid = new GridPane();
		kontrollitGrid.setPadding(new Insets(10,10,10,10));
		kontrollitGrid.setVgap(10);
		kontrollitGrid.setHgap(10);

		nopeutaButton = new Button("Nopeuta");
		GridPane.setConstraints(nopeutaButton, 0, 0);
		nopeutaButton.setOnAction(e -> kontrolleri.nopeuta());
		
		hidastaButton = new Button("Hidasta");
		GridPane.setConstraints(hidastaButton, 1, 0);
		hidastaButton.setOnAction(e -> kontrolleri.hidasta());
		
		ajaloppuunButton = new Button("Aja loppuun");
		GridPane.setConstraints(ajaloppuunButton, 2, 0);
		ajaloppuunButton.setOnAction(e -> kontrolleri.ajaLoppuun());

		kaynnistanappi = new Button("Käynnistä");
		GridPane.setConstraints(kaynnistanappi, 0, 1);
		kaynnistanappi.setOnAction(e -> {
			if(kontrolleri.kaynnistaSimulointi()) {
				kaynnistanappi.setDisable(true);
			}
			
		});
		
		// Lisätään kaikki elementit niille kuuluviin containereihin
		tiedotGrid.getChildren().addAll
		(aikaLabel, aikaField, viiveLabel, viiveField, nykyaikaLabel, tulosLabel,
		 nykyviiveLabel, nykyviiveTulos, normikassatLabel, normikassatField, 
		 pikakassatLabel, pikakassatField, ipkassatLabel, ipkassatField,
		 korttimaksuLabel, korttimaksuField, saapumisvalitLabel, saapumisvalitListView
		 );
		
		kontrollitGrid.getChildren().addAll
		(kaynnistanappi, nopeutaButton, hidastaButton, ajaloppuunButton);
		
		vBox.getChildren().addAll(tiedotGrid, kontrollitGrid);
		
		TilePane kuvaaja = kuvaaja();
		
		palautettavahBox.getChildren().addAll(vBox, kuvaaja);
		
		return palautettavahBox;
	}
	
	/**
	 * Luo <code>TilePane</code> -olion, jossa visualisoidaan simulaation asiakkaat
	 * @return TilePane jossa visualisoidaan asiakkaita
	 */
	private TilePane kuvaaja() {
		TilePane tile = new TilePane();
		tile.setPadding(new Insets(5, 0, 5, 0));
		tile.setVgap(4);
		tile.setHgap(4);
		tile.setPrefColumns(1);
		tile.setStyle( 
                "-fx-border-style: solid inside;" + 
                "-fx-border-width: 5;" +
                "-fx-border-radius: 10;" + 
                "-fx-border-color: white;" +
                "-fx-background-color: DAE6F3;" +
                "-fx-background-radius: 15");
				
		GridPane grid = new GridPane();
		grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(0, 10, 0, 10));
	    
	    
		ikonit = new ImageView[15];
		PalvelupisteTyyppi tyyppi[] = new PalvelupisteTyyppi[15];
		tyyppi[0] = PalvelupisteTyyppi.HAAHUILU;
		tyyppi[1] = PalvelupisteTyyppi.LIHATISKI;
		tyyppi[2] = PalvelupisteTyyppi.KALATISKI;
		tyyppi[3] = PalvelupisteTyyppi.JUUSTOTISKI;
		tyyppi[4] = PalvelupisteTyyppi.KASSA;
		tyyppi[5] = PalvelupisteTyyppi.KASSA;
		tyyppi[6] = PalvelupisteTyyppi.KASSA;
		tyyppi[7] = PalvelupisteTyyppi.KASSA;
		tyyppi[8] = PalvelupisteTyyppi.KASSA;
		tyyppi[9] = PalvelupisteTyyppi.KASSA;
		tyyppi[10] = PalvelupisteTyyppi.KASSA;
		tyyppi[11] = PalvelupisteTyyppi.KASSA;
		tyyppi[12] = PalvelupisteTyyppi.KASSA;
		tyyppi[13] = PalvelupisteTyyppi.KASSA;
		tyyppi[14] = PalvelupisteTyyppi.KASSA;
		
		
		for (int l = 0; l < ikonit.length; l++) {
			switch(tyyppi[l]) {
			
			case HAAHUILU:
				ikonit[l] = new ImageView(ostoskori);
				break;
			case LIHATISKI:
				ikonit[l] = new ImageView(liha);
				break;
			case KALATISKI:
				ikonit[l] = new ImageView(kala);
				break;
			case JUUSTOTISKI:
				ikonit[l] = new ImageView(juusto);
				break;
			case KASSA:
				if(l == 9 || l == 10) {
					ikonit[l] = new ImageView(pikakassa);
				}
				else if(l > 10 && l < 15) {
					ikonit[l] = new ImageView(ipkassa);
				}
				else {
					ikonit[l] = new ImageView(normikassa);
				}
				break;
			}
					
			grid.add(ikonit[l], l, 3);
		}

		luvut = new Label[15];
		
		for(int i = 0; i < luvut.length; i++) {
			luvut[i] = new Label("0");
			luvut[i].setFont(Font.font("Tahoma", FontWeight.BOLD, 30));
			luvut[i].setMaxWidth(Double.MAX_VALUE);
	        luvut[i].setAlignment(Pos.CENTER);
			grid.add(luvut[i], i, 2);
		}
		
		jono = new FlowPane[ikonit.length];
		
		for(int i = 0; i < jono.length; i++) {
			jono[i] = new FlowPane(Orientation.VERTICAL);	
			jono[i].setVgap(10);
			jono[i].setAlignment(Pos.BOTTOM_CENTER);
		    jono[i].setPrefWrapLength(600);
		    
		    grid.add(jono[i], i, 1);
		}	
		
		tile.getChildren().add(grid);
		
		return tile;
	}
	
	/**
	 * Lisää asiakkaan palvelupisteen jonoon graafisessa käyttöliittymässä
	 * @param index palvelupisteen indeksi
	 * @param maara asiakkaiden määrä jonossa
	 */
	public void lisaaJonoon(int index, int maara) {
		if(maara < 8) {
			jono[index].getChildren().add(new ImageView(
	    			new Image(SimulaattorinGUI.class.getResourceAsStream("/images/asiakas.png")))); 
		}
		luvut[index].setText(""+(++maara));
	}
	
	/**
	 * Lisää asiakkaan kassan jonoon graafisessa käyttöliittymässä
	 * @param id kassan id
	 * @param maara asiakkaiden määrä jonossa
	 * @param kassatyyppi kassatyyppi
	 */
	public void lisaaKassaJonoon(int id, int maara, KassaTyyppi kassatyyppi) {
		final int NORMIKASSATMAX = 5;
		final int PIKAKASSATMAX = 2;
		int index = 0;
		int normiKassat = getNormiKassat();
		int pikaKassat = getPikaKassat();
		
		switch(kassatyyppi) {
		case NORMAALI:
			index = id;
			break;
		case PIKA:
			index = NORMIKASSATMAX - normiKassat + id;
			break;
		case ITSEPALVELU:
			index = NORMIKASSATMAX - normiKassat + PIKAKASSATMAX - pikaKassat + id;
			break;
		}
		
		if(maara < 8) {
			jono[index].getChildren().add(new ImageView(
	    			new Image(SimulaattorinGUI.class.getResourceAsStream("/images/asiakas.png")))); 
		}
		luvut[index].setText(""+(++maara));
	}
	
	/**
	 * Poistaa asiakkaan palvelupisteen jonosta graafisessa käyttöliittymässä
	 * @param index palvelupisteen indeksi
	 * @param maara asiakkaiden määrä jonossa
	 */
	public void poistaJonosta(int index, int maara) {
		if(maara > 0 && maara <= 8) {
			jono[index].getChildren().remove(0);
		}
		luvut[index].setText(""+(--maara));
	}
	
	/**
	 * Tyhjentää kaikkien palvelupisteiden jonot graafisessa käyttöliittymässä
	 */
	public void poistaKaikkiJonosta() {
		for (FlowPane fp : jono) {
			fp.getChildren().clear();
		}
		for (Label l : luvut) {
			l.setText(""+0);
		}
	}
	
	/**
	 * Poistaa asiakkaan kassan jonosta graafisessa käyttöliittymässä
	 * @param id kassan id
	 * @param maara asiakkaiden määrä jonossa
	 * @param kassatyyppi kassatyyppi
	 */
	public void poistaKassaJonosta(int id, int maara, KassaTyyppi kassatyyppi) {
		final int NORMIKASSATMAX = 5;
		final int PIKAKASSATMAX = 2;
		int index = 0;
		int normiKassat = getNormiKassat();
		int pikaKassat = getPikaKassat();
		
		switch(kassatyyppi) {
		case NORMAALI:
			index = id;
			break;
		case PIKA:
			index = NORMIKASSATMAX - normiKassat + id;
			break;
		case ITSEPALVELU:
			index = NORMIKASSATMAX - normiKassat + PIKAKASSATMAX - pikaKassat + id;
			break;
		}
		if(maara > 0 && maara <= 8) {
			jono[index].getChildren().remove(0);
		}
		luvut[index].setText(""+(--maara));
	}
	
	/**
	 * Asettaa graafiseen käyttöliittymään näkyville simulaation ajan
	 * @param aika simulaation aika
	 */
	@Override
	public void setNykyAika(double aika) {
		DecimalFormat formatter = new DecimalFormat("#0.00");
		this.tulosLabel.setText(formatter.format(aika));
	}
	
	/**
	 * Asettaa graafiseen käyttöliittymään näkyville simulaation viiveen
	 * @param viive simulaation viive
	 */
	@Override
	public void setNykyViive(long viive) {
		this.nykyviiveTulos.setText(Long.toString(viive));
	}

	/**
	 * Asettaa <code>kaynnistanappi</code> -elementin käytettäväksi
	 */
	@Override
	public void otaKayttoonKaynnistys() {
		kaynnistanappi.setDisable(false);
	}
	
	/**
	 * Palauttaa käyttäjän asettaman simulaation viiveen
	 */
	@Override
	public long getViive() {
		long viive = -1;
		try {
			viive = Long.parseLong(viiveField.getText());
			if(viive >= 0 && viive <= 3000) {
				return viive;
			} else {
				virheIlmoitus("viive", "väliltä 0-3000 ms");
				return -1;
			}
		} catch(Exception e) {
			virheIlmoitus("viive", "väliltä 0-3000 ms");
		}
		return viive;
	}

	/**
	 * Palauttaa käyttäjän asettaman simulaatioajan
	 */
	@Override
	public double getAika() {
		double aika = -1;
		try {
			aika = Double.parseDouble(aikaField.getText());
			if(aika >= 1000 && aika <= 50000) {
				return aika;
			} else {
				virheIlmoitus("simulaatioaika", "väliltä 1000-50000 sekuntia");
				return -1;
			}
		} catch(Exception e) {
			virheIlmoitus("simulaatioaika", "väliltä 1000-50000 sekuntia");
		}
		return aika;
	}
	
	/**
	 * Palauttaa käyttäjän asettaman normaalien kassojen määrän
	 */
	@Override
	public int getNormiKassat() {
		int normiKassat = -1;
		try {
			normiKassat = Integer.parseInt(normikassatField.getText());
			if(normiKassat >= 1 && normiKassat <= 5) {
				return normiKassat;
			}
			else {
				virheIlmoitus("normikassamäärä", "väliltä 1-5");
				return -1;
			}
		} catch(Exception e) {
			virheIlmoitus("normikassamäärä", "väliltä 1-5");
		}
		// Palauttaa -1 jos syötetty kassamäärä ei ollut validi
		return normiKassat;
	}
	
	/**
	 * Palauttaa käyttäjän asettaman pikakassojen määrän
	 */
	@Override
	public int getPikaKassat() {
		int pikaKassat = -1;
		try {
			pikaKassat = Integer.parseInt(pikakassatField.getText());
			if(pikaKassat == 1 || pikaKassat == 2) {
				return pikaKassat;
			}
			else {
				virheIlmoitus("pikakassamäärä", "väliltä 1-2");
				return -1;
			}
		} catch(Exception e) {
			virheIlmoitus("pikakassamäärä", "väliltä 1-2");
		}
		// Palauttaa -1 jos syötetty kassamäärä ei ollut validi
		return pikaKassat;
	}
	
	/**
	 * Palauttaa käyttäjän asettaman itsepalvelukassojen määrän
	 */
	@Override
	public int getIpKassat() {
		int ipKassat = -1;
		try {
			ipKassat = Integer.parseInt(ipkassatField.getText());
			if(ipKassat >= 1 && ipKassat <= 4) {
				return ipKassat;
			}
			else {
				virheIlmoitus("itsepalvelukassamäärä", "väliltä 1-4");
				return -1;
			}
		} catch(Exception e) {
			virheIlmoitus("itsepalvelukassamäärä", "väliltä 1-4");
		}
		// Palauttaa -1 jos syötetty kassamäärä ei ollut validi
		return ipKassat;
	}
	
	/**
	 * Palauttaa käyttäjän asettaman korttimaksun todennäköisyyden
	 * @return korttimaksun todennäköisyys
	 */
	@Override
	public double getKorttimaksunTN() {
		double korttimaksunTN = -1;
		try {
			korttimaksunTN = Double.parseDouble(korttimaksuField.getText());
			if(korttimaksunTN >= 0 && korttimaksunTN <= 100) {
				return korttimaksunTN;
			} else {
				virheIlmoitus("korttimaksun todennäköisyys", "väliltä 0-100");
				return -1;
			}
		} catch(Exception e) {
			virheIlmoitus("korttimaksun todennäköisyys", "väliltä 0-100");
		}
		return korttimaksunTN;
	}
	
	/**
	 * Palauttaa valitun ruuhkan, tai virheilmoituksen
	 * @return listasta valittu ruuhka
	 */
	@Override 
	public int getRuuhkaMaara() {
		int saapumisvalitIndex = -1;
		try {
			saapumisvalitIndex = saapumisvalitListView.getSelectionModel().getSelectedIndex();
			if(saapumisvalitIndex >= 0 && saapumisvalitIndex <= 2) {
				return saapumisvalitIndex;
			} else {
				virheIlmoitus("ruuhkan määrä", "valitse yksi valikosta");
				return -1;
			}
		} catch(Exception e) {
			virheIlmoitus("ruuhkan määrä", "valitse yksi valikosta");
		}
		
		return saapumisvalitIndex;
	}
	
	/**
	 * Näyttää virheilmoituksen ponnahdusikkunassa
	 * @param syote käyttäjän antama syöte
	 * @param kehotus tieto käyttäjälle, mikä syöte aiheutti virheen ja miten välttää se
	 */
	private void virheIlmoitus(String syote, String kehotus) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Varoitus");
		alert.setHeaderText("Kelvoton "+syote);
		alert.setContentText("Syötä kelvollinen "+syote+" "+kehotus);
		alert.showAndWait();
	}
	
	/**
	 * Näyttää ponnahdusikkunan
	 */
	@Override
	public void simulaatioValmisIlmoitus() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Simulaatio ajettu!");
		alert.setHeaderText("Simulaatio ajettu loppuun!");
		alert.setContentText(
				"Simulaation tulokset tallennettu.\n\n"
				+"Voit tarkastella simulaation tuloksia tietopankkivalikon osiosta 'Tulokset'");
		alert.showAndWait();
	}

	/**
	 * Asettaa ruksit käyttämättömien kassojen ikonien tilalle
	 */
	@Override
	public void asetaRuksit() {
		final int NORMIKASSATMAX = 5;
		final int PIKAKASSATMAX = 2;
		final int IPKASSATMAX = 4;
		final int PALVELUTISKITLKM = 4;
		int normiKassat = getNormiKassat();
		int pikaKassat = getPikaKassat();
		int ipKassat = getIpKassat();
		
		if(normiKassat < NORMIKASSATMAX) {
			for(int i = normiKassat + PALVELUTISKITLKM; i < PALVELUTISKITLKM + NORMIKASSATMAX; i++) {
				ikonit[i].setImage(ruksi);
			}
		}
		if(pikaKassat < PIKAKASSATMAX) {
			for(int i = pikaKassat + PALVELUTISKITLKM + NORMIKASSATMAX; i < PALVELUTISKITLKM + NORMIKASSATMAX + PIKAKASSATMAX; i++) {
				ikonit[i].setImage(ruksi);
			}
		}
		if(ipKassat < IPKASSATMAX) {
			for(int i = ipKassat + NORMIKASSATMAX + PIKAKASSATMAX + PALVELUTISKITLKM; i < PALVELUTISKITLKM + NORMIKASSATMAX + PIKAKASSATMAX + IPKASSATMAX; i++) {
				ikonit[i].setImage(ruksi);
			}
		}
	}
	
	/**
	 * Asettaa kuvaajaan kassatyyppien ikonit
	 */
	@Override
	public void asetaIkonit() {
		for(int i = 4; i < 9; i++) {
			ikonit[i].setImage(normikassa);
		}
		for(int i = 9; i < 11; i++) {
			ikonit[i].setImage(pikakassa);
		}
		for(int i = 11; i < 15; i++) {
			ikonit[i].setImage(ipkassa);
		}
	}
	
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
