package net.spacetivity.colormaker.plugin.spigot.setup;

import lombok.Getter;
import net.spacetivity.colormaker.api.color.NetworkColor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class SetupManager {

    private final Map<UUID, Integer> setupPlayers = new ConcurrentHashMap<>();
    private final Map<UUID, NetworkColor> colorSetup = new ConcurrentHashMap<>();

    public boolean isProcessingSetup(UUID uniqueId) {
        return setupPlayers.containsKey(uniqueId);
    }

    public int getSetupCount(UUID uniqueId) {
        return setupPlayers.get(uniqueId);
    }

    public void nextSetupStep(UUID uniqueId) {
        setupPlayers.replace(uniqueId, getSetupCount(uniqueId) + 1);
    }

    public void addToSetup(UUID uniqueId) {
        setupPlayers.put(uniqueId, 0);
        colorSetup.put(uniqueId, new NetworkColor());
    }

    public NetworkColor currentColor(UUID uniqueId) {
        return colorSetup.get(uniqueId);
    }
}
