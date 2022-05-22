package net.spacetivity.colormaker.api;

import net.spacetivity.colormaker.api.color.NetworkColor;

import java.util.UUID;

public class ColorAPI {

    public static void onEnable(boolean useProxy) {
        ColorRepository.getInstance().onEnable(useProxy);
    }

    public static void onDisable() {
        ColorRepository.getInstance().onDisable();
    }

    public static void saveColorToDatabase(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().insertObject(color);
    }

    public static void updateCachedColors(UUID uniqueId) {

    }

    public static void cachePrimaryColor(UUID uniqueId) {

    }

    public static void cacheSecondaryColor(UUID uniqueId) {

    }

    public static NetworkColor getPrimaryColor(UUID uniqueId) {
        return null;
    }

    public static NetworkColor getSecondaryColor(UUID uniqueId) {
        return null;
    }

    public static void updatePrimaryColor(UUID uniqueId, NetworkColor newColor) {

    }

    public static void updateSecondaryColor(UUID uniqueId, NetworkColor newColor) {

    }

    public static void setDefaultPrimaryColor(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().updateField("colorName", color.getColorName(), "isPrimaryColor", !color.isPrimaryColor());
    }

    public static void setDefaultSecondaryColor(NetworkColor color) {
        ColorRepository.getInstance().getNetworkColorManager().updateField("colorName", color.getColorName(), "isSecondaryColor", !color.isSecondaryColor());
    }
}
