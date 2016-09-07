package br.thejungle.environment.things;

/**
 * This is an abstract class for all kinds of food.
 */
public abstract class Food extends Thing {

	private short richness;

	private short size;

	public Food(short richness, short size) {
		this.richness = richness;
		this.size = size;
	}

	public short getSize() {
		return size;
	}

	public short getRichness() {
		return richness;
	}

	public void setSize(short size) {
		this.size = size;
	}

	public void setRichness(short richness) {
		this.richness = richness;
	}

	public String toString() {
		return super.toString() + "; size=" + size + "; richness=" + richness;
	}
}
