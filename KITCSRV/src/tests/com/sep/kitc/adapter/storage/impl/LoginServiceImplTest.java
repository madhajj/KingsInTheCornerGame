package com.sep.kitc.adapter.storage.impl;

import com.sep.kitc.adapter.storage.entity.Player;
import com.sep.kitc.adapter.storage.entity.RegisteredPlayer;
import com.sep.kitc.adapter.storage.service.RegistrationService;
import com.sep.kitc.common.exception.PasswordIncorrectException;
import com.sep.kitc.common.exception.PlayerHasAlreadyLoggedInException;
import com.sep.kitc.common.exception.RegisterException;
import com.sep.kitc.common.exception.UserNotCreatedException;
import org.junit.Test;

import javax.security.auth.login.LoginException;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class LoginServiceImplTest {

    @Test
    public void login() throws RegisterException, UserNotCreatedException, PasswordIncorrectException, RemoteException, PlayerHasAlreadyLoggedInException {
        LoginServiceImpl loginService = new LoginServiceImpl();
        RegistrationService registrationService = new RegistrationService();
        Player player = new Player("Vulva", "123");

        String playerID = registrationService.register(player);
        RegisteredPlayer rplayer = registrationService.getRegisteredPlayer(playerID);
        assertNotEquals("10231", loginService.login("Vulva", "123"));
        assertEquals(registrationService.getUserIdentifier(rplayer).get(), loginService.login("Vulva", "123"));

        Player player2 = new Player("Bulbucula", "whereFingers");
        String player2ID = registrationService.register(player2);
        RegisteredPlayer rplayer2 = registrationService.getRegisteredPlayer(player2ID);

        try {
            loginService.login("Bulbucula", "123");
        } catch (PasswordIncorrectException e) {
            assertTrue(true);
        }

        Player player3 = new Player("Vanabol", "hahaha");

        try {
            loginService.login("Vanabol", "hahaha");
        } catch (UserNotCreatedException e) {
            assertTrue(true);
        }


    }

    @Test
    public void register() throws RegisterException, RemoteException, UserNotCreatedException, PasswordIncorrectException {
        LoginServiceImpl loginService = new LoginServiceImpl();
        RegistrationService registrationService = new RegistrationService();
        String playerID = loginService.register("Porcodon", "tradator");
        assertTrue(registrationService.isRegistered(new Player ("Porcodon", "tradator")));
    }

    /*
    @Test
    public void unregister() throws RemoteException, RegisterException, UserNotCreatedException, PasswordIncorrectException {
        LoginServiceImpl loginService = new LoginServiceImpl();
        RegistrationService registrationService = new RegistrationService();
        String playerID = loginService.register("Porcodon", "tradator");
        assertTrue(registrationService.isRegistered(new Player ("Porcodon", "tradator")));

        loginService.unregister("Porcodon", "tradator", playerID);
        assertFalse(registrationService.isRegistered(new Player("Porcodon", "tradator")));
    }

     */
}