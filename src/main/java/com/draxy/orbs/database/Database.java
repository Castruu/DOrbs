package com.draxy.orbs.database;

import org.bukkit.entity.Player;

public abstract class Database {

    public abstract void startConnection();

    public abstract void insertPlayer(Player player);

    public abstract void addOrb(Player player, int newOrbs);

    public abstract void removeOrb(Player player, int takedOrbs);

    public abstract int getOrbs(Player player);

    public abstract void close();

}
