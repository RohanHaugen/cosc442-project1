package edu.towson.cis.cosc442.project1.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import edu.towson.cis.cosc442.project1.monopoly.*;

public class MainWindow extends JFrame implements MonopolyGUI{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel eastPanel = new JPanel();
	ArrayList<GUICell> guiCells = new ArrayList<GUICell>();

	JPanel northPanel = new JPanel();
	PlayerPanel[] playerPanels;
	JPanel southPanel = new JPanel();
	JPanel westPanel = new JPanel();

	/**
	 * Constructs the main window for the Monopoly game GUI, sets up layout, borders, and registers a window closing event to exit the application.
	 */
	public MainWindow() {
		northPanel.setBorder(new LineBorder(Color.BLACK));
		southPanel.setBorder(new LineBorder(Color.BLACK));
		westPanel.setBorder(new LineBorder(Color.BLACK));
		eastPanel.setBorder(new LineBorder(Color.BLACK));
		
		Container c = getContentPane();
		//setSize(800, 600);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		setSize(d);
		c.add(northPanel, BorderLayout.NORTH);
		c.add(southPanel, BorderLayout.SOUTH);
		c.add(eastPanel, BorderLayout.EAST);
		c.add(westPanel, BorderLayout.WEST);
		
		this.addWindowListener(new WindowAdapter(){
			/**
			 * Handles the window closing event by exiting the application.
			 */
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	/**
	 * Adds GUI cell components to the specified panel for each game cell in the provided list.
	 * @param panel The JPanel to which GUI cells will be added.
	 * @param cells The list of game cells to create GUI cells from.
	 */
	private void addCells(JPanel panel, List<?> cells) {
		for(int x=0; x<cells.size(); x++) {
			GUICell cell = new GUICell((Cell)cells.get(x));
			panel.add(cell);
			guiCells.add(cell);
		}
	}
	
	/**
	 * Creates and adds player panels to the center of the window, displaying current player information.
	 */
	private void buildPlayerPanels() {
		GameMaster master = GameMaster.instance();
		JPanel infoPanel = new JPanel();
        int players = master.getNumberOfPlayers();
        infoPanel.setLayout(new GridLayout(2, (players+1)/2));
		getContentPane().add(infoPanel, BorderLayout.CENTER);
		playerPanels = new PlayerPanel[master.getNumberOfPlayers()];
		for (int i = 0; i< master.getNumberOfPlayers(); i++){
			playerPanels[i] = new PlayerPanel(master.getPlayer(i));
			infoPanel.add(playerPanels[i]);
			playerPanels[i].displayInfo();
		}
	}

	/**
	 * Enables the 'End Turn' button for the specified player.
	 * @param playerIndex The index of the player whose 'End Turn' button is enabled.
	 */
	public void enableEndTurnBtn(int playerIndex) {
		playerPanels[playerIndex].setEndTurnEnabled(true);
	}
	
	/**
	 * Enables the 'Roll Dice' button for the specified player's turn.
	 * @param playerIndex The index of the player whose turn is being enabled.
	 */
	public void enablePlayerTurn(int playerIndex) {
		playerPanels[playerIndex].setRollDiceEnabled(true);
		
	}

	/**
	 * Enables the 'Purchase Property' button for the specified player.
	 * @param playerIndex The index of the player whose purchase button is enabled.
	 */
	public void enablePurchaseBtn(int playerIndex) {
		playerPanels[playerIndex].setPurchasePropertyEnabled(true);
	}

	@SuppressWarnings("deprecation")
	/**
	 * Displays a dialog to allow the user to input a dice roll and returns the result as an array of two integers.
	 * @return An array of two integers representing the dice roll values.
	 */
	public int[] getDiceRoll() {
		TestDiceRollDialog dialog = new TestDiceRollDialog(this);
		dialog.show();
		return dialog.getDiceRoll();
	}

    /**
     * Checks if the current player's 'Draw Card' button is enabled.
     * @return True if the 'Draw Card' button is enabled for the current player; false otherwise.
     */
    public boolean isDrawCardButtonEnabled() {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        return playerPanels[currentPlayerIndex].isDrawCardButtonEnabled();
    }

    /**
     * Checks if the current player's 'End Turn' button is enabled.
     * @return True if the 'End Turn' button is enabled for the current player; false otherwise.
     */
    public boolean isEndTurnButtonEnabled() {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        return playerPanels[currentPlayerIndex].isEndTurnButtonEnabled();
    }

	/**
	 * Checks if the current player's 'Get Out Of Jail' button is enabled.
	 * @return True if the 'Get Out Of Jail' button is enabled for the current player; false otherwise.
	 */
	public boolean isGetOutOfJailButtonEnabled() {
		int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
		return playerPanels[currentPlayerIndex].isGetOutOfJailButtonEnabled();
	}

    /**
     * Checks if the trade button is enabled for the specified player.
     * @param i The index of the player to check trade button status for.
     * @return True if the trade button is enabled for the player; false otherwise.
     */
    public boolean isTradeButtonEnabled(int i) {
        return playerPanels[i].isTradeButtonEnabled();
    }
	
	/**
	 * Moves a player's piece visually from one cell to another on the game board GUI.
	 * @param index The player index to move.
	 * @param from The starting cell index.
	 * @param to The destination cell index.
	 */
	public void movePlayer(int index, int from, int to) {
		GUICell fromCell = queryCell(from);
		GUICell toCell = queryCell(to);
		fromCell.removePlayer(index);
		toCell.addPlayer(index);
	}

    @SuppressWarnings("deprecation")
	/**
	 * Opens a dialog to respond to a trade deal and returns the dialog instance.
	 * @param deal The trade deal to respond to.
	 * @return The RespondDialog instance displaying the trade deal response interface.
	 */
	public RespondDialog openRespondDialog(TradeDeal deal) {
        GUIRespondDialog dialog = new GUIRespondDialog();
        dialog.setDeal(deal);
        dialog.show();
        return dialog;
    }

    @SuppressWarnings("deprecation")
	/**
	 * Opens the trade dialog for initiating trades and returns the dialog instance.
	 * @return The TradeDialog instance for trade interactions.
	 */
	public TradeDialog openTradeDialog() {
        GUITradeDialog dialog = new GUITradeDialog(this);
        dialog.show();
        return dialog;
    }
	
	/**
	 * Returns the GUICell component corresponding to the specified cell index on the game board.
	 * @param index The index of the cell to query.
	 * @return The GUICell representing the cell at the given index, or null if not found.
	 */
	private GUICell queryCell(int index) {
		Cell cell = GameMaster.instance().getGameBoard().getCell(index);
		for(int x = 0; x < guiCells.size(); x++) {
			GUICell guiCell = (GUICell)guiCells.get(x);
			if(guiCell.getCell() == cell) return guiCell;
		}
		return null;
	}

    /**
     * Sets whether the 'Buy House' button is enabled for the current player.
     * @param b True to enable the button; false to disable.
     */
    public void setBuyHouseEnabled(boolean b) {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        playerPanels[currentPlayerIndex].setBuyHouseEnabled(b);
    }

    /**
     * Sets whether the 'Draw Card' button is enabled for the current player.
     * @param b True to enable the button; false to disable.
     */
    public void setDrawCardEnabled(boolean b) {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        playerPanels[currentPlayerIndex].setDrawCardEnabled(b);
    }

    /**
     * Enables or disables the 'End Turn' button for the current player.
     * @param enabled True to enable; false to disable the button.
     */
    public void setEndTurnEnabled(boolean enabled) {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        playerPanels[currentPlayerIndex].setEndTurnEnabled(enabled);
    }

    /**
     * Sets whether the 'Get Out Of Jail' button is enabled for the current player.
     * @param b True to enable the button; false to disable.
     */
    public void setGetOutOfJailEnabled(boolean b) {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        playerPanels[currentPlayerIndex].setGetOutOfJailEnabled(b);
    }

    /**
     * Enables or disables the 'Purchase Property' button for the current player.
     * @param enabled True to enable; false to disable the button.
     */
    public void setPurchasePropertyEnabled(boolean enabled) {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        playerPanels[currentPlayerIndex].setPurchasePropertyEnabled(enabled);
    }

    /**
     * Sets whether the 'Roll Dice' button is enabled for the current player.
     * @param b True to enable the button; false to disable.
     */
    public void setRollDiceEnabled(boolean b) {
        int currentPlayerIndex = GameMaster.instance().getCurrentPlayerIndex();
        playerPanels[currentPlayerIndex].setRollDiceEnabled(b);
    }

    /**
     * Enables or disables the trade button for the specified player.
     * @param index The index of the player.
     * @param b True to enable; false to disable the trade button.
     */
    public void setTradeEnabled(int index, boolean b) {
        playerPanels[index].setTradeEnabled(b);
    }
	
	/**
	 * Sets up and lays out the GUI cells around the window edges based on the game board and builds player panels.
	 * @param board The GameBoard instance containing all game cells.
	 */
	public void setupGameBoard(GameBoard board) {
		Dimension dimension = GameBoardUtil.calculateDimension(board.getCellNumber());
		northPanel.setLayout(new GridLayout(1, dimension.width + 2));
		southPanel.setLayout(new GridLayout(1, dimension.width + 2));
		westPanel.setLayout(new GridLayout(dimension.height, 1));
		eastPanel.setLayout(new GridLayout(dimension.height, 1));
		addCells(northPanel, GameBoardUtil.getNorthCells(board));
		addCells(southPanel, GameBoardUtil.getSouthCells(board));
		addCells(eastPanel, GameBoardUtil.getEastCells(board));
		addCells(westPanel, GameBoardUtil.getWestCells(board));
		buildPlayerPanels();
	}

    @SuppressWarnings("deprecation")
	/**
	 * Displays a dialog allowing the specified player to buy a house.
	 * @param currentPlayer The player who may buy a house.
	 */
	public void showBuyHouseDialog(Player currentPlayer) {
        BuyHouseDialog dialog = new BuyHouseDialog(currentPlayer);
        dialog.show();
    }

    /**
     * Shows a message dialog displaying the provided string.
     * @param msg The message string to display.
     */
    public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
    }

	/**
	 * Shows a dialog for rolling utility dice and returns the dice roll result.
	 * @return The integer result of the utility dice roll.
	 */
	public int showUtilDiceRoll() {
		return UtilDiceRoll.showDialog();
	}

	/**
	 * Initializes player positions by moving all players to the starting cell on the board.
	 */
	public void startGame() {
		int numberOfPlayers = GameMaster.instance().getNumberOfPlayers();
		for(int i = 0; i < numberOfPlayers; i++) {
			movePlayer(i, 0, 0);
		}
	}

	/**
	 * Refreshes the display information for all player panels and game board GUI cells.
	 */
	public void update() {
		for(int i = 0; i < playerPanels.length; i++) {
			playerPanels[i].displayInfo();
		}
		for(int j = 0; j < guiCells.size(); j++ ) {
			GUICell cell = (GUICell)guiCells.get(j);
			cell.displayInfo();
		}
	}
}
