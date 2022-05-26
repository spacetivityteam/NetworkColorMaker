package net.spacetivity.colormaker.api.color;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.format.TextColor;
import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.ColorTranslator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NetworkColor {

    private String colorName;
    private String colorCode;
    private boolean useHexadecimal;
    private String permission;
    private boolean isPrimaryColor;
    private boolean isSecondaryColor;

    public static NetworkColor from(String colorName, String colorCode, boolean useHexadecimal, String permission, boolean isPrimary, boolean isSecondary) {
        return new NetworkColor(colorName, colorCode, useHexadecimal, permission, isPrimary, isSecondary);
    }

    public static NetworkColor from(String colorName, String colorCode, boolean useHexadecimal, String permission) {
        return new NetworkColor(colorName, colorCode, useHexadecimal, permission, false, false);
    }

    public static NetworkColor fromCachedColor(String colorName) {
        return CacheAPI.Color.getCachedColors().stream().filter(networkColor -> networkColor.getColorName().equalsIgnoreCase(colorName))
                .findFirst().orElse(null);
    }

    public String toBungee() {
        return ColorTranslator.Bungee.toChatColor(this);
    }

    public String toSpigot() {
        return ColorTranslator.Spigot.toChatColor(this);
    }

    public TextColor toVelocity() {
        return ColorTranslator.Velocity.toTextColor(this);
    }

    public TextColor toPaper() {
        return ColorTranslator.Spigot.toTextColor(this);
    }
}