package net.spacetivity.colormaker.plugin.spigot.setup;

import lombok.Getter;
import net.spacetivity.colormaker.api.color.NetworkColor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class SetupManager {

    private final Map<UUID, NetworkColor> colorSetup = new ConcurrentHashMap<>();

    public void addToSetup(UUID uniqueId, NetworkColor networkColor) {
        colorSetup.put(uniqueId, networkColor);
    }

    public NetworkColor currentColor(UUID uniqueId) {
        return colorSetup.get(uniqueId);
    }
}
