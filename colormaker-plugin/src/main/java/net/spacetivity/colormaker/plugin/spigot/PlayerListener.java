package net.spacetivity.colormaker.plugin.spigot;

import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.player.ColorPlayer;
import net.spacetivity.colormaker.plugin.spigot.inventory.ColorInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

        Bukkit.getScheduler().runTaskLater(SpigotInitializer.getPlugin(SpigotInitializer.class), () -> {
            ColorInventory.getInventory(player).open(player);
        }, 10);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CacheAPI.Player.deleteFromCache(player.getUniqueId());
    }
}
