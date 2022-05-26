package net.spacetivity.colormaker.api.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.spacetivity.colormaker.database.redis.RedisPacket;
import org.redisson.api.RTopicRx;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class PlayerColorUpdatePacket implements RedisPacket {

    private UUID uniqueId;

    @Override
    public void send(RTopicRx channel) {
        channel.publish(this).subscribe();
    }
}
