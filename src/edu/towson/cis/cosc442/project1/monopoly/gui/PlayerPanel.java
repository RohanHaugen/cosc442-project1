package edu.towson.cis.cosc442.project1.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.border.BevelBorder;

import edu.towson.cis.cosc442.project1.monopoly.*;

public class PlayerPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnBuyHouse;
    private JButton btnDrawCard;
    private JButton btnEndTurn;
    private JButton btnGetOutOfJail;
    private JButton btnPurchaseProperty;
    private JButton btnRollDice;
    private JButton btnTrade;
    
    private JLabel lblMoney;
    private JLabel lblName;
    
    private Player player;
    
    private JTextArea txtProperty;

    /**
     * Creates a PlayerPanel GUI component initialized for the specified player.
     * @param player the Player object whose information is displayed and controlled by this panel
     */
    public PlayerPanel(Player player) {
        JPanel pnlAction = new JPanel();
        JPanel pnlInfo = new JPanel();
        btnRollDice = new JButton("Roll Dice");
        btnPurchaseProperty = new JButton("Purchase Property");
        btnEndTurn = new JButton("End Turn");
        btnBuyHouse = new JButton("Buy House");
        btnGetOutOfJail = new JButton("Get Out of Jail");
        btnDrawCard = new JButton("Draw Card");
        btnTrade = new JButton("Trade");
        this.player = player;
        lblName = new JLabel();
        lblMoney = new JLabel();
        txtProperty = new JTextArea(30, 70);

        txtProperty.setEnabled(false);

        JPanel pnlName = new JPanel();
        JPanel pnlProperties = new JPanel();

        pnlInfo.setLayout(new BorderLayout());
        pnlInfo.add(pnlName, BorderLayout.NORTH);
        pnlInfo.add(pnlProperties, BorderLayout.CENTER);

        pnlProperties.setLayout(new OverlayLayout(pnlProperties));

        pnlName.add(lblName);
        pnlName.add(lblMoney);
        pnlProperties.add(txtProperty);

        pnlAction.setLayout(new GridLayout(3, 3));
        pnlAction.add(btnBuyHouse);
        pnlAction.add(btnRollDice);
        pnlAction.add(btnPurchaseProperty);
        pnlAction.add(btnGetOutOfJail);
        pnlAction.add(btnEndTurn);
        pnlAction.add(btnDrawCard);
        pnlAction.add(btnTrade);

        pnlAction.doLayout();
        pnlInfo.doLayout();
        pnlName.doLayout();
        pnlProperties.doLayout();
        this.doLayout();

        setLayout(new BorderLayout());
        add(pnlInfo, BorderLayout.CENTER);
        add(pnlAction, BorderLayout.SOUTH);

        btnRollDice.setEnabled(false);
        btnPurchaseProperty.setEnabled(false);
        btnEndTurn.setEnabled(false);
        btnBuyHouse.setEnabled(false);
        btnGetOutOfJail.setEnabled(false);
        btnDrawCard.setEnabled(false);
        btnTrade.setEnabled(false);

        setBorder(new BevelBorder(BevelBorder.RAISED));

        btnRollDice.addActionListener(new ActionListener() {
            /**
             * This signature appears multiple times for different anonymous inner classes handling button click events; each handles the specific button's action by delegating to the GameMaster instance.
             * @param e the ActionEvent triggered by the button click
             */
            public void actionPerformed(ActionEvent e) {
                GameMaster.instance().btnRollDiceClicked();
            }
        });

        btnEndTurn.addActionListener(new ActionListener() {
            /**
             * This signature appears multiple times for different anonymous inner classes handling button click events; each handles the specific button's action by delegating to the GameMaster instance.
             * @param e the ActionEvent triggered by the button click
             */
            public void actionPerformed(ActionEvent e) {
                GameMaster.instance().btnEndTurnClicked();
            }
        });

        btnPurchaseProperty.addActionListener(new ActionListener() {
            /**
             * This signature appears multiple times for different anonymous inner classes handling button click events; each handles the specific button's action by delegating to the GameMaster instance.
             * @param e the ActionEvent triggered by the button click
             */
            public void actionPerformed(ActionEvent e) {
                GameMaster.instance().btnPurchasePropertyClicked();
            }
        });

        btnBuyHouse.addActionListener(new ActionListener() {
            /**
             * This signature appears multiple times for different anonymous inner classes handling button click events; each handles the specific button's action by delegating to the GameMaster instance.
             * @param e the ActionEvent triggered by the button click
             */
            public void actionPerformed(ActionEvent e) {
                GameMaster.instance().btnBuyHouseClicked();
            }
        });

        btnGetOutOfJail.addActionListener(new ActionListener() {
            /**
             * This signature appears multiple times for different anonymous inner classes handling button click events; each handles the specific button's action by delegating to the GameMaster instance.
             * @param e the ActionEvent triggered by the button click
             */
            public void actionPerformed(ActionEvent e) {
                GameMaster.instance().btnGetOutOfJailClicked();
            }
        });

        btnDrawCard.addActionListener(new ActionListener() {
            /**
             * This signature appears multiple times for different anonymous inner classes handling button click events; each handles the specific button's action by delegating to the GameMaster instance.
             * @param e the ActionEvent triggered by the button click
             */
            public void actionPerformed(ActionEvent e) {
                Card card = GameMaster.instance().btnDrawCardClicked();
                JOptionPane
                        .showMessageDialog(PlayerPanel.this, card.getLabel());
                displayInfo();
            }
        });

        btnTrade.addActionListener(new ActionListener() {
            /**
             * This signature appears multiple times for different anonymous inner classes handling button click events; each handles the specific button's action by delegating to the GameMaster instance.
             * @param e the ActionEvent triggered by the button click
             */
            public void actionPerformed(ActionEvent e) {
                GameMaster.instance().btnTradeClicked();
            }
        });
    }

    /**
     * Updates the panel's displayed information to reflect the current player's name, money, and owned properties.
     */
    public void displayInfo() {
        lblName.setText(player.getName());
        lblMoney.setText("$ " + player.getMoney());
        StringBuffer buf = new StringBuffer();
        Cell[] cells = player.getAllProperties();
        for (int i = 0; i < cells.length; i++) {
            buf.append(cells[i]).append("\n");
        }
        txtProperty.setText(buf.toString());
    }
    
    /**
     * Checks if the 'Buy House' button is currently enabled.
     * @return true if the 'Buy House' button is enabled; false otherwise
     */
    public boolean isBuyHouseButtonEnabled() {
        return btnBuyHouse.isEnabled();
    }

    /**
     * Checks if the 'Draw Card' button is currently enabled.
     * @return true if the 'Draw Card' button is enabled; false otherwise
     */
    public boolean isDrawCardButtonEnabled() {
        return btnDrawCard.isEnabled();
    }

    /**
     * Checks if the 'End Turn' button is currently enabled.
     * @return true if the 'End Turn' button is enabled; false otherwise
     */
    public boolean isEndTurnButtonEnabled() {
        return btnEndTurn.isEnabled();
    }
    
    /**
     * Checks if the 'Get Out of Jail' button is currently enabled.
     * @return true if the 'Get Out of Jail' button is enabled; false otherwise
     */
    public boolean isGetOutOfJailButtonEnabled() {
        return btnGetOutOfJail.isEnabled();
    }
    
    /**
     * Checks if the 'Purchase Property' button is currently enabled.
     * @return true if the 'Purchase Property' button is enabled; false otherwise
     */
    public boolean isPurchasePropertyButtonEnabled() {
        return btnPurchaseProperty.isEnabled();
    }
    
    /**
     * Checks if the 'Roll Dice' button is currently enabled.
     * @return true if the 'Roll Dice' button is enabled; false otherwise
     */
    public boolean isRollDiceButtonEnabled() {
        return btnRollDice.isEnabled();
    }

    /**
     * Checks if the 'Trade' button is currently enabled.
     * @return true if the 'Trade' button is enabled; false otherwise
     */
    public boolean isTradeButtonEnabled() {
        return btnTrade.isEnabled();
    }

    /**
     * Enables or disables the 'Buy House' button on the panel.
     * @param b true to enable the button; false to disable it
     */
    public void setBuyHouseEnabled(boolean b) {
        btnBuyHouse.setEnabled(b);
    }

    /**
     * Enables or disables the 'Draw Card' button on the panel.
     * @param b true to enable the button; false to disable it
     */
    public void setDrawCardEnabled(boolean b) {
        btnDrawCard.setEnabled(b);
    }

    /**
     * Enables or disables the 'End Turn' button on the panel.
     * @param enabled true to enable the button; false to disable it
     */
    public void setEndTurnEnabled(boolean enabled) {
        btnEndTurn.setEnabled(enabled);
    }

    /**
     * Enables or disables the 'Get Out of Jail' button on the panel.
     * @param b true to enable the button; false to disable it
     */
    public void setGetOutOfJailEnabled(boolean b) {
        btnGetOutOfJail.setEnabled(b);
    }

    /**
     * Enables or disables the 'Purchase Property' button on the panel.
     * @param enabled true to enable the button; false to disable it
     */
    public void setPurchasePropertyEnabled(boolean enabled) {
        btnPurchaseProperty.setEnabled(enabled);
    }

    /**
     * Enables or disables the 'Roll Dice' button on the panel.
     * @param enabled true to enable the button; false to disable it
     */
    public void setRollDiceEnabled(boolean enabled) {
        btnRollDice.setEnabled(enabled);
    }

    /**
     * Enables or disables the 'Trade' button on the panel.
     * @param b true to enable the button; false to disable it
     */
    public void setTradeEnabled(boolean b) {
        btnTrade.setEnabled(b);
    }
}