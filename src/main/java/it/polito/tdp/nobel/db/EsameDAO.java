package it.polito.tdp.nobel.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.nobel.model.Esame;


public class EsameDAO 
{
	private String tabella = "Esami";

	public List<Esame> getTuttiGliEsami() 
	{
		final String sql = "SELECT * FROM " + tabella;

		List<Esame> votiEsami = new ArrayList<Esame>();

		try 
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			while (result.next()) 
			{
				votiEsami.add(new Esame(result.getString("codins"),
										result.getString("nomecorso"), 
										result.getInt("crediti"), 
										result.getInt("voto")));
			}
			result.close();
			statement.close();
			connection.close();
		}
		catch (SQLException sqle) 
		{
			sqle.printStackTrace();
			throw new RuntimeException("Errore DB in getTuttiGliEsami()", sqle);
		}
		
		return votiEsami;
	}
	
	/*
	 * Data una matricola ed il codice insegnamento, iscrivi lo studente al corso.
	 */
	public boolean inserisciEsame(Esame esame) 
	{
		String sql = "INSERT IGNORE INTO esamitriennale." + tabella + " (codins, nomecorso, voto, crediti) VALUES(?,?,?,?)";
		boolean inserted = false;
		
		try 
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, esame.getCodins());
			statement.setString(2, esame.getNomeCorso());
			statement.setInt(3, esame.getVoto());
			statement.setInt(4, esame.getCrediti());
			
			int result = statement.executeUpdate();	

			if (result == 1)
				inserted = true;
			
			statement.close();
			connection.close();
		} 
		catch (SQLException sqle) 
		{
			sqle.printStackTrace();
			throw new RuntimeException("Errore inserimento esame in DB", sqle);
		}
		
		return inserted;
	}

	public boolean cancellaTuttiEsami() 
	{	
		boolean deleted = false;
		
		try 
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement1 = connection.prepareStatement("SELECT COUNT(*) AS cnt FROM "+tabella);
			ResultSet result1 = statement1.executeQuery();
			int numEsami = 0;
			
			if(result1.next())
				numEsami = result1.getInt("cnt");
			else
				throw new SQLException();
			
			result1.close();
			statement1.close();
			
			PreparedStatement statement2 = connection.prepareStatement("DELETE FROM "+tabella);
			int result = statement2.executeUpdate();	

			if (result == numEsami)
				deleted = true;
			else
				throw new SQLException();

			connection.close();
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Errore cancellazione DB", sqle);
		}
		
		return deleted;
	}
}
