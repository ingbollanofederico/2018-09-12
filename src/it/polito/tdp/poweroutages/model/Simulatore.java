package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;


public class Simulatore {
	//MODELLO/LO STATO DEL SISTEMA
	private Graph<Nerc,DefaultWeightedEdge> grafo;
	private List<PowerOutage> powerOutage;
	private Map<Nerc,Set<Nerc>> prestiti;
	
	
	//PARAMETRI SIMULAZIONE
	private int k;
	
	//VALORI IN OUTPUT
	private int CATASTROFI;
	private Map<Nerc, Long> bonus;
	
	
	//CODA
	private PriorityQueue<Evento> queue;
	
	
	public void init(int k, List<PowerOutage> powerOutages, NercIdMap nercMap, Graph<Nerc,DefaultWeightedEdge> grafo) {
		
		//inizializzo qui invece che nel costruttorew
		this.queue = new PriorityQueue<Evento>();
		this.bonus = new HashMap<Nerc,Long>();
		this.prestiti = new HashMap<Nerc,Set<Nerc>>();
		
		for(Nerc n : nercMap.values()) {
			this.bonus.put(n, Long.valueOf(0));
			this.prestiti.put(n, new HashSet<Nerc>()); //FONDAMENTALE QUESTA NEW PER INIZIALIZZARE LA LISTA SET
		}
		
		this.CATASTROFI=0;
		
		//salvo quello che arriva da fuori
		this.k=k;
		this.powerOutage=powerOutages;
		this.grafo=grafo;
		
		
		//inserisco gli eventi iniziali
		for(PowerOutage po : this.powerOutage) {
			Evento e = new Evento(Evento.TIPO.INIZIO_INTERRUZIONE,
					po.getNerc(),null,po.getInizio(),po.getInizio(),po.getFine());
			queue.add(e);
		}
		
		
	}
	
	
	public void run() {
		
		Evento e;
		while((e=queue.poll())!=null) {
			
			switch(e.getTipo()) {
			
			case INIZIO_INTERRUZIONE:
				Nerc nerc = e.getNerc();
				System.out.println("INIZIO INTERRUZIONE NERC: "+nerc);
				
				//cerco se c'è un donatore, altrimenti... CATASTROFE
				Nerc donatore = null;
				
				//algoritmo per selezionare donatore
				
				//cerco tra debitore
				if(this.prestiti.get(nerc).size()>0) {
					//scelgo tra i debitori
					double min = Long.MAX_VALUE;
					
					for(Nerc n : this.prestiti.get(nerc)) {
						DefaultWeightedEdge edge = this.grafo.getEdge(nerc, n); // prendo l'arco
						
						if(this.grafo.getEdgeWeight(edge) < min) { //verifico il peso
							
							if(!n.getStaPrestanzo()) {  //se non presta prendi lui
							donatore = n;
							min = this.grafo.getEdgeWeight(edge);
							}
						}
					}
					
				}else {
					//prendo quello con peso arco minore
					List<Nerc> neighbors = Graphs.neighborListOf(this.grafo, nerc);
					double min = Long.MAX_VALUE;
					for(Nerc n : neighbors) {
						DefaultWeightedEdge edge = this.grafo.getEdge(nerc, n); // prendo l'arco
						
						if(this.grafo.getEdgeWeight(edge) < min) { //verifico il peso
							
							if(!n.getStaPrestanzo()) {  //se non presta prendi lui
							donatore = n;
							min = this.grafo.getEdgeWeight(edge);
							}
						}
					
				}
				
				
				if(donatore!=null) {
					donatore.setStaPrestanzo(true); //blocco il donatore che non la può dare ad altri
					
					//creo l'evento di fine
					Evento fine = new Evento(Evento.TIPO.FINE_INTERRUZIONE,e.getNerc(),donatore,
							e.getDataFine(),e.getDatatInizio(),e.getDataFine());
					queue.add(fine);
					
					//creo il prestito e la sua rimozione
					this.prestiti.get(donatore).add(e.getNerc());
					
					Evento cancella = new Evento(Evento.TIPO.CANCELLA_PRESTITO,e.getNerc(),donatore,e.getData().plusMonths(k), e.getDatatInizio(),e.getDataFine());
					queue.add(cancella);
					
				}else {
					//CATASTROFE
					this.CATASTROFI++;
				}
				
			}
				break;
				
				
			case FINE_INTERRUZIONE:
				//assegnare bonus a donatore
				if(e.getDonatore()!=null) {
					this.bonus.put(e.getDonatore(), (bonus.get(e.getDonatore())+Duration.between(e.getDatatInizio(), e.getDataFine()).toDays()));
				}
				
				//dire che donatore non sta più prestando
				e.getDonatore().setStaPrestanzo(false);
				
				break;
				
				
				
			case CANCELLA_PRESTITO:
				this.prestiti.remove(e.getDonatore()).remove(e.getNerc());
				
				break;
			}
				
		}
		
		
		
	
	}
}
