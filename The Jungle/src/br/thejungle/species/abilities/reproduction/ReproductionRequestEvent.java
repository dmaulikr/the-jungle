/*
 * Created on Dec 15, 2004
 */
package br.thejungle.species.abilities.reproduction;

import br.thejungle.species.SenseEvent;
import br.thejungle.species.SpeciesInfo;


/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 *
 */
public class ReproductionRequestEvent extends SenseEvent {

	private Voucher voucher;
	
	public ReproductionRequestEvent(SpeciesInfo speciesInfo) {
		this.voucher = new Voucher(speciesInfo); 
	}
	
	public Voucher getVoucher() {
		return voucher;
	}
	
}
