package com.draxy.orbs.commands;

import com.draxy.orbs.DOrbs;
import com.draxy.orbs.config.ConfigHandler;
import com.draxy.orbs.manager.PlayerManager;
import com.draxy.orbs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrbCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(!(sender instanceof Player)) return false;
            Player player = (Player) sender;
            player.openInventory(PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).getInventory());
            return true;
        }
        else if(args.length == 3) {
            Player target = null;
            int amount = 0;
            try {
                target = Bukkit.getPlayer(args[1]);
                amount = Integer.parseInt(args[2]);
                if(target == null) throw new NullPointerException();
                if(amount <= 0) throw new IllegalArgumentException();
            } catch (NullPointerException e) {
                sender.sendMessage(Utils.color("&cThis player is not online!"));
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage(Utils.color("&cThe third argument must be a number!"));
                return true;
            } catch (IllegalArgumentException e) {
                sender.sendMessage(Utils.color("&cInsert a positive number!"));
                return true;
            }
            if(args[0].equalsIgnoreCase("pay")) {
                if(!(sender instanceof Player)) return false;
                if(!ConfigHandler.getInstance().isTradeSystemOn()) {
                    sender.sendMessage(Utils.color("&cThe trade system were disabled by the Administrators!"));
                    return true;
                }
                Player player = (Player) sender;
                if (target.equals(player)) {
                    player.sendMessage(Utils.color("&cYou can't pay yourself!"));
                    return true;
                }
                if(PlayerManager.getPlayerManagerByUUID.get(player.getUniqueId()).getOrbAmount() - amount < 0) {
                    sender.sendMessage(Utils.color("&cYou don't have orbs for that!"));
                    return true;
                }
                target.sendMessage(Utils.color("&aYou received " + amount + " orbs from &b" + player.getName() + "!"));
                player.sendMessage(Utils.color("&aYou gave " + amount + " orbs to &b" + target.getName() + "!"));
                DOrbs.getInstance().connection.removeOrb(player, amount);
                DOrbs.getInstance().connection.addOrb(target, amount);
                return true;
            }
            if(!sender.hasPermission("orbs.admin")) {
                sender.sendMessage(Utils.color("&cYou do not have permission to it!"));
                return true;
            }
            switch (args[0]) {
                case "add":
                    DOrbs.getInstance().connection.addOrb(target, amount);
                    target.sendMessage(Utils.color("&aYou received " + amount + " orbs!"));
                    break;
                case "remove":
                    if(PlayerManager.getPlayerManagerByUUID.get(target.getUniqueId()).getOrbAmount() - amount < 0) {
                        sender.sendMessage(Utils.color("&c" + target.getName() + " doesn't have orbs for that!"));
                        return true;
                    }
                    DOrbs.getInstance().connection.removeOrb(target, amount);
                    target.sendMessage(Utils.color("&c" + amount + " orbs were taken from you!"));
                    break;
            }
            sender.sendMessage(Utils.color("&b" + target.getName() +  "'s orbs were changed sucesfully!"));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("orbs.admin")) {
                sender.sendMessage(Utils.color("&cYou do not have permission to it!"));
                return true;
            }
            DOrbs.getInstance().reloadConfig();
            ConfigHandler.getInstance().setValues();
            sender.sendMessage(Utils.color("&bCommand reloaded"));
        } else {
            sender.sendMessage(Utils.color("&cWrong usage!"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        String[] options;
        List<String> tab = new ArrayList<>();
        if(strings.length == 1) {
            if(commandSender.hasPermission("orbs.admin")) {
                if(ConfigHandler.getInstance().isTradeSystemOn())
                    options = new String[]{"pay", "add", "remove", "reload"};
                else {
                    options = new String[]{"add", "remove", "reload"};
                }
            } else {
                if(ConfigHandler.getInstance().isTradeSystemOn())
                    options = new String[]{"pay"};
                else return tab;
            }
            if(strings[0].equalsIgnoreCase("")) {
                tab.addAll(Arrays.asList(options));
            } else {
                for(String tabs : options) {
                    if(tabs.startsWith(strings[0])) {
                        tab.add(tabs);
                    }
                }
            }
            return tab;
        }
        return null;
    }
}
