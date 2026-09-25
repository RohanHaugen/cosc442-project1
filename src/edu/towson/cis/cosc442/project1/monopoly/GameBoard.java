package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Hashtable;

public class GameBoard {

	private ArrayList<Cell> cells = new ArrayList<Cell>();
    private ArrayList<Card> chanceCards = new ArrayList<Card>();
	//the key of colorGroups is the name of the color group.
	private Hashtable<String, Integer> colorGroups = new Hashtable<String, Integer>();
	private ArrayList<Card> communityChestCards = new ArrayList<Card>();
	/**
	 * Constructs a new GameBoard and initializes it with a GoCell.
	 */
	public GameBoard() {
		Cell go = new GoCell();
		addCell(go);
	}

    /**
     * Adds a card to the appropriate collection based on its type (Community Chest or Chance).
     * @param card The card to be added
     */
    public void addCard(Card card) {
        if(card.getCardType() == Card.TYPE_CC) {
            communityChestCards.add(card);
        } else {
            chanceCards.add(card);
        }
    }
	
	/**
	 * Adds a general cell to the game board.
	 * @param cell The cell to be added
	 */
	public void addCell(Cell cell) {
		cells.add(cell);
	}
	
	/**
	 * Adds a PropertyCell to the game board and updates the color group count.
	 * @param cell The property cell to be added
	 */
	public void addCell(PropertyCell cell) {
		String cellColorGroup = cell.getColorGroup();
		int propertyNumber = getPropertyNumberForColor(cellColorGroup);
		colorGroups.put(cellColorGroup, new Integer(propertyNumber + 1));
        cells.add(cell);
	}

    /**
     * Draws the first Community Chest card, removes it from the deck, then adds it back to the bottom of the deck.
     * @return The drawn Community Chest card
     */
    public Card drawCCCard() {
        Card card = (Card)communityChestCards.get(0);
        communityChestCards.remove(0);
        addCard(card);
        return card;
    }

    /**
     * Draws the first Chance card, removes it from the deck, then adds it back to the bottom of the deck.
     * @return The drawn Chance card
     */
    public Card drawChanceCard() {
        Card card = (Card)chanceCards.get(0);
        chanceCards.remove(0);
        addCard(card);
        return card;
    }

	/**
	 * Returns the cell at the specified index on the game board.
	 * @param newIndex The index of the cell to retrieve
	 * @return The Cell at the specified index
	 */
	public Cell getCell(int newIndex) {
		return (Cell)cells.get(newIndex);
	}
	
	/**
	 * Returns the total number of cells on the game board.
	 * @return The number of cells
	 */
	public int getCellNumber() {
		return cells.size();
	}
	
	/**
	 * Returns an array of PropertyCells that belong to the specified color group (monopoly).
	 * @param color The color group name to retrieve properties for
	 * @return An array of PropertyCells in the given color group
	 */
	public PropertyCell[] getPropertiesInMonopoly(String color) {
		PropertyCell[] monopolyCells = 
			new PropertyCell[getPropertyNumberForColor(color)];
		int counter = 0;
		for (int i = 0; i < getCellNumber(); i++) {
			Cell c = getCell(i);
			if(c instanceof PropertyCell) {
				PropertyCell pc = (PropertyCell)c;
				if(pc.getColorGroup().equals(color)) {
					monopolyCells[counter] = pc;
					counter++;
				}
			}
		}
		return monopolyCells;
	}
	
	/**
	 * Returns the number of properties in the specified color group.
	 * @param name The name of the color group
	 * @return The number of properties in that color group
	 */
	public int getPropertyNumberForColor(String name) {
		Integer number = (Integer)colorGroups.get(name);
		if(number != null) {
			return number.intValue();
		}
		return 0;
	}

	/**
	 * Finds and returns the cell with the specified name, or null if not found.
	 * @param string The name of the cell to find
	 * @return The Cell matching the name or null if none found
	 */
	public Cell queryCell(String string) {
		for(int i = 0; i < cells.size(); i++){
			Cell temp = (Cell)cells.get(i); 
			if(temp.getName().equals(string)) {
				return temp;
			}
		}
		return null;
	}
	
	/**
	 * Finds and returns the index of the cell with the specified name, or -1 if not found.
	 * @param string The name of the cell to find
	 * @return The index of the cell or -1 if not found
	 */
	public int queryCellIndex(String string){
		for(int i = 0; i < cells.size(); i++){
			Cell temp = (Cell)cells.get(i); 
			if(temp.getName().equals(string)) {
				return i;
			}
		}
		return -1;
	}

    /**
     * Clears all Community Chest cards from the game board.
     */
    public void removeCards() {
        communityChestCards.clear();
    }
}
