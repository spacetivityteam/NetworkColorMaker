package net.spacetivity.colormaker.api.messaging;

import net.spacetivity.colormaker.database.redis.RedisPacket;
import org.redisson.api.RTopicRx;

public class CachedColorsUpdatePacket implements RedisPacket {

    @Override
    public void send(RTopicRx channel) {
        channel.publish(this).subscribe();
    }
}
