package net.spacetivity.colormaker.plugin.velocity;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.player.ColorPlayer;

import java.util.Optional;

public class PlayerListener {

    @Subscribe(order = PostOrder.FIRST)
    public void onConnected(ServerConnectedEvent event) {
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

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        CacheAPI.Player.deleteFromCache(player.getUniqueId());
    }
}
