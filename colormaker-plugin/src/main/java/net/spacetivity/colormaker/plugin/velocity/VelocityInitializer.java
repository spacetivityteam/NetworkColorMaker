package net.spacetivity.colormaker.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.spacetivity.colormaker.api.ColorAPI;

import java.util.logging.Logger;

@Plugin(id = "networkcolormaker", name = "NetworkColorMaker", version = "1.0", authors = {"TGamings"},dependencies = {@Dependency(id = "networkcolormaker")})
@Getter
public class VelocityInitializer {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityInitializer(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        ColorAPI.onEnable(true);
    }

    @Subscribe
    public void onProxyInitialization(ProxyShutdownEvent event) {
        ColorAPI.onDisable();
    }
}
