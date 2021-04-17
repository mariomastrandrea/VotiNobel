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
		
		cercaRicorsivamente3(soluzioneParziale, 0);
		
		return this.soluzioneMigliore;	
	}

	
	@SuppressWarnings("unused")
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
			soluzioneParziale.add(e);
			cercaRicorsivamente3(soluzioneParziale, L+1);
			soluzioneParziale.remove(e);
		}
		
	}


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
