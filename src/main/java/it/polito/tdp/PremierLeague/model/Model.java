package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Match, DefaultWeightedEdge> graph;
	private Map<Integer, Match> idMap;
	private List<Match> best;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.idMap = new HashMap<>(this.dao.listAllMatches());
	}
	
	public void creaGrafo(int mese, int min) {
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.graph, this.getPartiteGiocateMese(mese));
		
		for(Arco a : this.dao.getArchi(idMap, mese, min)) {
			Graphs.addEdgeWithVertices(this.graph, a.getM1(), a.getM2(), a.getPeso());
		}
		
		System.out.println(this.getNVertici() + "\n");
		System.out.println(this.getNArchi() + "\n");
		
		
	}
	
	public List<DefaultWeightedEdge> getConnessioneMax(){
		List<DefaultWeightedEdge> result = new ArrayList<>();
		double max = 0;
		
		for(DefaultWeightedEdge edge : this.graph.edgeSet()) {
			if(this.graph.getEdgeWeight(edge) > max)
				max = this.graph.getEdgeWeight(edge);
		}
		
		for(DefaultWeightedEdge edge : this.graph.edgeSet()) {
			if(this.graph.getEdgeWeight(edge) == max)
				result.add(edge);
		}
		
		return result;
	} 
	
	private List<Match> getPartiteGiocateMese(int mese) {	//getVertici	
		List<Match> matches = new ArrayList<>();
		for(Match m : this.dao.listAllMatches().values()) {
			if(m.getDate().getMonthValue() == mese)
				matches.add(m);
		}
		
		return matches;
	}
	
	public String getNVertici() {
		return "#VERTICI: " + this.graph.vertexSet().size();
	}
	
	public String getNArchi() {
		return "#ARCHI: " + this.graph.edgeSet().size();
	}
	
	public Graph<Match, DefaultWeightedEdge> getGraph(){
		return this.graph;
	}
	
	public boolean isGrafo() {
		if(this.graph == null)
			return false;
		else 
			return true;
	}
	
	public List<Match> calcolaPercorso(Match sorgente, Match destinazione){
		best = new LinkedList<>();
		List<Match> parziale = new LinkedList<>();
		parziale.add(sorgente);
		cerca(parziale, destinazione);
		return best;
	}

	private void cerca(List<Match> parziale, Match destinazione) {
//		Condizione di terminazione
		if(condizioneDiTerminazione(parziale, destinazione)) {
//			E' la soluzione migliore?
			double lunghezza = calcolaLunghezza(parziale);
			if(best == null || calcolaLunghezza(best) < lunghezza)
				best = new LinkedList<>(parziale);
			return;
		}
		
//		Scorro i vicini dell'ultimo inserito e provo le varie strade
		for(Match v : Graphs.neighborListOf(this.graph, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(v)) {
				parziale.add(v);
				cerca(parziale, destinazione);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	private boolean aggiuntaValida(Match v, List<Match> parziale) {
		if(parziale.size() == 0)
			return true;
		else {
			Match ultimoVertice = parziale.get(parziale.size()-1);
			if(this.graph.containsEdge(ultimoVertice, v) && !parziale.contains(v))
				return true;
		}
		return false;
	}

	private double calcolaLunghezza(List<Match> parziale) {
		double l = 0;
		for(int i = 0; i < parziale.size()-1; i++)
			l += this.graph.getEdgeWeight(this.graph.getEdge(parziale.get(i), parziale.get(i+1)));
		return l;
	}

	private boolean condizioneDiTerminazione(List<Match> parziale, Match destinazione) {
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
//			int ultimoVertice = parziale.get(parziale.size()-1);
			int conta = 0;	//conta i vertici non inseriti
			for(Match v : this.graph.vertexSet())
				if(!aggiuntaValida(v, parziale))
					conta++;
			
			if(conta == this.graph.vertexSet().size())
				return true;
		}
		return false;
	}
	
	
}
