package com.sep.kitc;

import javafx.scene.image.Image;

import java.io.File;

/**
 * Dies ist die Klasse, die jeden Kartennamen mit seinem Bild verknüpft.
 */
public enum ShowImage {
    TWOCLUBS("2 of Clubs", "src/main/resources/images/2_of_clubs.png"),
    TWODIAMONDS("2 of Diamonds", "src/main/resources/images/2_of_diamonds.png"),
    TWOHEARTS("2 of Hearts", "src/main/resources/images/2_of_hearts.png"),
    TWOSPADES("2 of Spades", "src/main/resources/images/2_of_spades.png"),
    THREECLUBS("3 of Clubs","src/main/resources/images/3_of_clubs.png"),
    THREEDIAMONDS("3 of Diamonds","src/main/resources/images/3_of_diamonds.png"),
    THREEHEARTS("3 of Hearts","src/main/resources/images/3_of_hearts.png"),
    THREESPADES("3 of Spades","src/main/resources/images/3_of_spades.png"),
    FOURCLUBS("4 of Clubs","src/main/resources/images/4_of_clubs.png"),
    FOURDIAMONDS("4 of Diamonds","src/main/resources/images/4_of_diamonds.png"),
    FOURHEARTS("4 of Hearts","src/main/resources/images/4_of_hearts.png"),
    FOURSPADES("4 of Spades","src/main/resources/images/4_of_spades.png"),
    FIVECLUBS("5 of Clubs","src/main/resources/images/5_of_clubs.png"),
    FIVEDIAMONDS("5 of Diamonds","src/main/resources/images/5_of_diamonds.png"),
    FIVEHEARTS("5 of Hearts","src/main/resources/images/5_of_hearts.png"),
    FIVESPADES("5 of Spades","src/main/resources/images/5_of_spades.png"),
    SIXCLUBS("6 of Clubs","src/main/resources/images/6_of_clubs.png"),
    SIXDIAMONDS("6 of Diamonds","src/main/resources/images/6_of_diamonds.png"),
    SIXHEARTS("6 of Hearts","src/main/resources/images/6_of_hearts.png"),
    SIXSPADES("6 of Spades","src/main/resources/images/6_of_spades.png"),
    SEVENCLUBS("7 of Clubs","src/main/resources/images/7_of_clubs.png"),
    SEVENDIAMONDS("7 of Diamonds","src/main/resources/images/7_of_diamonds.png"),
    SEVENHEARTS("7 of Hearts","src/main/resources/images/7_of_hearts.png"),
    SEVENSPADES("7 of Spades","src/main/resources/images/7_of_spades.png"),
    EIGHTCLUBS("8 of Clubs","src/main/resources/images/8_of_clubs.png"),
    EIGHTDIAMONDS("8 of Diamonds","src/main/resources/images/8_of_diamonds.png"),
    EIGHTHEARTS("8 of Hearts","src/main/resources/images/8_of_hearts.png"),
    EIGHTSPADES("8 of Spades","src/main/resources/images/8_of_spades.png"),
    NINECLUBS("9 of Clubs","src/main/resources/images/9_of_clubs.png"),
    NINEDIAMONDS("9 of Diamonds","src/main/resources/images/9_of_diamonds.png"),
    NINEHEARTS("9 of Hearts","src/main/resources/images/9_of_hearts.png"),
    NINESPADES("9 of Spades","src/main/resources/images/9_of_spades.png"),
    TENCLUBS("10 of Clubs","src/main/resources/images/10_of_clubs.png"),
    TENDIAMONS("10 of Diamonds","src/main/resources/images/10_of_diamonds.png"),
    TENHEARTS("10 of Hearts","src/main/resources/images/10_of_hearts.png"),
    TENSPADES("10 of Spades","src/main/resources/images/10_of_spades.png"),
    JACKCLUBS("Jack of Clubs","src/main/resources/images/jack_of_clubs.png"),
    JACKDIAMONS("Jack of Diamonds","src/main/resources/images/jack_of_diamonds.png"),
    JACKHEARTS("Jack of Hearts","src/main/resources/images/jack_of_hearts.png"),
    JACKSPADES("Jack of Spades","src/main/resources/images/jack_of_spades.png"),
    QUEENCLUBS("Queen of Clubs","src/main/resources/images/queen_of_clubs.png"),
    QUEENDIAMONDS("Queen of Diamonds","src/main/resources/images/queen_of_diamonds.png"),
    QUEENHEARTS("Queen of Hearts","src/main/resources/images/queen_of_hearts.png"),
    QUEENSPADES("Queen of Spades","src/main/resources/images/queen_of_spades.png"),
    KINGCLUBS("King of Clubs","src/main/resources/images/king_of_clubs.png"),
    KINGDIAMONDS("King of Diamonds","src/main/resources/images/king_of_diamonds.png"),
    KINGHEARTS("King of Hearts","src/main/resources/images/king_of_hearts.png"),
    KINGSPADES("King of Spades","src/main/resources/images/king_of_spades.png"),
    ACECLUBS("Ace of Clubs","src/main/resources/images/ace_of_clubs.png"),
    ACEDIAMONDS("Ace of Diamonds","src/main/resources/images/ace_of_diamonds.png"),
    ACEHEARTS("Ace of Hearts","src/main/resources/images/ace_of_hearts.png"),
    ACESPADES("Ace of Spades","src/main/resources/images/ace_of_spades.png"),
    BACKOFCARD("Back of Card", "src/main/resources/images/back.png"),
    WHITECARD("White Card", "src/main/resources/images/white.png");

    private String cardName;
    private Image cardImage;

    ShowImage(String cardName, String imageLoc) {
        this.cardName = cardName;
        this.cardImage = new Image(new File(imageLoc).toURI().toString());
    }
    public String getCardName() {
        return cardName;
    }
    public Image getImage() {
        return cardImage;
    }
    public static String readCardName(Image image) {
        for (ShowImage s: ShowImage.values()) {
            if (s.getImage().equals(image)) {
                return s.getCardName();
            }
        }
        return null;
    }
    public static Image findImage(String cardName) {
        for (ShowImage s: ShowImage.values()) {
            if (s.getCardName().equals(cardName)) {
                return s.getImage();
            }
        }
        return null;
    }
}
