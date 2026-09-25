package edu.towson.cis.cosc442.project1.monopoly;

import java.util.ArrayList;
import java.util.Iterator;


public class GameMaster {

	private static GameMaster gameMaster;
	static final public int MAX_PLAYER = 8;	
	private Die[] dice;
	private GameBoard gameBoard;
	private MonopolyGUI gui;
	private int initAmountOfMoney;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int turn = 0;
	private int utilDiceRoll;
	private boolean testMode;

	/**
	 * Returns the singleton instance of the GameMaster class.
	 * @return The singleton GameMaster instance.
	 */
	public static GameMaster instance() {
		if(gameMaster == null) {
			gameMaster = new GameMaster();
		}
		return gameMaster;
	}

	/**
	 * Constructs a GameMaster with default initial money and two dice.
	 */
	public GameMaster() {
		initAmountOfMoney = 1500;
		dice = new Die[]{new Die(), new Die()};
	}

    /**
     * Handles the event when the 'Buy House' button is clicked by showing the buy house dialog for the current player.
     */
    public void btnBuyHouseClicked() {
        gui.showBuyHouseDialog(getCurrentPlayer());
    }

    /**
     * Handles the event when the 'Draw Card' button is clicked, draws and applies a Chance or Community Chest card for the current player.
     * @return The drawn Card object.
     */
    public Card btnDrawCardClicked() {
        gui.setDrawCardEnabled(false);
        CardCell cell = (CardCell)getCurrentPlayer().getPosition();
        Card card = null;
        if(cell.getType() == Card.TYPE_CC) {
            card = getGameBoard().drawCCCard();
            card.applyAction();
        } else {
            card = getGameBoard().drawChanceCard();
            card.applyAction();
        }
        gui.setEndTurnEnabled(true);
        return card;
    }

    /**
     * Handles the event when the 'End Turn' button is clicked, executes the current player's position action and switches turn if not bankrupt.
     */
    public void btnEndTurnClicked() {
		setAllButtonEnabled(false);
		getCurrentPlayer().getPosition().playAction();
		if(getCurrentPlayer().isBankrupt()) {
			gui.setBuyHouseEnabled(false);
			gui.setDrawCardEnabled(false);
			gui.setEndTurnEnabled(false);
			gui.setGetOutOfJailEnabled(false);
			gui.setPurchasePropertyEnabled(false);
			gui.setRollDiceEnabled(false);
			gui.setTradeEnabled(getCurrentPlayerIndex(),false);
			updateGUI();
		}
		else {
			switchTurn();
			updateGUI();
		}
    }

    /**
     * Handles the event when the 'Get Out of Jail' button is clicked and updates GUI buttons based on player status.
     */
    public void btnGetOutOfJailClicked() {
		getCurrentPlayer().getOutOfJail();
		if(getCurrentPlayer().isBankrupt()) {
			gui.setBuyHouseEnabled(false);
			gui.setDrawCardEnabled(false);
			gui.setEndTurnEnabled(false);
			gui.setGetOutOfJailEnabled(false);
			gui.setPurchasePropertyEnabled(false);
			gui.setRollDiceEnabled(false);
			gui.setTradeEnabled(getCurrentPlayerIndex(),false);
		}
		else {
			gui.setRollDiceEnabled(true);
			gui.setBuyHouseEnabled(getCurrentPlayer().canBuyHouse());
			gui.setGetOutOfJailEnabled(getCurrentPlayer().isInJail());
		}
    }

    /**
     * Handles the event when the 'Purchase Property' button is clicked, allowing the current player to purchase the property and updating the GUI.
     */
    public void btnPurchasePropertyClicked() {
        Player player = getCurrentPlayer();
		player.purchase();
		gui.setPurchasePropertyEnabled(false);
		updateGUI();
    }
    
    /**
     * Handles the event when the 'Roll Dice' button is clicked, rolls dice, moves the current player accordingly, and updates the GUI.
     */
    public void btnRollDiceClicked() {
		int[] rolls = rollDice();
		if((rolls[0]+rolls[1]) > 0) {
			Player player = getCurrentPlayer();
			gui.setRollDiceEnabled(false);
			StringBuffer msg = new StringBuffer();
			msg.append(player.getName())
					.append(", you rolled ")
					.append(rolls[0])
					.append(" and ")
					.append(rolls[1]);
			gui.showMessage(msg.toString());
			movePlayer(player, rolls[0] + rolls[1]);
			gui.setBuyHouseEnabled(false);
		}
    }

    /**
     * Handles the event when the 'Trade' button is clicked, managing the trade dialog and completing trade if accepted.
     */
    public void btnTradeClicked() {
        TradeDialog dialog = gui.openTradeDialog();
        TradeDeal deal = dialog.getTradeDeal();
        if(deal != null) {
            RespondDialog rDialog = gui.openRespondDialog(deal);
            if(rDialog.getResponse()) {
                completeTrade(deal);
                updateGUI();
            }
        }
    }

    /**
     * Completes the given trade deal by transferring property and money between players.
     * @param deal The TradeDeal object representing the trade agreement.
     */
    public void completeTrade(TradeDeal deal) {
        Player seller = getPlayer(deal.getPlayerIndex());
        Cell property = gameBoard.queryCell(deal.getPropertyName());
        seller.sellProperty(property, deal.getAmount());
        getCurrentPlayer().buyProperty(property, deal.getAmount());
    }

    /**
     * Draws a Community Chest card from the game board.
     * @return The drawn Community Chest Card.
     */
    public Card drawCCCard() {
        return gameBoard.drawCCCard();
    }

    /**
     * Draws a Chance card from the game board.
     * @return The drawn Chance Card.
     */
    public Card drawChanceCard() {
        return gameBoard.drawChanceCard();
    }

	
	/**
	 * Returns the player whose turn it currently is.
	 * @return The current Player object.
	 */
	public Player getCurrentPlayer() {
		return getPlayer(turn);
	}
    
    /**
     * Returns the index of the current player whose turn it is.
     * @return The current player's index as an integer.
     */
    public int getCurrentPlayerIndex() {
        return turn;
    }

	/**
	 * Returns the game board used in the game.
	 * @return The GameBoard object.
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

    /**
     * Returns the GUI interface associated with the game.
     * @return The MonopolyGUI object.
     */
    public MonopolyGUI getGUI() {
        return gui;
    }

	/**
	 * Returns the initial amount of money each player starts with.
	 * @return The initial amount of money as an integer.
	 */
	public int getInitAmountOfMoney() {
		return initAmountOfMoney;
	}
	
	/**
	 * Returns the total number of players currently in the game.
	 * @return The number of players as an integer.
	 */
	public int getNumberOfPlayers() {
		return players.size();
	}

    /**
     * Returns the number of players excluding the current player, used for trading purposes.
     * @return The number of other players as an integer.
     */
    public int getNumberOfSellers() {
        return players.size() - 1;
    }

	/**
	 * Returns the player at the specified index.
	 * @param index The index of the player to retrieve.
	 * @return The Player object at the given index.
	 */
	public Player getPlayer(int index) {
		return (Player)players.get(index);
	}
	
	/**
	 * Returns the index of the specified player in the players list.
	 * @param player The Player object to find the index for.
	 * @return The index of the player or -1 if not found.
	 */
	public int getPlayerIndex(Player player) {
		return players.indexOf(player);
	}

    /**
     * Returns a list of players excluding the current player, representing potential sellers for trades.
     * @return An ArrayList of Player objects excluding the current player.
     */
    public ArrayList<Player> getSellerList() {
        ArrayList<Player> sellers = new ArrayList<Player>();
        for (Iterator<Player> iter = players.iterator(); iter.hasNext();) {
            Player player = (Player) iter.next();
            if(player != getCurrentPlayer()) sellers.add(player);
        }
        return sellers;
    }

	/**
	 * Returns the index of the current turn.
	 * @return The current turn index as an integer.
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Returns the utility dice roll value used in the game for special rolls.
	 * @return The utility dice roll as an integer.
	 */
	public int getUtilDiceRoll() {
		return this.utilDiceRoll;
	}

	/**
	 * Moves the player at the specified index forward by the given dice value and updates the game state accordingly.
	 * @param playerIndex The index of the player to move.
	 * @param diceValue The number of positions to move the player forward.
	 */
	public void movePlayer(int playerIndex, int diceValue) {
		Player player = (Player)players.get(playerIndex);
		movePlayer(player, diceValue);
	}
	
	/**
	 * Moves the specified player forward by the given dice value, handles salary on passing start, updates position, GUI, and player state.
	 * @param player The Player object to move.
	 * @param diceValue The number of positions to move the player forward.
	 */
	public void movePlayer(Player player, int diceValue) {
		Cell currentPosition = player.getPosition();
		int positionIndex = gameBoard.queryCellIndex(currentPosition.getName());
		int newIndex = (positionIndex+diceValue)%gameBoard.getCellNumber();
		if(newIndex <= positionIndex || diceValue > gameBoard.getCellNumber()) {
			player.setMoney(player.getMoney() + 200);
		}
		player.setPosition(gameBoard.getCell(newIndex));
		gui.movePlayer(getPlayerIndex(player), positionIndex, newIndex);
		playerMoved(player);
		updateGUI();
	}

	/**
	 * Handles updates to the GUI and player options after a player has moved to a new position.
	 * @param player The Player who has moved.
	 */
	public void playerMoved(Player player) {
		Cell cell = player.getPosition();
		int playerIndex = getPlayerIndex(player);
		if(cell instanceof CardCell) {
		    gui.setDrawCardEnabled(true);
		} else{
			if(cell.isAvailable()) {
				int price = cell.getPrice();
				if(price <= player.getMoney() && price > 0) {
					gui.enablePurchaseBtn(playerIndex);
				}
			}	
			gui.enableEndTurnBtn(playerIndex);
		}
        gui.setTradeEnabled(turn, false);
	}

	/**
	 * Resets the game state by repositioning players to start, removing cards from the board, and resetting turn index.
	 */
	public void reset() {
		for(int i = 0; i < getNumberOfPlayers(); i++){
			Player player = (Player)players.get(i);
			player.setPosition(gameBoard.getCell(0));
		}
		if(gameBoard != null) gameBoard.removeCards();
		turn = 0;
	}
	
	/**
	 * Rolls two dice unless in test mode, in which case it retrieves the dice roll from the GUI.
	 * @return An array of two integers representing the dice roll values.
	 */
	public int[] rollDice() {
		if(testMode) {
			return gui.getDiceRoll();
		}
		else {
			return new int[]{
					dice[0].getRoll(),
					dice[1].getRoll()
			};
		}
	}
	
	/**
	 * Sends the specified player to the Jail position, sets them as in jail, and updates the GUI.
	 * @param player The Player to send to jail.
	 */
	public void sendToJail(Player player) {
	    int oldPosition = gameBoard.queryCellIndex(getCurrentPlayer().getPosition().getName());
		player.setPosition(gameBoard.queryCell("Jail"));
		player.setInJail(true);
		int jailIndex = gameBoard.queryCellIndex("Jail");
		gui.movePlayer(
		        getPlayerIndex(player),
		        oldPosition,
		        jailIndex);
	}
    
	/**
	 * Enables or disables all main GUI buttons based on the given boolean flag.
	 * @param enabled True to enable buttons, false to disable.
	 */
	private void setAllButtonEnabled(boolean enabled) {
		gui.setRollDiceEnabled(enabled);
		gui.setPurchasePropertyEnabled(enabled);
		gui.setEndTurnEnabled(enabled);
        gui.setTradeEnabled(turn, enabled);
        gui.setBuyHouseEnabled(enabled);
        gui.setDrawCardEnabled(enabled);
        gui.setGetOutOfJailEnabled(enabled);
	}

	/**
	 * Sets the game board to the specified GameBoard object.
	 * @param board The GameBoard to set.
	 */
	public void setGameBoard(GameBoard board) {
		this.gameBoard = board;
	}
	
	/**
	 * Assigns the GUI interface to the specified MonopolyGUI object.
	 * @param gui The MonopolyGUI to associate.
	 */
	public void setGUI(MonopolyGUI gui) {
		this.gui = gui;
	}

	/**
	 * Sets the initial amount of money players start with to the specified value.
	 * @param money The initial money amount to set.
	 */
	public void setInitAmountOfMoney(int money) {
		this.initAmountOfMoney = money;
	}

	/**
	 * Sets the number of players by creating new Player instances with initial money, clearing previous players.
	 * @param number The desired number of players.
	 */
	public void setNumberOfPlayers(int number) {
		players.clear();
		for(int i =0;i<number;i++) {
			Player player = new Player();
			player.setMoney(initAmountOfMoney);
			players.add(player);
		}
	}

	/**
	 * Sets the utility dice roll value used for special rolls.
	 * @param diceRoll The utility dice roll value to set.
	 */
	public void setUtilDiceRoll(int diceRoll) {
		this.utilDiceRoll = diceRoll;
	}
	
	/**
	 * Starts the game by initializing the GUI and enabling the first player's turn and trade options.
	 */
	public void startGame() {
		gui.startGame();
		gui.enablePlayerTurn(0);
        gui.setTradeEnabled(0, true);
	}

	/**
	 * Switches to the next player's turn and updates GUI buttons based on their jail status and house buying ability.
	 */
	public void switchTurn() {
		turn = (turn + 1) % getNumberOfPlayers();
		if(!getCurrentPlayer().isInJail()) {
			gui.enablePlayerTurn(turn);
			gui.setBuyHouseEnabled(getCurrentPlayer().canBuyHouse());
            gui.setTradeEnabled(turn, true);
		}
		else {
			gui.setGetOutOfJailEnabled(true);
		}
	}
	
	/**
	 * Updates the GUI display to reflect the current game state.
	 */
	public void updateGUI() {
		gui.update();
	}

	/**
	 * Prompts the GUI to perform and return a utility dice roll, saving it to internal state.
	 */
	public void utilRollDice() {
		this.utilDiceRoll = gui.showUtilDiceRoll();
	}

	/**
	 * Enables or disables test mode, affecting dice roll behavior.
	 * @param b True to enable test mode, false to disable.
	 */
	public void setTestMode(boolean b) {
		testMode = b;
	}
}
