package com.draxy.orbs.events;

import com.draxy.orbs.DOrbs;
import com.draxy.orbs.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        DOrbs.getInstance().connection.insertPlayer(e.getPlayer());
        if(PlayerManager.getPlayerManagerByUUID.containsKey(e.getPlayer().getUniqueId())) return;
        PlayerManager.getPlayerManagerByUUID.put(e.getPlayer().getUniqueId(), new PlayerManager(e.getPlayer()));
    }

}
