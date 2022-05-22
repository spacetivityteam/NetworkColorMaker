package net.spacetivity.colormaker.api.color;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NetworkColor {

    private String colorName;
    private String hexCode;
    private String permission;
    private boolean isPrimaryColor;
    private boolean isSecondaryColor;

    public static NetworkColor from(String name, String hexCode, String permission, boolean isPrimary, boolean isSecondary) {
        return new NetworkColor(name, hexCode, permission, isPrimary, isSecondary);
    }
}