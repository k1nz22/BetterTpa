package com.k1nz.bettertpa;

import com.k1nz.bettertpa.Commands.Tpa;
import com.k1nz.bettertpa.Commands.Tpaccept;
import com.k1nz.bettertpa.Commands.Tpadeny;
import com.k1nz.bettertpa.Manager.TpaManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterTpa extends JavaPlugin {
    private TpaManager tpaManager;

    @Override
    public void onEnable() {
        getLogger().info("Plugin BetterTpa został uruchomiony.");
        tpaManager = new TpaManager();
        getCommand("tpa").setExecutor(new Tpa(tpaManager));
        getCommand("tpaccept").setExecutor(new Tpaccept(tpaManager));
        getCommand("tpadeny").setExecutor(new Tpadeny(tpaManager));
    }

    @Override
    public void onDisable() {
    getLogger().info("Plugin BetterTpa został wyłączony.");
    }
}
