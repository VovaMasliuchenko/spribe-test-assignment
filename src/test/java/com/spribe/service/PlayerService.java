package com.spribe.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    private static final Logger LOGGER = LogManager.getLogger(PlayerService.class);

    public static ThreadLocal<List<String>> playerIdsToRemove = ThreadLocal.withInitial(ArrayList::new);

    public static void addPlayerIdToRemoveList(String id) {
        LOGGER.info("Adding player {} id's to remove list...", id);
        playerIdsToRemove.get().add(id);
    }

    public static List<String> getPlayerIdsToRemove() {
        LOGGER.info("Getting player id's to remove list...");
        return playerIdsToRemove.get();
    }

    public static void clearList() {
        LOGGER.info("Clearing player id's to remove list...");
        playerIdsToRemove.get().clear();
    }
}
