package net.spacetivity.colormaker.api.event;

import lombok.Getter;
import net.spacetivity.colormaker.api.player.ColorPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class SpigotColorUpdateEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ColorPlayer colorPlayer;

    public SpigotColorUpdateEvent(@NotNull Player who, ColorPlayer colorPlayer) {
        super(who);
        this.colorPlayer = colorPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
