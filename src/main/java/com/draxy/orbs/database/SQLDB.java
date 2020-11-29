package com.draxy.orbs.database;

import com.draxy.orbs.DOrbs;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class SQLDB extends Database {

    @Override
    public abstract void startConnection();

    @Override
    public abstract void insertPlayer(Player player);

    @Override
    public abstract void addOrb(Player player, int newOrbs);

    @Override
    public abstract void removeOrb(Player player, int takedOrbs);

    @Override
    public abstract int getOrbs(Player player);

    @Override
    public abstract void close();

    public void createTable(Connection con){
        try {
            PreparedStatement stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS player (uuid CHAR(36), orbs INT)");
            System.out.println("Tabela player carregadas com sucesso!");
            stm.execute();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Falha ao criar a tabela de players!");
            DOrbs.getInstance().getPluginLoader().disablePlugin(DOrbs.getInstance());
        }
    }
}
