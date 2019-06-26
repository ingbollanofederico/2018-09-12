package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{

	public enum TIPO{
		INIZIO_INTERRUZIONE,
		FINE_INTERRUZIONE,
		CANCELLA_PRESTITO
	}
	
	private Nerc nerc;
	private Nerc donatore;
	private TIPO tipo;
	private LocalDateTime data; //per ordinare nella coda
	
	//due elementi di riferimento
	private LocalDateTime datatInizio;
	private LocalDateTime dataFine;
	
	
	public Evento(TIPO tipo, Nerc nerc, Nerc donatore, LocalDateTime data, LocalDateTime datatInizio,
			LocalDateTime dataFine) {
		super();
		this.nerc = nerc;
		this.donatore = donatore;
		this.tipo = tipo;
		this.data = data;
		this.datatInizio = datatInizio;
		this.dataFine = dataFine;
	}


	public Nerc getNerc() {
		return nerc;
	}


	public void setNerc(Nerc nerc) {
		this.nerc = nerc;
	}


	public Nerc getDonatore() {
		return donatore;
	}


	public void setDonatore(Nerc donatore) {
		this.donatore = donatore;
	}


	public TIPO getTipo() {
		return tipo;
	}


	public void setTipo(TIPO tipo) {
		this.tipo = tipo;
	}


	public LocalDateTime getData() {
		return data;
	}


	public void setData(LocalDateTime data) {
		this.data = data;
	}


	public LocalDateTime getDatatInizio() {
		return datatInizio;
	}


	public void setDatatInizio(LocalDateTime datatInizio) {
		this.datatInizio = datatInizio;
	}


	public LocalDateTime getDataFine() {
		return dataFine;
	}


	public void setDataFine(LocalDateTime dataFine) {
		this.dataFine = dataFine;
	}


	@Override
	public int compareTo(Evento o) {
		return this.data.compareTo(o.getData());
	}
	
	
	
	
}
