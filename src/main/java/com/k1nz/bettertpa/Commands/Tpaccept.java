package com.k1nz.bettertpa.Commands;

import com.k1nz.bettertpa.Manager.TpaManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpaccept implements CommandExecutor {
    private final TpaManager tpaManager;

    public Tpaccept(TpaManager tpaManager) {
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;
        tpaManager.acceptTpaRequest(player);
        return true;
    }
}
