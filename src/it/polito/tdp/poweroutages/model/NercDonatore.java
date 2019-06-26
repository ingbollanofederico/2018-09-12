package it.polito.tdp.poweroutages.model;

import java.util.List;

public class NercDonatore {
	
	private Nerc nercDonatore;
	private List<Nerc> nercBeneficiari;
	public Nerc getNercDonatore() {
		return nercDonatore;
	}
	public void setNercDonatore(Nerc nercDonatore) {
		this.nercDonatore = nercDonatore;
	}
	public List<Nerc> getNercBeneficiari() {
		return nercBeneficiari;
	}
	public void setNercBeneficiari(List<Nerc> nercBeneficiari) {
		this.nercBeneficiari = nercBeneficiari;
	}
	public NercDonatore(Nerc nercDonatore, List<Nerc> nercBeneficiari) {
		super();
		this.nercDonatore = nercDonatore;
		this.nercBeneficiari = nercBeneficiari;
	}
	
	public void aggiungiBeneficiario(Nerc beneficiario) {
		this.nercBeneficiari.add(beneficiario);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nercDonatore == null) ? 0 : nercDonatore.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NercDonatore other = (NercDonatore) obj;
		if (nercDonatore == null) {
			if (other.nercDonatore != null)
				return false;
		} else if (!nercDonatore.equals(other.nercDonatore))
			return false;
		return true;
	}

	
}
