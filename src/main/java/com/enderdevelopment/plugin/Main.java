package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("PlayerInfoPlugin has been enabled!");
        Bukkit.getPluginManager().registerEvents(this, this);
        updateScoreboard();
    }

    @Override
    public void onDisable() {
        getLogger().info("PlayerInfoPlugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("playerinfo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int onlinePlayers = Bukkit.getOnlinePlayers().size();
                Component message = Component.text("Online Players: ").color(NamedTextColor.GOLD)
                        .append(Component.text(onlinePlayers).color(NamedTextColor.WHITE));
                player.sendMessage(message);
                return true;
            } else {
                sender.sendMessage("This command can only be used by players.");
                return false;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        Component message = Component.text(player.getName() + " is sleeping at " + event.getBed().getLocation().toString()).color(NamedTextColor.GOLD);
        Bukkit.broadcast(message);
    }

    private void updateScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("onlinePlayers", "dummy", Component.text("Online Players").color(NamedTextColor.GOLD));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            int onlinePlayers = Bukkit.getOnlinePlayers().size();
            objective.getScore("Players").setScore(onlinePlayers);
        }, 0L, 20L);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
    }
}