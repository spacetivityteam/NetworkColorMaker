package net.spacetivity.colormaker.api;

import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.api.player.ColorPlayer;
import net.spacetivity.colormaker.api.player.ColorPlayerManager;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ColorAPI {

    public static boolean isProxyUsed() {
        return ColorRepository.getInstance().isUseProxy();
    }

    public static void onEnable(boolean useProxy) {
        ColorRepository.onEnable(useProxy);
        CacheAPI.Color.cacheColors();
    }

    public static void onDisable() {
        ColorRepository.getInstance().onDisable();
    }

    public static void saveColorToDatabase(NetworkColor color, boolean updateCache) {
        ColorRepository.getInstance().getNetworkColorManager().insertObject(color);
        if (updateCache) CacheAPI.Color.updateCachedColorsAsync();
    }

    public static void setDefaultPrimaryColor(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().updateField("colorName", color.getColorName(),
                "defaultPrimaryColor", color.isDefaultPrimaryColor());
    }

    public static void setDefaultSecondaryColor(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().updateField("colorName", color.getColorName(),
                "defaultSecondaryColor", color.isDefaultSecondaryColor());
    }

    public static Optional<NetworkColor> getColor(String colorName) {
        return CacheAPI.Color.getCachedColors().stream().filter(color -> color.getColorName().equalsIgnoreCase(colorName)).findFirst();
    }

    public static Optional<NetworkColor> getDefaultPrimaryColor() {
        return CacheAPI.Color.getColor(NetworkColor::isDefaultPrimaryColor).stream().findFirst();
    }

    public static Optional<NetworkColor> getDefaultSecondaryColor() {
        return CacheAPI.Color.getColor(NetworkColor::isDefaultSecondaryColor).stream().findFirst();
    }

    public static Optional<ColorPlayer> createNewPlayer(UUID uniqueId) {
        Optional<NetworkColor> optionalPrimaryColor = getDefaultPrimaryColor();
        Optional<NetworkColor> optionalSecondaryColor = getDefaultSecondaryColor();

        if (optionalPrimaryColor.isEmpty() || optionalSecondaryColor.isEmpty()) {
            Logger.getGlobal().log(Level.WARNING, "Player could not be created, because there was no primary and / or secondary color was found.");
            return Optional.empty();
        }

        String defaultPrimaryColor = optionalPrimaryColor.get().getColorName();
        String defaultSecondaryColor = optionalSecondaryColor.get().getColorName();
        ColorPlayer colorPlayer = ColorPlayer.from(uniqueId, defaultPrimaryColor, defaultSecondaryColor);
        ColorRepository.getInstance().getColorPlayerManager().insertObject(colorPlayer);
        return Optional.of(colorPlayer);
    }

    public static void getPlayerAsync(UUID uniqueId, Consumer<ColorPlayer> result) {
        ColorRepository.getInstance().getColorPlayerManager().getAsync(uniqueId.toString()).thenAccept(result);
    }

    public static NetworkColor getPrimaryColor(UUID uniqueId) {
        String primaryColor = CacheAPI.Player.getCachedPlayer(uniqueId).getPrimaryColor();
        return NetworkColor.fromCachedColor(primaryColor);
    }

    public static NetworkColor getSecondaryColor(UUID uniqueId) {
        String secondaryColor = CacheAPI.Player.getCachedPlayer(uniqueId).getSecondaryColor();
        return NetworkColor.fromCachedColor(secondaryColor);
    }

    public static void updatePrimaryColor(ColorPlayer colorPlayer, String primaryColor, boolean updateCache) {
        ColorPlayerManager manager = ColorRepository.getInstance().getColorPlayerManager();
        colorPlayer.setPrimaryColor(primaryColor);
        manager.updateField("uniqueId", colorPlayer.getUniqueId().toString(), "primaryColor", colorPlayer.getPrimaryColor());
        if (updateCache) CacheAPI.Player.update(colorPlayer);
    }

    public static void updateSecondaryColor(ColorPlayer colorPlayer, String secondaryColor, boolean updateCache) {
        ColorPlayerManager manager = ColorRepository.getInstance().getColorPlayerManager();
        colorPlayer.setSecondaryColor(secondaryColor);
        manager.updateField("uniqueId", colorPlayer.getUniqueId().toString(), "secondaryColor", colorPlayer.getSecondaryColor());
        if (updateCache) CacheAPI.Player.update(colorPlayer);
    }
}
