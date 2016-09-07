/*
 * Created on Dec 15, 2004
 */
package br.thejungle.species.abilities.reproduction;

import java.util.Random;

import br.thejungle.species.SpeciesInfo;


/**
 * @author Flávio Stutz (flaviostutz@uol.com.br)
 *
 */
public class Voucher {

	private SpeciesInfo speciesInfo;
	private String voucher;
	private static Random random = new Random();
	
	public Voucher(SpeciesInfo speciesInfo) {
		this.speciesInfo = speciesInfo;
		this.voucher = speciesInfo.getIndividualID() + random.nextLong();
	}
	
	/**
	 * @return Returns the speciesInfo.
	 */
	public SpeciesInfo getSpeciesInfo() {
		return speciesInfo;
	}
	/**
	 * @return Returns the voucher.
	 */
	public String getVoucher() {
		return voucher;
	}
}
