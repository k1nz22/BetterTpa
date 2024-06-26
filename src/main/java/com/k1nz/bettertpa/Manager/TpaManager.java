package com.k1nz.bettertpa.Manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class TpaManager {
    private final Map<Player, Player> tpaRequests = new HashMap<>();
    private final Map<Player, Integer> tpaTimeouts = new HashMap<>();
    private final Map<Player, Integer> tpaHealthTasks = new HashMap<>();
    private final Map<Player, Double> initialHealth = new HashMap<>();

    public void sendTpaRequest(Player sender, Player target) {
        tpaRequests.put(target, sender);

        target.sendMessage(sender.getName() + " §7wysłał prośbę o teleportację do Ciebie. Wpisz §a/tpaccept §7aby zaakceptować lub §c/tpadeny§7 aby odrzucić.");
        sender.sendMessage("§7Prośba o teleportację wysłana do " + target.getName());

        int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("BetterTpa"), () -> {
            if (tpaRequests.containsKey(target) && tpaRequests.get(target).equals(sender)) {
                cancelTpaRequest(target);
                sender.sendMessage("§cTwoja prośba o teleportację do " + target.getName() + " §cwygasła.");
            }
        }, 2400L);

        tpaTimeouts.put(target, taskId);
    }

    public void acceptTpaRequest(Player target) {
        Player sender = tpaRequests.remove(target);
        Integer taskId = tpaTimeouts.remove(target);

        if (sender != null) {
            if (taskId != null) {
                Bukkit.getScheduler().cancelTask(taskId);
            }

            sendActionBarCountdown(sender, 5);

            sendTitleMessage(sender, "§a§cTELEPORTACJA ZAAKCEPTOWANA!", "§7Za chwilę zostaniesz przeteleportowany");

            sender.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 110, 0));
            sender.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 110, 10));
            sender.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 110, 255));

            // Pobierz zdrowie gracza przy rozpoczęciu teleportacji
            double startingHealth = sender.getHealth();
            initialHealth.put(sender, startingHealth);

            // Sprawdź zdrowie gracza co sekundę
            int healthTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("BetterTpa"), () -> {
                double currentHealth = sender.getHealth();
                double initial = initialHealth.getOrDefault(sender, startingHealth);
                if (currentHealth != initial) {
                    cancelTeleport(sender, target);
                }
            }, 20L, 20L);

            tpaHealthTasks.put(sender, healthTaskId);

            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("BetterTpa"), () -> {
                // Anulowanie zadania monitorowania zdrowia po teleportacji
                Integer healthTaskIdAfterTeleport = tpaHealthTasks.remove(sender);
                if (healthTaskIdAfterTeleport != null) {
                    Bukkit.getScheduler().cancelTask(healthTaskIdAfterTeleport);
                }

                sender.teleport(target);
                sender.sendMessage("§aProśba o teleportację zaakceptowana przez " + target.getName());
                target.sendMessage("§aZaakceptowałeś prośbę o teleportację od " + sender.getName());
            }, 95L);
        } else {
            target.sendMessage("§cNie znaleziono prośby o teleportację.");
        }
    }

    public void denyTpaRequest(Player target) {
        Player sender = tpaRequests.remove(target);
        Integer taskId = tpaTimeouts.remove(target);
        Integer healthTaskId = tpaHealthTasks.remove(sender);

        if (sender != null) {
            if (taskId != null) {
                Bukkit.getScheduler().cancelTask(taskId);
            }
            if (healthTaskId != null) {
                Bukkit.getScheduler().cancelTask(healthTaskId);
            }

            sender.sendMessage("§cProśba o teleportację odrzucona przez " + target.getName());
            target.sendMessage("§7Odrzuciłeś prośbę o teleportację od " + sender.getName());
        } else {
            target.sendMessage("§cNie znaleziono prośby o teleportację.");

        }
    }

    private void sendActionBarCountdown(Player player, int seconds) {
        final int[] taskId = new int[1];

        taskId[0] = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("BetterTpa"), new Runnable() {
            int countdown = seconds;

            @Override
            public void run() {
                if (countdown > 0) {
                    player.sendActionBar("§aTeleportacja za: " + countdown + "s");
                    countdown--;
                } else {
                    Bukkit.getScheduler().cancelTask(taskId[0]);
                }
            }
        }, 0L, 20L);
    }

    private void sendTitleMessage(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 70, 20);
    }

    private void cancelTpaRequest(Player target) {
        tpaRequests.remove(target);
        Integer taskId = tpaTimeouts.remove(target);

        if (taskId != null) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
    }

    private void cancelTeleport(Player sender, Player target) {
        sender.sendMessage("§cTeleportacja anulowana, ponieważ Twoje zdrowie zmieniło się.");
        sender.sendTitle("§c§lTELEPORTACJA ANULOWANA!", "§cPoruszyłeś Się!");

        Integer healthTaskId = tpaHealthTasks.remove(sender);
        if (healthTaskId != null) {
            Bukkit.getScheduler().cancelTask(healthTaskId);
        }
    }
}
