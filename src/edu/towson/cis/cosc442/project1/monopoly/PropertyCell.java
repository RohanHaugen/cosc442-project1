package edu.towson.cis.cosc442.project1.monopoly;

public class PropertyCell extends Cell {
	private String colorGroup;
	private int housePrice;
	private int numHouses;
	private int rent;
	private int sellPrice;

	/**
	 * Returns the color group of this property.
	 * @return The color group as a String.
	 */
	public String getColorGroup() {
		return colorGroup;
	}

	/**
	 * Returns the price of a house on this property.
	 * @return The house price as an integer.
	 */
	public int getHousePrice() {
		return housePrice;
	}

	/**
	 * Returns the number of houses currently on this property.
	 * @return The number of houses as an integer.
	 */
	public int getNumHouses() {
		return numHouses;
	}
    
    /**
     * Returns the selling price of this property.
     * @return The selling price as an integer.
     */
    public int getPrice() {
		return sellPrice;
	}

	/**
	 * Calculates and returns the current rent owed for this property.
	 * @return The rent amount as an integer, which may be adjusted for monopolies or houses.
	 */
	public int getRent() {
		int rentToCharge = rent;
		String [] monopolies = theOwner.getMonopolies();
		rentToCharge = calculateMonopoliesRent(rentToCharge, monopolies);
		if(numHouses > 0) {
			rentToCharge = rent * (numHouses + 1);
		}
		return rentToCharge;
	}

	/**
	 * Calculates rent adjustment based on monopolies owned in the given color groups.
	 * @param rentToCharge The current base rent to potentially adjust.
	 * @param monopolies Array of color groups representing monopolies owned by the player.
	 * @return The adjusted rent amount after applying monopoly rules.
	 */
	private int calculateMonopoliesRent(int rentToCharge, String[] monopolies) {
		for(int i = 0; i < monopolies.length; i++) {
			if(monopolies[i].equals(colorGroup)) {
				rentToCharge = rent * 2;
			}
		}
		return rentToCharge;
	}

	/**
	 * Executes the action of this property when landed on by a player, including rent payment if owned by another player.
	 */
	public void playAction() {
		Player currentPlayer = null;
		if(!isAvailable()) {
			currentPlayer = GameMaster.instance().getCurrentPlayer();
			if(theOwner != currentPlayer) {
				currentPlayer.payRentTo(theOwner, getRent());
			}
		}
	}

	/**
	 * Sets the color group for this property.
	 * @param colorGroup The color group to assign to the property.
	 */
	public void setColorGroup(String colorGroup) {
		this.colorGroup = colorGroup;
	}

	/**
	 * Sets the price of a house on this property.
	 * @param housePrice The house price to set.
	 */
	public void setHousePrice(int housePrice) {
		this.housePrice = housePrice;
	}

	/**
	 * Sets the number of houses currently on this property.
	 * @param numHouses The number of houses to set.
	 */
	public void setNumHouses(int numHouses) {
		this.numHouses = numHouses;
	}

	/**
	 * Sets the selling price of this property.
	 * @param sellPrice The selling price to set.
	 */
	public void setPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * Sets the base rent value for this property.
	 * @param rent The rent amount to set.
	 */
	public void setRent(int rent) {
		this.rent = rent;
	}
}
