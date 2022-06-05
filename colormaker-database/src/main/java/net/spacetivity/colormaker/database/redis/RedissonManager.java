package net.spacetivity.colormaker.database.redis;

import lombok.Getter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.config.Config;

public class RedissonManager {

    private final RedissonClient client;

    @Getter
    private final RedissonRxClient connector;

    public RedissonManager() {
        Config config = new Config();
        config.setCodec(new SerializationCodec());
        config.setNettyThreads(4);
        config.useSingleServer()
                .setAddress("redis://-:-")
                .setPassword("-");

        this.client = Redisson.create(config);
        this.connector = this.client.rxJava();
    }

    public void disconnect() {
        this.client.shutdown();
    }
}
