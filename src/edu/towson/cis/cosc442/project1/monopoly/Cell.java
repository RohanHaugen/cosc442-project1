package edu.towson.cis.cosc442.project1.monopoly;

public abstract class Cell {
	private boolean available = true;
	private String name;
	protected Player theOwner;

	/**
	 * Returns the name of the cell.
	 * @return The cell's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the owner of the cell.
	 * @return The player who owns the cell.
	 */
	public Player getTheOwner() {
		return theOwner;
	}
	
	/**
	 * Returns the price of the cell, defaulting to 0.
	 * @return The price of the cell as an integer.
	 */
	public int getPrice() {
		return 0;
	}

	/**
	 * Indicates whether the cell is currently available.
	 * @return True if the cell is available, false otherwise.
	 */
	public boolean isAvailable() {
		return available;
	}
	
	/**
	 * Performs the action associated with landing on the cell.
	 */
	public abstract void playAction();

	/**
	 * Sets the availability status of the cell.
	 * @param available The new availability status to set.
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	/**
	 * Sets the name of the cell.
	 * @param name The name to assign to the cell.
	 */
	void setName(String name) {
		this.name = name;
	}

	/**
	 * Assigns an owner to the cell.
	 * @param owner The player to set as the cell's owner.
	 */
	public void setTheOwner(Player owner) {
		this.theOwner = owner;
	}
    
    /**
     * Returns a string representation of the cell.
     * @return The cell's name as a string.
     */
    public String toString() {
        return name;
    }
}
