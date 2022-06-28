/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.time.Month;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Month> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(!this.model.isGrafo()) {
    		this.txtResult.appendText("Crea prima il grafo!\n");
    		return;
    	}
    	
    	this.txtResult.appendText("Coppie con connessione massima: \n");
    	for(DefaultWeightedEdge edge : this.model.getConnessioneMax()) {
    		this.txtResult.appendText(this.model.getGraph().getEdgeSource(edge) + " - " 
    				+ this.model.getGraph().getEdgeTarget(edge) + " (" 
    				+ this.model.getGraph().getEdgeWeight(edge) + ")\n");
    	}
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	this.cmbM1.getItems().clear();
    	this.cmbM2.getItems().clear();
    	
    	String m = this.txtMinuti.getText();
    	int min = 0;
    	
    	try {
			min = Integer.parseInt(m);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			this.txtResult.appendText("Inserisci un numero minimo di minuti valido!\n");
			return;
		}
    	
    	Month mese = this.cmbMese.getValue();
    	
    	if(mese == null) {
    		this.txtResult.appendText("Seleziona un mese valido!\n");
    		return;
    	}
    	
    	this.model.creaGrafo(mese.getValue(), min);
    	this.txtResult.appendText("GRAFO CREATO!\n");
    	this.txtResult.appendText(this.model.getNVertici() + "\n");
    	this.txtResult.appendText(this.model.getNArchi() + "\n");
    	
    	this.cmbM1.getItems().addAll(this.model.getGraph().vertexSet());
    	this.cmbM2.getItems().addAll(this.model.getGraph().vertexSet());
    	
    	this.cmbM1.setDisable(false);
        this.cmbM2.setDisable(false);
        this.btnCollegamento.setDisable(false);
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	this.txtResult.clear();
    	Match sorgente = this.cmbM1.getValue();
    	Match destinazione = this.cmbM2.getValue();
    	
    	if(sorgente == null || destinazione == null) {
    		this.txtResult.appendText("Seleziona dei match validi!\n");
    		return;
    	}
    	
    	if(sorgente == destinazione) {
    		this.txtResult.appendText("Seleziona due match diversi!\n");
    		return;
    	}
    	
    	this.txtResult.appendText("Cammino: \n");
    	for(Match m : this.model.calcolaPercorso(sorgente, destinazione))
    		this.txtResult.appendText(m + "\n");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

        this.cmbM1.setDisable(true);
        this.cmbM2.setDisable(true);
        this.btnCollegamento.setDisable(true);
    }
    
    public void setModel(Model model) {
    	this.model = model;
  
    	for(int i = 1; i <= 12; i++)
    		this.cmbMese.getItems().add(Month.of(i));
    }
    
    
}
