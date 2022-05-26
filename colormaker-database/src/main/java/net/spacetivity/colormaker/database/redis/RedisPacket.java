package net.spacetivity.colormaker.database.redis;

import org.redisson.api.RTopicRx;

import java.io.Serializable;

public interface RedisPacket extends Serializable {

    void send(RTopicRx channel);

}
