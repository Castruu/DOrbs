package com.draxy.orbs.events;

import com.draxy.orbs.DOrbs;
import com.draxy.orbs.config.ConfigHandler;
import com.draxy.orbs.manager.PlayerManager;
import com.draxy.orbs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class TradeOrbsEvent implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(!e.getInventory().getTitle().contains(Utils.color("&6Orbs ->"))) return;
        if(e.getCurrentItem() == null) return;
        e.setCancelled(true);
        System.out.println(e.getAction());
        if(e.getSlot() != 13) return;
        Player player = (Player) e.getWhoClicked();
        if(PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).getOrbAmount() <= 0) {
            player.sendMessage(Utils.color("&cNot enough orbs!"));
        } else {
            switch (e.getAction()) {
                case PICKUP_ALL:
                    player.sendMessage(Utils.color("&bYou traded one orb for &a" + ConfigHandler.getInstance().getTP() + "&b TPs!"));
                    DOrbs.getInstance().connection.removeOrb(player, 1);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "jrmctp " + ConfigHandler.getInstance().getTP() + " " + player.getName());
                    player.openInventory(PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).getInventory());
                    break;
                case PICKUP_HALF:
                    int amount = PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).getOrbAmount();
                    int tpAmount = amount * ConfigHandler.getInstance().getTP();
                    player.sendMessage(Utils.color("&bYou traded all your orbs for &a" +tpAmount + "&b TPs!"));
                    DOrbs.getInstance().connection.removeOrb(player, amount);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "jrmctp " +tpAmount + " " + player.getName());
                    player.closeInventory();
                    break;
            }
        }
    }
}
