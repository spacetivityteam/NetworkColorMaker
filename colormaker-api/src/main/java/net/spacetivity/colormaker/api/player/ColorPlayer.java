package net.spacetivity.colormaker.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.spacetivity.colormaker.api.CacheAPI;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ColorPlayer {

    private UUID uniqueId;
    private String primaryColor;
    private String secondaryColor;

    public static ColorPlayer from(UUID uniqueId, String primaryColor, String secondaryColor) {
        return new ColorPlayer(uniqueId, primaryColor, secondaryColor);
    }

    public void putInCache() {
        CacheAPI.Player.putInCache(this);
    }
}
