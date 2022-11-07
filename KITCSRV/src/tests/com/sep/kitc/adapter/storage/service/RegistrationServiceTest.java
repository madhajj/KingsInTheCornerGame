package com.sep.kitc.adapter.storage.service;

import com.sep.kitc.adapter.storage.entity.Player;
import com.sep.kitc.adapter.storage.entity.RegisteredPlayer;
import com.sep.kitc.common.exception.PasswordIncorrectException;
import com.sep.kitc.common.exception.RegisterException;
import com.sep.kitc.common.exception.UserNotCreatedException;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class RegistrationServiceTest {

    @Test
    public void checkIfPasswordCorrect() throws RegisterException {
        RegistrationService registrationService = new RegistrationService();
        Player player = new Player("Marius", "Stratan");
        registrationService.register(player);
        assertTrue(registrationService.checkIfPasswordCorrect(player));
        assertFalse(registrationService.checkIfPasswordCorrect(new Player("Marius", "1234")));
    }

    /*
    @Test

    public void isRegistered() throws RegisterException, UserNotCreatedException, PasswordIncorrectException {
        RegistrationService registrationService = new RegistrationService();
        Player player1 = new Player("Marius", "Stratan");
        registrationService.register(player1);
        boolean b1 = false;
        try{
            registrationService.register(player1);
        }catch(RegisterException e){
            b1 = true;
        }

        if(b1) assertTrue(true);
        else assertTrue(false);

        Player player2 = new Player("vladRobot", "poot5");
        registrationService.register(player2);

        assertTrue(registrationService.isRegistered(player1));
        Player player3 = new Player("valeriu", "munteanu70");
        registrationService.register(player3);

        assertTrue(registrationService.isRegistered(player3));
        assertTrue(registrationService.isRegistered(player2));

        assertFalse(registrationService.isRegistered(new Player("Anatol", "vanabol")));

        boolean b2 = false;
        try{
            registrationService.unregister(new Player("Marius", "Ganea"));
        }catch(PasswordIncorrectException e){b2 = true;}
        if(b2) assertTrue(true);
        else assertTrue(false);

        registrationService.unregister(player1);
        assertFalse(registrationService.isRegistered(player1));

        boolean b3 = false;
        try{
            registrationService.unregister(player1);
        }catch(UserNotCreatedException e){
            b3 = true;
        }
        if(b3) assertTrue(true);
        else assertTrue(false);

        registrationService.unregister(player2);

        assertFalse(registrationService.isRegistered(player2));
        registrationService.unregister(player3);
        assertFalse(registrationService.isRegistered(player3));
    }

     */


    @Test
    public void getUserIdentifier() throws RegisterException {
        RegistrationService registrationService = new RegistrationService();
        Player player = new Player("Marius", "Stratan");
        String playerID = registrationService.register(player);
        String s = registrationService.getUserIdentifier(player).get();
        assertEquals(s, playerID);
    }

    @Test
    public void getUserNameOfPlayer() throws RegisterException, UserNotCreatedException {
        RegistrationService registrationService = new RegistrationService();
        Player player1 = new Player("Marius", "Stratan");
        String id1 = registrationService.register(player1);

        Player player2 = new Player("vladRobot", "poot5");
        String id2 = registrationService.register(player2);

        assertTrue(registrationService.isRegistered(player1));
        Player player3 = new Player("valeriu", "munteanu70");
        String id3 = registrationService.register(player3);

        assertEquals(registrationService.getUserNameOfPlayer(id1), "Marius");
        assertEquals(registrationService.getUserNameOfPlayer(id2), "vladRobot");
        assertEquals(registrationService.getUserNameOfPlayer(id3), "valeriu");

        boolean b = false;
        try{
            registrationService.getUserNameOfPlayer("ufeiwfeu");
        }catch (UserNotCreatedException e){
            b = true;
        }
        if(b) assertTrue(true);
        else assertTrue(false);
    }

    @Test
    public void getRegisteredPlayer() throws RegisterException, UserNotCreatedException {
        RegistrationService registrationService = new RegistrationService();
        Player player1 = new Player("Marius", "Stratan");
        String id1 = registrationService.register(player1);
        assertEquals(registrationService.getRegisteredPlayer(id1).getUserIdent(), id1);

        boolean b = false;
        try{
            registrationService.getRegisteredPlayer("ufeiwfeu");
        }catch (UserNotCreatedException e){
            b = true;
        }
        if(b) assertTrue(true);
        else assertTrue(false);
    }
}