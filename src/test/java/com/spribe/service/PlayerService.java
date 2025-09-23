package com.spribe.service;

import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    public static ThreadLocal<List<String>> playerIdsToRemove = ThreadLocal.withInitial(ArrayList::new);

    public static void addPlayerIdToRemoveList(String id) {
        playerIdsToRemove.get().add(id);
    }

    public static List<String> getPlayerIdsToRemove() {
        return playerIdsToRemove.get();
    }

    public static void clearList() {
        playerIdsToRemove.get().clear();
    }
}
