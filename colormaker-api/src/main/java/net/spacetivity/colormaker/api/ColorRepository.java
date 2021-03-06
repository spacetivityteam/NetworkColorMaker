package net.spacetivity.colormaker.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.api.color.NetworkColorManager;
import net.spacetivity.colormaker.api.player.ColorPlayer;
import net.spacetivity.colormaker.api.player.ColorPlayerManager;
import net.spacetivity.colormaker.database.DatabaseRepository;
import org.redisson.api.RTopicRx;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class ColorRepository {

    private final Set<ColorPlayer> cachedPlayers = new LinkedHashSet<>();
    private final List<NetworkColor> cachedColors = new CopyOnWriteArrayList<>();

    @Getter
    private static ColorRepository instance;
    public static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();

    /* Boolean decides if a color update should be sent via redis to the
    proxy so that then the colors are updated in the proxy cache. */
    private final boolean useProxy;
    private final NetworkColorManager networkColorManager;
    private final ColorPlayerManager colorPlayerManager;

    private RTopicRx updateChannel;

    public ColorRepository(boolean useProxy) {
        instance = this;
        this.useProxy = useProxy;
        this.createTables();
        this.networkColorManager = new NetworkColorManager("colors");
        this.colorPlayerManager = new ColorPlayerManager("players");
        this.updateChannel = null;

        if (useProxy) {
            DatabaseRepository.getInstance().enableRedis();
            this.updateChannel = DatabaseRepository.getInstance().getRedissonManager().getConnector().getTopic("colorUpdateChannel");
        }
    }

    public static void onEnableSpigot(boolean useProxy) {
        new ColorRepository(useProxy);
    }

    public static void onEnableProxy() {
        CacheAPI.Packet.listenForPlayerUpdates(packet -> {
            UUID uniqueId = packet.getUniqueId();
            ColorAPI.getPlayerAsync(uniqueId, CacheAPI.Player::update);
        });

        CacheAPI.Packet.listenForColorUpdates(packet -> CacheAPI.Color.updateCachedColorsAsync());
    }

    public void onDisable() {

    }

    private void createTables() {
        LinkedHashMap<String, String> colorFields = new LinkedHashMap<>();
        colorFields.put("colorName", "VARCHAR(70) NOT NULL PRIMARY KEY");
        colorFields.put("colorCode", "VARCHAR(70) NOT NULL");
        colorFields.put("useHexadecimal", "TINYINT NOT NULL");
        colorFields.put("permission", "VARCHAR(70) NOT NULL");
        colorFields.put("defaultPrimaryColor", "TINYINT NOT NULL");
        colorFields.put("defaultSecondaryColor", "TINYINT NOT NULL");
        DatabaseRepository.getInstance().getDatabaseManager().createTable("colors", colorFields);

        LinkedHashMap<String, String> playerFields = new LinkedHashMap<>();
        playerFields.put("uniqueId", "VARCHAR(70) NOT NULL PRIMARY KEY");
        playerFields.put("primaryColor", "VARCHAR(70) NOT NULL");
        playerFields.put("secondaryColor", "VARCHAR(70) NOT NULL");
        DatabaseRepository.getInstance().getDatabaseManager().createTable("players", playerFields);
    }
}
