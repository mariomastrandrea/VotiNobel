package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model 
{
	private List<Esame> insiemeEsamiDiPartenza;
	private Set<Esame> soluzioneMigliore;
	private double mediaSoluzioneMigliore;
	private int creditiTotali;
	
	
	public Model()
	{
		EsameDAO dao = new EsameDAO();
		this.insiemeEsamiDiPartenza = dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) 
	{
		Set<Esame> soluzioneParziale = new HashSet<>();
		this.soluzioneMigliore = new HashSet<>();
		this.mediaSoluzioneMigliore = 0.0;
		this.creditiTotali = numeroCrediti;
		
		cercaRicorsivamente3(soluzioneParziale, 0); //<-- *** QUI *** per cambiare il metodo della ricorsione
		
		return this.soluzioneMigliore;	
	}

	@SuppressWarnings("unused")
	/**
	 * metodo iniziale NON ottimizzato, prestazioni peggiori (per 35cfu ~ t=406ms)
	 * @param soluzioneParziale
	 * @param L
	 */
	private void cercaRicorsivamente1(Set<Esame> soluzioneParziale, int L)
	{
		//casi terminali
		
		int crediti = this.sommaCrediti(soluzioneParziale);
		
		if(crediti > this.creditiTotali)
			return;
		
		if(crediti == this.creditiTotali)
		{
			double media = this.calcolaMedia(soluzioneParziale);
			if(media > this.mediaSoluzioneMigliore)
			{
				this.soluzioneMigliore = new HashSet<>(soluzioneParziale);
				this.mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		//sicuramente, crediti < m
		//1) L = N -> non ci sono più esami da aggiungere
		if(L == this.insiemeEsamiDiPartenza.size())
		{
			return;
		}
		
		for(Esame e : insiemeEsamiDiPartenza)
		{
			if(!soluzioneParziale.contains(e))
			{
				soluzioneParziale.add(e);
				cercaRicorsivamente1(soluzioneParziale, L+1);
				soluzioneParziale.remove(e);
			}
		}
		
	}
	
	/**
	 * metodo semi-ottimizzato, con ricerca dicotomica; prestazioni molto migliori 
	 * 					(per 35cfu ~ t=6ms;  per 70cfu ~ t=145ms)
	 * @param soluzioneParziale
	 * @param L
	 */
	@SuppressWarnings("unused")
	private void cercaRicorsivamente2(Set<Esame> soluzioneParziale, int L)
	{
		//casi terminali
		
		int crediti = this.sommaCrediti(soluzioneParziale);
		
		if(crediti > this.creditiTotali)
			return;
		
		if(crediti == this.creditiTotali)
		{
			double media = this.calcolaMedia(soluzioneParziale);
			if(media > this.mediaSoluzioneMigliore)
			{
				this.soluzioneMigliore = new HashSet<>(soluzioneParziale);
				this.mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		//sicuramente, crediti < m
		//1) L = N -> non ci sono più esami da aggiungere
		if(L >= this.insiemeEsamiDiPartenza.size())
		{
			return;
		}
		
		//generazione sotto-problemi
		//partenza[L] è da aggiungere oppure no? Provo entrambe le cose
		soluzioneParziale.add(insiemeEsamiDiPartenza.get(L));
		cercaRicorsivamente2(soluzioneParziale, L+1);
		
		soluzioneParziale.remove(insiemeEsamiDiPartenza.get(L));
		cercaRicorsivamente2(soluzioneParziale, L+1);
	}
	
	/**
	 * >>> metodo ottimizzato, con l'uso di Set. Prestazioni *** MIGLIORI *** in assoluto
	 * 					(per 35cfu ~ t=4ms;  per 70cfu ~ t=106ms)
	 * @param soluzioneParziale 
	 * @param L livello della ricorsione corrente
	 */
	private void cercaRicorsivamente3(Set<Esame> soluzioneParziale, int L)
	{
		//casi terminali
		
		int crediti = this.sommaCrediti(soluzioneParziale);
		
		if(crediti > this.creditiTotali)
			return;
		
		if(crediti == this.creditiTotali)
		{
			double media = this.calcolaMedia(soluzioneParziale);
			if(media > this.mediaSoluzioneMigliore)
			{
				this.soluzioneMigliore = new HashSet<>(soluzioneParziale);
				this.mediaSoluzioneMigliore = media;
			}
			return;
		}
		
		//sicuramente, crediti < m
		//1) L = N -> non ci sono più esami da aggiungere
		if(L == this.insiemeEsamiDiPartenza.size())
		{
			return;
		}
		
		for(int i=L; i<insiemeEsamiDiPartenza.size(); i++)
		{
			Esame e = this.insiemeEsamiDiPartenza.get(i);
			if(!soluzioneParziale.contains(e))
			{
				soluzioneParziale.add(e);
				cercaRicorsivamente3(soluzioneParziale, i+1);	//// <--- passo 'i+1' e non 'L+1', 
																//perché i+1 rappresenta REALMENTE il livello
																//della prossima iterazione (* vedi esempio sotto)
				soluzioneParziale.remove(e); //backtracking
			}
		}
		
	}
	
	
	/* esempio (SENZA ottimizzazione):
	 * partenza = {e1, e2, e3, e4, ...}
	 * 
	 *  L=0
	 * parziale = {e1}
	 * 		- parziale = {e1, e2}
	 * 	 	  L=1
	 * 		  [...]
	 * 
	 *		- parziale = {e1, e3}
	 *	  	  L=1 
	 * 		  [...]
	 * 
	 *  L=0
	 * parziale = {e2}
	 * 		- parziale = {e2, e1} !!! problema -> il livello era L=0, quindi il 
	 * 			L=1								metodo RI-SELEZIONA e1, che è l'esame in posizione 0 
	 * 			[...]						In realtà dovrebbe selezionare e3. Che è l'elemento in posizione 2
	 * 									>> Quindi: non siamo al livello 0, ma al livello 1 (!)
	 * 
	 * SOLUZIONE
	 * 
	 * esempio (CON ottimizzazione):
	 * partenza = {e1, e2, e3, e4, ...}
	 * 
	 *  L=0
	 * parziale = {e1}
	 * 		- parziale = {e1, e2}
	 * 		  L=1 
	 * 		  [...]
	 * 
	 * 		- parziale = {e1, e3}
	 * 		  L=2 (!!!)
	 * 		  [...]
	 * 
	 * L=1 !!!  <---- NON L=0			{ Qui il livello aumenta progressivamente ad ogni ciclo }
	 * parziale = {e2}
	 * 		- parziale = {e2, e3} !!! 
	 * 		  L=2
	 * 		  [...]
	 * 
	 * 		- parziale = {e2, e4} 
	 * 		  L=3   (!)
	 * 		  [...]
	 * 
	 * 		- parziale = {e2, e5}
	 * 	  	  L=4	(!)
	 * 		  [...]
	 * 
	 * [...]
	 */
	
	
	public double calcolaMedia(Set<Esame> esami) 
	{
		
		int crediti = 0;
		int somma = 0;
		
		for(Esame e : esami){
			crediti += e.getCrediti();
			somma += (e.getVoto() * e.getCrediti());
		}
		
		return somma/crediti;
	}
	
	public int sommaCrediti(Set<Esame> esami) 
	{
		int somma = 0;
		
		for(Esame e : esami)
			somma += e.getCrediti();
		
		return somma;
	}

}
