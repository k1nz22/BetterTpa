package com.k1nz.bettertpa.Commands;

import com.k1nz.bettertpa.Manager.TpaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpadeny implements CommandExecutor {
    private final TpaManager tpaManager;

    public Tpadeny(TpaManager tpaManager) {
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;
        tpaManager.denyTpaRequest(player);
        return true;
    }
}
