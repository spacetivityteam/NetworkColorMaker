package net.spacetivity.colormaker.plugin.spigot;

import net.kyori.adventure.text.Component;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.api.player.ColorPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Optional;

public class PlayerListener implements Listener {

    public PlayerListener(SpigotInitializer initializer) {
        initializer.getServer().getPluginManager().registerEvents(this, initializer);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ColorAPI.getPlayerAsync(player.getUniqueId(), colorPlayer -> {
            if (colorPlayer == null) {
                Optional<ColorPlayer> newColorPlayer = ColorAPI.createNewPlayer(player.getUniqueId());
                newColorPlayer.ifPresent(ColorPlayer::putInCache);
                return;
            }

            colorPlayer.putInCache();
        });
    }

    @EventHandler
    public void onText(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isSneaking()) {

            NetworkColor primaryColor = ColorAPI.getPrimaryColor(player.getUniqueId());
            NetworkColor secondaryColor = ColorAPI.getSecondaryColor(player.getUniqueId());

            player.sendMessage(primaryColor.toSpigot() + player.getName() + " §fhas the level: " + secondaryColor.toSpigot() + player.getLevel() + " §f(For: Spigot & Bungee)");

            player.sendMessage("-----------------------");

            player.sendMessage(Component.text()
                    .append(Component.text(player.getName()).color(primaryColor.toPaper()))
                    .append(Component.text(" has the level: "))
                    .append(Component.text(player.getLevel()).color(secondaryColor.toPaper()))
                    .append(Component.text(" (For: Paper & Velocity)")));


        }
    }

    public void test(Player player) {
        NetworkColor primaryColor = ColorAPI.getPrimaryColor(player.getUniqueId());
        NetworkColor secondaryColor = ColorAPI.getSecondaryColor(player.getUniqueId());

        player.sendMessage(Component.text()
                .append(Component.text(player.getName()).color(primaryColor.toPaper()))
                .append(Component.text(" has the level: "))
                .append(Component.text(player.getLevel()).color(secondaryColor.toPaper()))
                .append(Component.text(" (For: Paper & Velocity)")));
    }
}
