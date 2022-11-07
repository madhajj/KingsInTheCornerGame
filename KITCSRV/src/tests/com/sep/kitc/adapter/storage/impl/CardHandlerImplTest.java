package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.adapter.storage.entity.*;
import com.sep.kitc.adapter.storage.service.CreateGameRoomService;
import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.exception.*;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class CardHandlerImplTest {

    @Test
    public void fillCardStorage() {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        Card card = cardHandler.getCardOf("King of Spades");
        assertNotNull(card);
        assertTrue(card.isKing());
        assertTrue(card.getColor().equals("Black"));
        assertEquals(card.getValue(), 13);
        assertTrue(card.getSuit().equals("Spades"));
    }

    @Test
    public void distributeChips() throws RegisterException, CreateGameRoomException, RemoteException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.distributeChips("Spielraum");
        assertEquals( cardHandler.getThePlayersChips(playerId), 40);

        boolean b = false;
        try {
            CardHandlerImpl cardHandler2 = new CardHandlerImpl();
            RegistrationService registrationService2 = new RegistrationService();
            Player player2 = new Player("vasile", "85900");
            String playerId2 = registrationService.register(player);
            GameRoomServiceImpl gameRoomService3 = new GameRoomServiceImpl();
            gameRoomService.createGameRoom("Spielraum2", "420024", 2);
            gameRoomService.joinGameRoom("Spielraum2", "420024", playerId);
            cardHandler2.distributeChips("Spielraum2");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

        boolean b1 = false;
        try {
            cardHandler.distributeChips("Spielraum3");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void distributeCards() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        assertEquals( registrationService.getRegisteredPlayer(playerId).getHand().size(), 7);
        assertNotNull(createGameRoomService.getGameRoom("Spielraum").getGameStalls().get("northStall").getTopCard());

        boolean b = false;
        try {
            cardHandler.distributeCards("Spielraum", "tgrfsz");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);


        b = false;
        try {
            cardHandler.distributeCards("Spielraumwecec", playerId);
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

    @Test
    public void hasSomeoneWon()throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        assertTrue(cardHandler.hasSomeoneWon("Spielraum"));
        cardHandler.distributeCards("Spielraum", playerId);
        assertFalse(cardHandler.hasSomeoneWon("Spielraum"));
        boolean b = false;
        try {
            cardHandler.hasSomeoneWon("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

        b = false;
        try {
            CardHandlerImpl cardHandler2 = new CardHandlerImpl();
            RegistrationService registrationService2 = new RegistrationService();
            Player player2 = new Player("vasile", "85900");
            String playerId2 = registrationService.register(player);
            GameRoomServiceImpl gameRoomService3 = new GameRoomServiceImpl();
            gameRoomService.createGameRoom("Spielraum2", "420024", 2);
            gameRoomService.joinGameRoom("Spielraum2", "420024", playerId);
            cardHandler2.hasSomeoneWon("Spielraum2");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

    @Test
    public void addBot()throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 4);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        cardHandler.addEasyBot("Spielraum");
        assertEquals(cardHandler.getNumberOfPlayersInGameRoom("Spielraum"), 2);
        assertEquals(createGameRoomService.getGameRoom("Spielraum").getBotCounter(), 1);
        assertEquals(cardHandler.botsInGameRoom("Spielraum"), 1);
        cardHandler.addMediumBot("Spielraum");
        assertEquals(cardHandler.getNumberOfPlayersInGameRoom("Spielraum"), 3);
        assertEquals(createGameRoomService.getGameRoom("Spielraum").getBotCounter(), 2);
        assertEquals(cardHandler.botsInGameRoom("Spielraum"), 2);
        boolean b = false;
        try {
            cardHandler.addEasyBot("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

        b = false;
        try {
            cardHandler.addMediumBot("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

        b = false;
        try {
            cardHandler.botsInGameRoom("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

    /*
    @Test
    public void botPlaysCard()throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 4);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.addEasyBot("Spielraum");
        cardHandler.addMediumBot("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        cardHandler.botPlaysCard("Spielraum");

        boolean b = false;
        try {
            cardHandler.botPlaysCard("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

     */

    @Test
    public void whoWonRound()throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 4);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.addEasyBot("Spielraum");
        cardHandler.addMediumBot("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        //cardHandler.botPlaysCard("Spielraum");
        boolean b = false;
        try{
            cardHandler.whoWonRound("Spielraum");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (b) assertTrue(true);
        else assertTrue(false);

        b = false;
        try {
            cardHandler.whoWonRound("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

    @Test
    public void payChipsToPodAfterRoundEnded()throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("gm", "420024", 4);
        gameRoomService.joinGameRoom("gm", "420024", playerId);
        cardHandler.setDealer("gm");
        cardHandler.addEasyBot("gm");
        cardHandler.addMediumBot("gm");
        cardHandler.distributeChips("gm");
        cardHandler.distributeCards("gm", playerId);
        cardHandler.payChipsToPodAfterRoundEnded("gm");
        assertEquals(cardHandler.getPotChips("gm"), 21);
        boolean b = false;
        try{
            cardHandler.payChipsToPodAfterRoundEnded("hiwd'hi");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);


    }



    @Test
    public void getHandOfPlayer() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        assertEquals(cardHandler.getHandOfPlayer(playerId).size(), 7);
        boolean b = false;
        try {
            cardHandler.getHandOfPlayer("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

    @Test
    public void isDeckOfGameRoomEmpty() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("gm", "420024", 2);
        gameRoomService.joinGameRoom("gm", "420024", playerId);
        cardHandler.setDealer("gm");
        cardHandler.distributeChips("gm");
        cardHandler.distributeCards("gm", playerId);
        assertFalse(cardHandler.isDeckOfGameRoomEmpty("gm"));
        boolean b = false;
        try {
            cardHandler.isDeckOfGameRoomEmpty("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

    @Test
    public void emptyThePlayersHand() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        cardHandler.emptyThePlayersHand(playerId);
        assertEquals(cardHandler.getHandOfPlayer(playerId).size(), 0);
        boolean b = false;
        try {
            cardHandler.emptyThePlayersHand("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);


    }

    @Test
    public void giveThePlayerWhoWonRoundHisScore() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, PasswordIncorrectException, UserNotCreatedException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        Player player2 = new Player("ghenadie", "456456");
        String playerId2 = registrationService.register(player2);
        gameRoomService.createGameRoom("gm", "420024", 4);
        gameRoomService.joinGameRoom("gm", "420024", playerId);
        cardHandler.setDealer("gm");
        cardHandler.addEasyBot("gm");
        cardHandler.addMediumBot("gm");
        cardHandler.distributeChips("gm");
        cardHandler.distributeCards("gm", playerId);
        gameRoomService.joinGameRoom("gm", "420024", playerId2);
        cardHandler.payChipsToPodAfterRoundEnded("gm");
        registrationService.getRegisteredPlayer(playerId2).setWonRound(true);
        cardHandler.giveThePlayerWhoWonRoundHisScore("gm");
        assertEquals(cardHandler.getPotChips("gm"),0);
        assertEquals(registrationService.getRegisteredPlayer(playerId2).getScore(), 21);
        boolean b = false;
        try{
            cardHandler.giveThePlayerWhoWonRoundHisScore("hiwd'hi");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void endRound() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, PasswordIncorrectException, UserNotCreatedException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        Player player2 = new Player("ghenadie", "456456");
        String playerId2 = registrationService.register(player2);
        gameRoomService.createGameRoom("gm", "420024", 4);
        gameRoomService.joinGameRoom("gm", "420024", playerId);
        cardHandler.setDealer("gm");
        cardHandler.addEasyBot("gm");
        cardHandler.addMediumBot("gm");
        cardHandler.distributeChips("gm");
        cardHandler.distributeCards("gm", playerId);
        gameRoomService.joinGameRoom("gm", "420024", playerId2);
        registrationService.getRegisteredPlayer(playerId2).setWonRound(true);
        cardHandler.endRound("gm");
        assertEquals(cardHandler.getPotChips("gm"),0);
        assertEquals(registrationService.getRegisteredPlayer(playerId).getScore(), 0);
        assertFalse(registrationService.getRegisteredPlayer(playerId).hasWonRound());
        assertFalse(registrationService.getRegisteredPlayer(playerId).isPlayersTurn());
        assertEquals(registrationService.getRegisteredPlayer(playerId).getHand().size(), 0);
        assertEquals(registrationService.getRegisteredPlayer(playerId2).getScore(), 21);
        assertFalse(registrationService.getRegisteredPlayer(playerId2).hasWonRound());
        assertFalse(registrationService.getRegisteredPlayer(playerId2).isPlayersTurn());
        assertEquals(registrationService.getRegisteredPlayer(playerId2).getHand().size(), 0);

        boolean b = false;
        try{
            cardHandler.endRound("hiwd'hi");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void getTheGameStalls() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        assertEquals(cardHandler.getTheGameStalls("Spielraum").get("eastStall").size(), 2);

        boolean b = false;
        try {
            cardHandler.getTheGameStalls("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

    }

    @Test
    public void getThePlayersChips() {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        boolean b = false;
        try {
            cardHandler.getThePlayersChips("ewoufhuowuh");
        }catch(RuntimeException | RemoteException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void getThePlayersScore() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        assertEquals(cardHandler.getThePlayersScore(playerId), 0);

        boolean b = false;
        try {
            cardHandler.getThePlayersScore("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void getPotChips() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        assertEquals(cardHandler.getPotChips("Spielraum"), 0);

        boolean b = false;
        try {
            cardHandler.getPotChips("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void startTurn() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        cardHandler.startTurn(playerId);
        assertTrue(cardHandler.isItThePlayersTurn(playerId));

        boolean b = false;
        try {
            cardHandler.startTurn("ewoufhuowuh");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertFalse(false);
    }

    @Test
    public void endTurn() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, NoMoreCardsInDeckException, PasswordIncorrectException, NotThePlayersTurnException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        Player player2 = new Player("Stephan", "666552");
        String playerId = registrationService.register(player);
        String playerId2 = registrationService.register(player2);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        cardHandler.startTurn(playerId);
        int numberOfCards = cardHandler.getHandOfPlayer(playerId).size();
        cardHandler.endTurn(playerId, "Spielraum");
        assertEquals(cardHandler.getHandOfPlayer(playerId).size(), numberOfCards + 1);


        boolean b;
        b = false;
        try {
            cardHandler.endTurn("playerId", "Spielraum");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertFalse(false);
    }

    @Test
    public void removeBot() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, PasswordIncorrectException, UserNotCreatedException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        String playerId = registrationService.register(player);
        Player player2 = new Player("ghenadie", "456456");
        String playerId2 = registrationService.register(player2);
        gameRoomService.createGameRoom("gm", "420024", 4);
        gameRoomService.joinGameRoom("gm", "420024", playerId);
        cardHandler.setDealer("gm");
        cardHandler.addEasyBot("gm");
        cardHandler.addMediumBot("gm");
        cardHandler.distributeChips("gm");
        cardHandler.distributeCards("gm", playerId);
        gameRoomService.joinGameRoom("gm", "420024", playerId2);
        cardHandler.removeBot("gm");
        assertEquals(createGameRoomService.getGameRoom("gm").getBotCounter(),1);
        assertEquals(cardHandler.getNumberOfPlayersInGameRoom("gm"), 2);
        boolean b = false;
        try{
            cardHandler.removeBot("hiwd'hi");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }


    @Test
    public void isItThePlayersTurn() {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        boolean b = false;
        try {
            cardHandler.isItThePlayersTurn("ewoufhuowuh");
        }catch(RuntimeException | RemoteException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void playThisCard() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, UserNotCreatedException, NoMoreCardsInDeckException, IllegalMoveException, NotThePlayersTurnException, PasswordIncorrectException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        RegistrationService registrationService = new RegistrationService();
        GameRoomServiceImpl gameRoomService = new GameRoomServiceImpl();
        Player player = new Player("vasile", "85900");
        Player player2 = new Player("Stephan", "666552");
        String playerId = registrationService.register(player);
        String playerId2 = registrationService.register(player2);
        gameRoomService.createGameRoom("Spielraum", "420024", 2);
        gameRoomService.joinGameRoom("Spielraum", "420024", playerId);
        cardHandler.setDealer("Spielraum");
        cardHandler.distributeChips("Spielraum");
        cardHandler.distributeCards("Spielraum", playerId);
        cardHandler.startTurn(playerId);
        String card = cardHandler.getHandOfPlayer(playerId).get(4);
        boolean b = false;
        try {
            cardHandler.playThisCard("newihfo", "Spielraum", "westStall", card);
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);

        b = false;
        try {
            cardHandler.playThisCard(playerId, "rgeouh", "westStall", card);
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }




    @Test
    public void mergeTheseStalls() throws CreateGameRoomException, RegisterException, IllegalMoveException, RemoteException {
        RegistrationService registrationService = new RegistrationService();
        CreateGameRoomService gameRoomService = new CreateGameRoomService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        String stall1Name = "northStall";
        String stall2Name = "southStall";
        Player player = new Player("Zarzavat" ,"123");
        Card card1 = new Card("Black", "Spades", 13);
        Card card2 = new Card("Red", "Hearts", 12);
        Card card3 = new Card("Black", "Spades", 11);
        Card card4 = new Card("Red", "Diamonds", 10);
        GameRoom gm1 = new GameRoom("gm1", "abc", 4);

        String playerID = registrationService.register(player);

        gameRoomService.createGameRoom(gm1);

        Stall stall1 = gm1.getGameStalls().get(stall1Name);
        Stall stall2 = gm1.getGameStalls().get(stall2Name);

        stall1.addCardToStall(card1);
        stall1.addCardToStall(card2);
        stall2.addCardToStall(card3);
        stall2.addCardToStall(card4);

        try {
            cardHandler.mergeTheseStalls(playerID, "room", stall1Name, stall2Name);
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        cardHandler.mergeTheseStalls(playerID, gm1.getGameName(), stall2Name, stall1Name);

        assertEquals(card4, stall1.getTopCard());
        assertEquals(card1, stall1.getBottomCard());
        assertNull(stall2.getTopCard());
        assertNull(stall2.getBottomCard());
    }


    @Test
    public void isPlayerReady() throws RegisterException, RemoteException, UserNotCreatedException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        Player player = new Player("Mos Craciun", "hohoho");
        String playerID = registrationService.register(player);

        RegisteredPlayer rplayer = registrationService.getRegisteredPlayer(playerID);
        rplayer.setUnReady();
        assertFalse(cardHandler.isPlayerReady(playerID));
        rplayer.setReady();
        assertTrue(cardHandler.isPlayerReady(playerID));

        try {
            cardHandler.isPlayerReady("123");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void switchPlayerToReady() throws RegisterException, UserNotCreatedException, RemoteException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        Player player = new Player("Mos Craciun", "hohoho");
        String playerID = registrationService.register(player);

        RegisteredPlayer rplayer = registrationService.getRegisteredPlayer(playerID);
        rplayer.setUnReady();
        assertFalse(cardHandler.isPlayerReady(playerID));
        cardHandler.switchPlayerToReady(playerID);
        assertTrue(cardHandler.isPlayerReady(playerID));

        try {
            cardHandler.switchPlayerToReady("123");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void setPlayerToUnready() throws UserNotCreatedException, RegisterException, RemoteException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        Player player = new Player("Mos Craciun", "hohoho");
        String playerID = registrationService.register(player);

        RegisteredPlayer rplayer = registrationService.getRegisteredPlayer(playerID);
        rplayer.setReady();
        assertTrue(cardHandler.isPlayerReady(playerID));
        cardHandler.setPlayerToUnready(playerID);
        assertFalse(cardHandler.isPlayerReady(playerID));

        try {
            cardHandler.setPlayerToUnready("ab");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void isEveryPlayerReady() throws CreateGameRoomException, RegisterException, UserNotCreatedException, RemoteException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();

        Player player1 = new Player("Ionut Pacate", "xoxoxo");
        Player player2 = new Player("Ma-ta", "123");
        Player player3 = new Player("Eu", "eueueu");
        GameRoom gm1 = new GameRoom("gm1", "abcdefg", 4);

        String player1ID = registrationService.register(player1);
        String player2ID = registrationService.register(player2);
        String player3ID = registrationService.register(player3);

        gm1.getPlayerSet().add(player1ID);
        gm1.getPlayerSet().add(player2ID);
        gm1.getPlayerSet().add(player3ID);
        createGameRoomService.createGameRoom(gm1);

        cardHandler.switchPlayerToReady(player1ID);
        cardHandler.switchPlayerToReady(player2ID);
        cardHandler.switchPlayerToReady(player3ID);
        assertTrue(cardHandler.isEveryPlayerReady(gm1.getGameName()));

        cardHandler.setPlayerToUnready(player2ID);
        assertFalse(cardHandler.isEveryPlayerReady(gm1.getGameName()));

        try {
            cardHandler.isEveryPlayerReady("room");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void endGame() throws RegisterException, RemoteException, CreateGameRoomException, MaxPlayersReachedException, GameRoomNotCreatedException, PlayerHasAlreadyJoinedException, PasswordIncorrectException, UserNotCreatedException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        boolean b = false;
        try{
            cardHandler.endGame("hiwd'hi");
        }catch(RuntimeException e){
            b = true;
        }
        if (b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void isDealer() throws RegisterException, RemoteException, UserNotCreatedException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        Player player = new Player("Gheorghe", "Asachi");
        String playerID = registrationService.register(player);
        RegisteredPlayer rplayer = registrationService.getRegisteredPlayer(playerID);
        rplayer.makeDealer();
        assertTrue(cardHandler.isDealer(playerID));

        try {
            cardHandler.isDealer("abc");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void hasRoundStarted() throws CreateGameRoomException, GameRoomNotCreatedException, RemoteException {
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();
        GameRoom gm1 = new GameRoom("gm1", "abcdefg", 4);

        createGameRoomService.createGameRoom(gm1);
        createGameRoomService.getGameRoom(gm1.getGameName()).startRound();
        assertTrue(cardHandler.hasRoundStarted(gm1.getGameName()));
        createGameRoomService.getGameRoom(gm1.getGameName()).finishRound();
        assertFalse(cardHandler.hasRoundStarted(gm1.getGameName()));

        try {
            cardHandler.hasRoundStarted("room");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

    }

    @Test
    public void howManyPlayersAreReady() throws RegisterException, CreateGameRoomException, RemoteException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();

        Player player1 = new Player("Ionut Pacate", "xoxoxo");
        Player player2 = new Player("Ma-ta", "123");
        Player player3 = new Player("Eu", "eueueu");
        GameRoom gm1 = new GameRoom("gm1", "abcdefg", 4);

        String player1ID = registrationService.register(player1);
        String player2ID = registrationService.register(player2);
        String player3ID = registrationService.register(player3);

        gm1.getPlayerSet().add(player1ID);
        gm1.getPlayerSet().add(player2ID);
        gm1.getPlayerSet().add(player3ID);
        createGameRoomService.createGameRoom(gm1);

        cardHandler.switchPlayerToReady(player1ID);
        cardHandler.switchPlayerToReady(player2ID);
        cardHandler.switchPlayerToReady(player3ID);
        assertEquals(3, cardHandler.howManyPlayersAreReady(gm1.getGameName()));

        cardHandler.setPlayerToUnready(player2ID);
        assertEquals(2, cardHandler.howManyPlayersAreReady(gm1.getGameName()));

        cardHandler.setPlayerToUnready(player1ID);
        cardHandler.setPlayerToUnready(player3ID);
        assertEquals(0, cardHandler.howManyPlayersAreReady(gm1.getGameName()));

        try {
            cardHandler.howManyPlayersAreReady("room");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        gm1.getPlayerSet().add("123");
        try {
            cardHandler.howManyPlayersAreReady(gm1.getGameName());
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }


    @Test
    public void startGame() throws RegisterException, CreateGameRoomException, RemoteException, GameRoomNotCreatedException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();

        Player player1 = new Player("Francezul", "sacre-bleu");
        Player player2 = new Player("Macelarul", "cunt");
        Player player3 = new Player("Laptele Mamei", "soldierboy");
        GameRoom gm1 = new GameRoom("gm1", "abcdefg", 4);

        String player1ID = registrationService.register(player1);
        String player2ID = registrationService.register(player2);
        String player3ID = registrationService.register(player3);

        gm1.getPlayerSet().add(player1ID);
        gm1.getPlayerSet().add(player2ID);
        gm1.getPlayerSet().add(player3ID);
        createGameRoomService.createGameRoom(gm1);
        cardHandler.startGame(gm1.getGameName());
        assertTrue(cardHandler.hasGameStarted("gm1"));

        assertEquals( 3, gm1.getPot().getChips());



        try {
            cardHandler.startGame("dsf");
        } catch (RuntimeException e){
            assertTrue(true);
        }

        GameRoom gm2 = new GameRoom("gm2", "abc", 2);
        gm2.getPlayerSet().add("cxv");
        createGameRoomService.createGameRoom(gm2);

        try {
            cardHandler.startGame(gm2.getGameName());
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        try {
            cardHandler.hasGameStarted("jefuwehfuh");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

    }


    @Test
    public void isMaxPlayersReached() throws CreateGameRoomException, NotAllPlayersJoinedException, RemoteException, RegisterException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();

        GameRoom gm1 = new GameRoom("gm1", "abcdefg", 4);
        createGameRoomService.createGameRoom(gm1);

        Player player1 = new Player("Francezul", "sacre-bleu");
        Player player2 = new Player("Macelarul", "cunt");
        Player player3 = new Player("Laptele Mamei", "soldierboy");
        Player player4 = new Player("Adancul", "octopus");


        String player1ID = registrationService.register(player1);
        String player2ID = registrationService.register(player2);
        String player3ID = registrationService.register(player3);
        String player4ID = registrationService.register(player4);

        gm1.getPlayerSet().add(player1ID);
        gm1.getPlayerSet().add(player2ID);
        gm1.getPlayerSet().add(player3ID);
        gm1.getPlayerSet().add(player4ID);

        assertTrue(cardHandler.isMaxPlayersReached(gm1.getGameName()));

        GameRoom gm2 = new GameRoom("gm2", "vcxxzcv", 2);
        createGameRoomService.createGameRoom(gm2);


        try {
            assertFalse(cardHandler.isMaxPlayersReached(gm2.getGameName()));
        } catch (NotAllPlayersJoinedException e) {
            assertTrue(true);
        }

        try {
            cardHandler.isMaxPlayersReached("aaa");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void setDealer() throws CreateGameRoomException, RegisterException, RemoteException, UserNotCreatedException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();

        Player player1 = new Player("Francezul", "sacre-bleu");
        Player player2 = new Player("Macelarul", "cunt");

        String player1ID = registrationService.register(player1);
        String player2ID = registrationService.register(player2);

        GameRoom gm1 = new GameRoom("gm1", "abcdefg", 4);
        createGameRoomService.createGameRoom(gm1);
        gm1.getPlayerSet().add(player1ID);
        gm1.getPlayerSet().add(player2ID);

        cardHandler.setDealer(gm1.getGameName());

        RegisteredPlayer rplayer1 = registrationService.getRegisteredPlayer(player1ID);
        assertTrue(rplayer1.isDealer());

        try {
            cardHandler.setDealer("fadsfa");
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        GameRoom gm2 = new GameRoom("gm2", "abfvv", 3);
        try {
            cardHandler.setDealer(gm2.getGameName());
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void undoDealer() throws RegisterException, CreateGameRoomException, RemoteException, UserNotCreatedException {
        RegistrationService registrationService = new RegistrationService();
        CardHandlerImpl cardHandler = new CardHandlerImpl();
        CreateGameRoomService createGameRoomService = new CreateGameRoomService();

        Player player1 = new Player("Francezul", "sacre-bleu");
        Player player2 = new Player("Macelarul", "cunt");

        String player1ID = registrationService.register(player1);
        String player2ID = registrationService.register(player2);

        GameRoom gm1 = new GameRoom("gm1", "abcdefg", 4);
        createGameRoomService.createGameRoom(gm1);
        gm1.getPlayerSet().add(player1ID);
        gm1.getPlayerSet().add(player2ID);

        RegisteredPlayer rplayer1 = registrationService.getRegisteredPlayer(player1ID);
        cardHandler.setDealer(gm1.getGameName());
        assertTrue(rplayer1.isDealer());
        cardHandler.undoDealer(player1ID);
        assertFalse(rplayer1.isDealer());


        try {
            cardHandler.undoDealer("123");
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }
}

