package net.spacetivity.colormaker.plugin.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.player.ColorPlayer;

import java.util.Optional;

public class PlayerListener implements Listener {

    @EventHandler(priority = 1)
    public void onConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();

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
    public void onDisconnect(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        CacheAPI.Player.deleteFromCache(player.getUniqueId());
    }
}
