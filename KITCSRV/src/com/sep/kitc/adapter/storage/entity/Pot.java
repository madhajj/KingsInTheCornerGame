package com.sep.kitc.adapter.storage.entity;

import java.io.Serializable;

public class Pot implements Serializable {
    private int chips = 0;
    public int getChips() {
        return chips;
    }
    public void addChips(int toAddChips) {
        chips += toAddChips;
    }
    public void resetPot() {
        chips = 0;
    }
}
