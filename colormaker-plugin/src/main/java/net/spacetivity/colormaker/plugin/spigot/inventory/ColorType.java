package net.spacetivity.colormaker.plugin.spigot.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ColorType {

    PRIMARY("Primary"),
    SECONDARY("Secondary");

    private final String title;
}
