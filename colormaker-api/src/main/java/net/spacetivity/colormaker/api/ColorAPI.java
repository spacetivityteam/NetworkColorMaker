package net.spacetivity.colormaker.api;

import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.api.player.ColorPlayer;
import net.spacetivity.colormaker.api.player.ColorPlayerManager;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class ColorAPI {

    public static boolean isProxyUsed() {
        return ColorRepository.getInstance().isUseProxy();
    }

    public static void onEnable(boolean useProxy) {
        ColorRepository.onEnable(useProxy);
        CacheAPI.Colors.cacheColors();
    }

    public static void onDisable() {
        ColorRepository.getInstance().onDisable();
    }

    public static void saveColorToDatabase(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().insertObject(color);
    }

    public static void setDefaultPrimaryColor(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().updateField("colorName", color.getColorName(), "isPrimaryColor", !color.isPrimaryColor());
    }

    public static void setDefaultSecondaryColor(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().updateField("colorName", color.getColorName(), "isSecondaryColor", !color.isSecondaryColor());
    }

    public static Optional<NetworkColor> getDefaultPrimaryColor() {
        return CacheAPI.Colors.getCachedColors().stream().filter(NetworkColor::isPrimaryColor).findFirst();
    }

    public static Optional<NetworkColor> getDefaultSecondaryColor() {
        return CacheAPI.Colors.getCachedColors().stream().filter(NetworkColor::isSecondaryColor).findFirst();
    }

    public static Optional<ColorPlayer> createNewPlayer(UUID uniqueId) {
        Optional<NetworkColor> optionalPrimaryColor = getDefaultPrimaryColor();
        Optional<NetworkColor> optionalSecondaryColor = getDefaultSecondaryColor();

        if (optionalPrimaryColor.isEmpty() || optionalSecondaryColor.isEmpty()) return Optional.empty();

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
        return NetworkColor.from(primaryColor);
    }

    public static NetworkColor getSecondaryColor(UUID uniqueId) {
        String secondaryColor = CacheAPI.Player.getCachedPlayer(uniqueId).getSecondaryColor();
        return NetworkColor.from(secondaryColor);
    }

    public static void updatePrimaryColor(ColorPlayer colorPlayer, String primaryColor) {
        ColorPlayerManager manager = ColorRepository.getInstance().getColorPlayerManager();
        colorPlayer.setPrimaryColor(primaryColor);
        manager.updateField("uniqueId", colorPlayer.getUniqueId().toString(), "primaryColor", colorPlayer.getPrimaryColor());
        CacheAPI.Player.update(colorPlayer);
    }

    public static void updateSecondaryColor(ColorPlayer colorPlayer, String secondaryColor) {
        ColorPlayerManager manager = ColorRepository.getInstance().getColorPlayerManager();
        colorPlayer.setSecondaryColor(secondaryColor);
        manager.updateField("uniqueId", colorPlayer.getUniqueId().toString(), "secondaryColor", colorPlayer.getPrimaryColor());
        CacheAPI.Player.update(colorPlayer);
    }
}
