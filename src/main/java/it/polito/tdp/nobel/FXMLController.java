package it.polito.tdp.nobel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.nobel.model.Esame;
import it.polito.tdp.nobel.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController 
{
	private Model model;
	
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtInput;
    
    @FXML
    private TextArea txtResult;

    @FXML
    private Button btnReset;

    @FXML
    void doCalcolaCombinazione(ActionEvent event) 
    {
    	String input = this.txtInput.getText();
    	
    	if(!input.matches("[\\d]+"))
    	{
    		this.txtResult.setText("Inserire un numero intero di crediti > 0");
    		return;
    	}
    	
    	int numeroCrediti;

    	try 
    	{
    		 numeroCrediti = Integer.parseInt(input);
    	} 
    	catch (NumberFormatException e) 
    	{
    		this.txtResult.setText("errore di formato dell'input");
    		return;
    	}
    	
    	long start = System.nanoTime();
    	Set<Set<Esame>> soluzioniMigliori = this.model.migliorSottoinsiemeEsamiDa(numeroCrediti);
    	long end = System.nanoTime();
    		
    	if(soluzioniMigliori == null || soluzioniMigliori.isEmpty()) 
    	{
    		this.txtResult.setText(String.format("Non esistono soluzioni per %d crediti\n", numeroCrediti));
    		return;
    	}
    		
    	StringBuilder sb = new StringBuilder();
    	
    	double time = ((double)(end-start))/Math.pow(10, 6);
    	String timeMs = String.format("%.3f", time);
    	sb.append("TEMPO IMPIEGATO: ").append(timeMs).append(" ms\n\n");
    	
    	Set<Esame> firstSolution = soluzioniMigliori.iterator().next();
    	String mediaMassima = String.format("%.3f", this.calcolaMedia(firstSolution));
   		sb.append("MEDIA MASSIMA: ").append(mediaMassima).append("\n\n");	
    	
    	int numSoluzioni = soluzioniMigliori.size();
    	int count = 0;
    	if(numSoluzioni > 1)
    	{
    		sb.append(numSoluzioni).append(" soluzioni migliori trovate!\n\n");
    	}
    	
    	for(Set<Esame> soluzione : soluzioniMigliori)
    	{
    		if(numSoluzioni > 1)
    		{
    			sb.append("--- Soluzione ").append(++count).append(" ---").append("\n");
    		}
    		sb.append(this.stampaEsami(soluzione)).append("\n");
    	}
    	
    	this.txtResult.setText(sb.toString()); 	
    }
    
    private double calcolaMedia(Set<Esame> esami)
    {
    	int punteggio = 0;
    	int crediti = 0;
    	
    	for(Esame e : esami)
    	{
    		punteggio += e.getVoto() * e.getCrediti();
    		crediti += e.getCrediti();
    	}
    	
    	double media = crediti == 0 ? 0.0 : (double)punteggio/(double)crediti;
    	return media;
    }
    
    private String stampaEsami(Set<Esame> esami)
    {
    	StringBuilder sb = new StringBuilder();
    	for(Esame e : esami) 
    	{
    		sb.append(e.toString()).append("\n");
    	}
    	return sb.toString();
    }

    @FXML
    void doReset(ActionEvent event) 
    {
    	// reset the UI
    	this.txtInput.clear();
    	this.txtResult.clear();
    }

    @FXML
    void initialize() 
    {
        assert txtInput != null : "fx:id=\"txtInput\" was not injected: check your FXML file 'VotiNobel.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'VotiNobel.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'VotiNobel.fxml'.";
    }

	public void setModel(Model model) 
	{
		this.model = model;
	}
}
