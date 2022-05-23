package net.spacetivity.colormaker.api;

import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.api.player.ColorPlayer;

import java.util.Set;
import java.util.UUID;

public class CacheAPI {

    public static class Colors {
        public static Set<NetworkColor> getCachedColors() {
            return ColorRepository.getInstance().getCachedColors();
        }

        public static void cacheColors() {
            ColorRepository.getInstance().getNetworkColorManager().getAllAsync().thenAccept(colors -> getCachedColors().addAll(colors));
        }

        public static void updateCachedColorsAsync() {
            ColorRepository.getInstance().getNetworkColorManager().getAllAsync().thenAccept(colors -> {
                getCachedColors().clear();
                getCachedColors().addAll(colors);
            });
        }
    }

    public static class Player {
        public static Set<ColorPlayer> getCachedPlayers() {
            return ColorRepository.getInstance().getCachedPlayers();
        }

        public static boolean isCached(UUID uniqueId) {
            return getCachedPlayers().stream().anyMatch(colorPlayer -> colorPlayer.getUniqueId().equals(uniqueId));
        }

        public static void putInCache(ColorPlayer player) {
            if (!isCached(player.getUniqueId())) getCachedPlayers().add(player);
        }

        public static void deleteFromCache(UUID uniqueId){
            getCachedPlayers().stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().ifPresent(player -> getCachedPlayers().remove(player));
        }

        public static void update(ColorPlayer player) {
            deleteFromCache(player.getUniqueId());
            putInCache(player);
        }

        public static ColorPlayer getCachedPlayer(UUID uniqueId) {
            return getCachedPlayers().stream().filter(player -> player.getUniqueId().equals(uniqueId))
                    .findFirst().orElse(null);
        }
    }
}
