package traina.david.minesweepergame;

import java.io.Serializable;
import java.util.Observable;

import traina.david.slidingtilegame.R;

class Cell extends Observable implements Serializable {

    /**
     * If Cell is a bomb, value = -1. If it is blank cell, value = 0.
     * Otherwise, value = num of neighbouring bombs.
     */
    private final int value;

    /**
     * The image associated with the Cell's value.
     */
    private int imageId;

    /**
     * Whether the Cell has been clicked/revealed.
     */
    private boolean isRevealed;

    /**
     * Whether the Cell is a bomb.
     */
    private final boolean isBomb;

    /**
     * Whether the Cell has been flagged by user.
     */
    private boolean isFlagged;

    /**
     * Display for an unopened or flagged cell.
     */
    private final int blankImageID;

    /**
     * Return whether this Cell has been flagged as possible mine.
     * @return Boolean
     */
    boolean isFlagged() {
        return isFlagged;
    }

    /**
     * A setter for isFlagged
     * @param flag True if the cell should be changed to flagged. False if it should be un-flagged
     */
    void setFlagged(Boolean flag){
        isFlagged = flag;
        setChanged();
        notifyObservers();
    }

    /**
     * Getter for blankImageID.
     */
    int getBlankImageID() {
        return blankImageID;
    }

    /**
     * Get this cell's value.
     */
    public int getValue() {
        return value;
    }

    /**
     * A getter for imageId
     * @return imageId the resource for the cells revealed background
     */
    int getImageId() {
        return imageId;
    }

    /**
     * A setter for cells imageId
     */
    void setImageId() {
        this.imageId = R.drawable.bomb_exploded;
    }

    /**
     * A getter for isRevealed
     * @return True if the cell is revealed. false if not
     */
    boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Sets a tile to be revealed(this method is only called on unrevealed tile) and
     * notifies observers
     */
    void setRevealed() {
        isRevealed = true;
        setChanged();
        notifyObservers();
    }

    /**
     * A getter for isBomb
     * @return true is the cell is a bomb. false if not
     */
    boolean isBomb() {
        return isBomb;
    }

    Cell(int value) {
        this.value = value;
        this.isBomb = (value == -1);
        this.isRevealed = false;
        this.isFlagged = false;
        this.blankImageID = R.drawable.button;

        switch (value + 1) {
            case 0:
                imageId = R.drawable.bomb_normal;
                break;
            case 1:
                imageId = R.drawable.number_0;
                break;
            case 2:
                imageId = R.drawable.number_1;
                break;
            case 3:
                imageId = R.drawable.number_2;
                break;
            case 4:
                imageId = R.drawable.number_3;
                break;
            case 5:
                imageId = R.drawable.number_4;
                break;
            case 6:
                imageId = R.drawable.number_5;
                break;
            case 7:
                imageId = R.drawable.number_6;
                break;
            case 8:
                imageId = R.drawable.number_7;
                break;
            case 9:
                imageId = R.drawable.number_8;
                break;
        }
    }
}
