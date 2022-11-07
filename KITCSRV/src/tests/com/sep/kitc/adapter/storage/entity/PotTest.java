package com.sep.kitc.adapter.storage.entity;

import org.junit.Test;

import static org.junit.Assert.*;

public class PotTest {

    @Test
    public void addChips() {
        Pot pot = new Pot();
        pot.addChips(5);

        assertNotEquals(3, pot.getChips());
        assertEquals(5, pot.getChips());


    }

    @Test
    public void resetPot() {
        Pot pot = new Pot();

        pot.addChips(30);
        pot.resetPot();

        assertNotEquals(30, pot.getChips());
        assertEquals(0, pot.getChips());
    }
}