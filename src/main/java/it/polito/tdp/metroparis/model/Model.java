package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata, DefaultEdge> grafo;
	private List<Fermata> fermate;	// la mettiamo qua perchè può servire, poi la popolo quando creo il grafo
	private Map<Integer, Fermata> fermateIdMap = new HashMap<>();
	
	public void creaGrafo() {
		// vogliamo un grafo non orientato, semplice e non pesato
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		
		// aggiungi i vertici
		MetroDAO dao = new MetroDAO();
		long partenzaMetodoVertici = System.currentTimeMillis();
		fermate = dao.readFermate();
		
		long inizioGrafo = System.currentTimeMillis();
		Graphs.addAllVertices(this.grafo, this.fermate);
		long fineGrafo = System.currentTimeMillis();
		
		long fineMetodoVertici = System.currentTimeMillis();
		
		for(Fermata f: this.fermate)
			this.fermateIdMap.put(f.getIdFermata(), f);	// non devo creare la mappa prima? Esatto, prima devo creare istanziare la mappa!
		
		// aggiungi gli archi
		
		// metodo 1: considero tutti i potenziali archi
		/*
		for(Fermata partenza: this.grafo.vertexSet()) {
			for(Fermata arrivo: this.grafo.vertexSet()) {
				if(dao.isConnesse(partenza, arrivo) ) {
					this.grafo.addEdge(partenza, arrivo);
					// nel caso il grafo fosse orientato mi aggiungerebbe anche l'altro percorso, ma nel mio caso non è orientato,
					// quindi come si comporta? Leggendo sopra il metodo, nel nostro caso non permette la duplicazione dello stesso arco già aggiunto, rimanda null
				}
			}
		}
		*/
		long inizioMetodoArchi = System.currentTimeMillis();
		
		// metodo 2: data una fermata, trova la lista di quelle adiacenti (richiede più intelligenza dal dao)
		for(Fermata partenza: this.grafo.vertexSet()) {
			List<Fermata> collegate = dao.trovaCollegate(partenza, this.fermateIdMap);
			
			for(Fermata arrivo: collegate) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		
		long fineMetodoArchi = System.currentTimeMillis();
		
		
		System.out.println("Grafo creato con "+this.grafo.vertexSet().size() +
				" vertici e " + this.grafo.edgeSet().size() + " archi");
		System.out.println(this.grafo);
		
		System.out.println("\n\nCi ha messo "+ (fineMetodoVertici - partenzaMetodoVertici) + " millisecondi il metodo per i vertici\n"
				+ "Mentre il metodo per popolare gli archi: " + (fineMetodoArchi - inizioMetodoArchi)
				+ "tempo messo a popolare il grafo: " + (fineGrafo-inizioGrafo));
	}
	
}
