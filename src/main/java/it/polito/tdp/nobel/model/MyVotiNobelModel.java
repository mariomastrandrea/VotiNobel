package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class MyVotiNobelModel implements Model
{
	private List<Esame> esami;
	private EsameDAO esameDao;

	
	public MyVotiNobelModel()
	{
		this.esameDao = new EsameDAO();
		this.esami = this.esameDao.getTuttiGliEsami();
	}
	
	public Set<Set<Esame>> migliorSottoinsiemeEsamiDa(int cfu)
	{
		Set<Esame> soluzioneParziale = new HashSet<>();
		Set<Set<Esame>> soluzioniMigliori = new HashSet<>();
		int offset = 0;
		DoubleObj mediaMigliore = DoubleObj.of(0.0);
		DoubleObj mediaAttuale = DoubleObj.of(0.0);
		IntObj cfuAttuali = IntObj.of(0);
		
		this.calcolaRicorsivamente(soluzioneParziale, cfuAttuali, mediaAttuale, 
									offset, soluzioniMigliori, mediaMigliore, cfu);
		
		return soluzioniMigliori;
	}
	
	//metodo ottimizzato
	private void calcolaRicorsivamente(Set<Esame> soluzioneParziale, IntObj cfuAttuali, DoubleObj mediaAttuale,
			int offset, Set<Set<Esame>> soluzioniMigliori, DoubleObj mediaMigliore, int cfuTotali)
	{
		int cfu = cfuAttuali.value;
		
		if(cfu > cfuTotali)
			return;
		
		if(cfu == cfuTotali)
		{
			if(mediaAttuale.value >= mediaMigliore.value)
			{
				if(mediaAttuale.value > mediaMigliore.value)
				{
					mediaMigliore.value = mediaAttuale.value;
					soluzioniMigliori.clear();
				}
				
				Set<Esame> nuovaSoluzioneMigliore = new HashSet<>(soluzioneParziale);
				soluzioniMigliori.add(nuovaSoluzioneMigliore);
			}
			return;
		}
		
		//cfu attuali < cfu totali
		
		int size = this.esami.size();
		
		for(int i=offset; i<size; i++)
		{
			Esame esame = this.esami.get(i);
			int cfuEsame = esame.getCrediti();
			int votoEsame = esame.getVoto();
			
			soluzioneParziale.add(esame);
			
			double vecchiaMedia = mediaAttuale.value;
			int vecchiCfu = cfuAttuali.value;
			
			//nuova media
			mediaAttuale.value = ((vecchiaMedia * (double)vecchiCfu) + (votoEsame * (double)cfuEsame))/(double)(vecchiCfu + cfuEsame);
			//nuovi cfu
			cfuAttuali.value = vecchiCfu + cfuEsame;
			
			calcolaRicorsivamente(soluzioneParziale, cfuAttuali, mediaAttuale, 
									i+1, soluzioniMigliori, mediaMigliore, cfuTotali);
			
			//backtracking
			soluzioneParziale.remove(esame);
			mediaAttuale.value = vecchiaMedia;
			cfuAttuali.value = vecchiCfu;
		}
		
	}
}
