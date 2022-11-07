package com.sep.kitc.adapter.storage.entity;

public class RegisteredPlayer extends Bot{
    private final String userIdent;

    /**
     * Wenn sich ein Spieler registriert, dann bekommt dieser eine einzigartige Benutzerkennung (UserID)
     * @param player Spieler der sich registrieren möchte.
     * @param userIdent einzigartige Benutzerkennung.
     */
    public RegisteredPlayer(Player player, String userIdent) {
        super(player.getUsername(), player.getPassword());
        this.userIdent = userIdent;
    }
    public String getUserIdent() {
        return userIdent;
    }
}
