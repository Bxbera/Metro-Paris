package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.metroparis.model.Fermata;

public class TestDAO {

	public static void main(String[] args) {
		
		try {
			Connection connection = DBConnect.getConnection();
			connection.close();
			System.out.println("Connection Test PASSED");
			
			MetroDAO dao = new MetroDAO() ;
			List<Fermata> fermate = dao.readFermate();
			Map<Integer, Fermata> IdMapFermate = new HashMap<>();
			
			for(Fermata fi: fermate) {
				IdMapFermate.put(fi.getIdFermata(), fi);
			}
			
			//System.out.println(dao.readFermate()) ;
			//System.out.println(dao.readLinee()) ;
			System.out.println(dao.trovaCollegate(IdMapFermate.get(123), IdMapFermate));

		} catch (Exception e) {
			throw new RuntimeException("Test FAILED", e);
		}
	}

}
