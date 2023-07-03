package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;

public class CardCtrl {
    private final TalioMainCtrl mainCtrl;
    private final ServerUtils serverUtils;

    private BoardViewCtrl boardViewCtrl;

    private Card card;

    @FXML
    private TextField title;

    @FXML
    private ImageView indicator;

    /** Constructor for the CardCTrl
     * @param mainCtrl the main controller
     * @param serverUtils the server utils
     */
    @Inject
    public CardCtrl(TalioMainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }


    /** Setter for the boardViewCtrl
     * @param boardViewCtrl the boardViewCtrl
     */
    public void setBoardViewCtrl(BoardViewCtrl boardViewCtrl) {
        this.boardViewCtrl = boardViewCtrl;
    }

    /** Sets the state of the card
     * @param card the card to be set
     */
    public void setState(Card card) {
        title.setText(card.name);
        this.card = card;

        //Only shows the indicator if the card has a description
        if(card.description == null || card.description.strip().equals("")){
            indicator.setVisible(false);
        }
    }

    /**
     * Calls the method to rename the card
     */
    public void rename(){
        this.card.name = title.getText();
        serverUtils.updateCard(card, card.list.board.id);
    }
}
