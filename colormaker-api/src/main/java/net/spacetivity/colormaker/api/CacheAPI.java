package net.spacetivity.colormaker.api;

import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.api.event.SpigotColorUpdateEvent;
import net.spacetivity.colormaker.api.messaging.CachedColorsUpdatePacket;
import net.spacetivity.colormaker.api.messaging.PlayerColorUpdatePacket;
import net.spacetivity.colormaker.api.player.ColorPlayer;
import net.spacetivity.colormaker.database.redis.RedisPacket;
import org.bukkit.Bukkit;
import org.redisson.api.RTopicRx;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CacheAPI {

    public static class Color {
        public static List<NetworkColor> getCachedColors() {
            return ColorRepository.getInstance().getCachedColors();
        }

        public static Optional<NetworkColor> getColor(Predicate<? super NetworkColor> filter) {
            return getCachedColors().stream().filter(filter).findFirst();
        }

        public static boolean isExisting(String colorName) {
            return getCachedColors().stream().anyMatch(color -> color.getColorName().equalsIgnoreCase(colorName));
        }

        public static void cacheColors() {
            ColorRepository.getInstance().getNetworkColorManager().getAllAsync().thenAccept(colors -> getCachedColors().addAll(colors));
        }

        public static void updateCachedColorsAsync() {
            ColorRepository.getInstance().getNetworkColorManager().getAllAsync().thenAccept(colors -> {
                getCachedColors().clear();
                getCachedColors().addAll(colors);
                if (ColorAPI.isProxyUsed()) Packet.sendPacket(new CachedColorsUpdatePacket());
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

        public static void deleteFromCache(UUID uniqueId) {
            getCachedPlayers().stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().ifPresent(player -> getCachedPlayers().remove(player));
        }

        public static void update(ColorPlayer player) {
            deleteFromCache(player.getUniqueId());
            putInCache(player);
            if (ColorAPI.isProxyUsed()) Packet.sendPacket(new PlayerColorUpdatePacket(player.getUniqueId()));
            else Bukkit.getPluginManager().callEvent(new SpigotColorUpdateEvent(Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())), player));
        }

        public static ColorPlayer getCachedPlayer(UUID uniqueId) {
            return getCachedPlayers().stream().filter(player -> player.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
        }
    }

    public static class Packet {
        public static Optional<RTopicRx> getUpdateChannel() {
            return Optional.ofNullable(ColorRepository.getInstance().getUpdateChannel());
        }

        public static void sendPacket(RedisPacket packet) {
            getUpdateChannel().ifPresent(packet::send);
        }

        public static void listenForPlayerUpdates(Consumer<PlayerColorUpdatePacket> result) {
            getUpdateChannel().ifPresent(channel -> channel.addListener(PlayerColorUpdatePacket.class, (channel1, packet) -> result.accept(packet)).subscribe());
        }

        public static void listenForColorUpdates(Consumer<CachedColorsUpdatePacket> result) {
            getUpdateChannel().ifPresent(channel -> channel.addListener(CachedColorsUpdatePacket.class, (channel1, packet) -> result.accept(packet)).subscribe());
        }
    }
}
