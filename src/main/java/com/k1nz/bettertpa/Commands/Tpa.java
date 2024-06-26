package com.k1nz.bettertpa.Commands;

import com.k1nz.bettertpa.Manager.TpaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tpa implements CommandExecutor {
    private final TpaManager tpaManager;

    public Tpa(TpaManager tpaManager) {
        this.tpaManager = tpaManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Komenda tylko dla graczy!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage("§cUżycie: /tpa <player>");
            return true;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage("§cGracz nie znaleziony!");
            return true;
        }

        tpaManager.sendTpaRequest(player, target);
        return true;
    }
}
