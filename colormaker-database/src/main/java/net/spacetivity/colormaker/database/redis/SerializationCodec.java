package net.spacetivity.colormaker.database.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.redisson.codec.CustomObjectInputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationCodec extends BaseCodec {
    private final Decoder<Object> decoder;
    private final Encoder encoder;
    private final ClassLoader classLoader;

    public SerializationCodec() {
        this(null);
    }

    public SerializationCodec(ClassLoader classLoader) {
        this.decoder = new NamelessClass_2();
        this.encoder = new NamelessClass_1();
        this.classLoader = classLoader;
    }

    public SerializationCodec(ClassLoader classLoader, org.redisson.codec.SerializationCodec codec) {
        this.decoder = new NamelessClass_2();
        this.encoder = new NamelessClass_1();
        this.classLoader = classLoader;
    }

    public Decoder<Object> getValueDecoder() {
        return this.decoder;
    }

    public Encoder getValueEncoder() {
        return this.encoder;
    }

    public ClassLoader getClassLoader() {
        return this.classLoader != null ? this.classLoader : this.getClass().getClassLoader();
    }

    private class NamelessClass_2 implements Decoder<Object> {
        NamelessClass_2() {
        }

        public Object decode(ByteBuf buf, State state) throws IOException {
            try {
                ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

                Object var6;
                try {
                    ByteBufInputStream in = new ByteBufInputStream(buf);
                    ObjectInputStream inputStream;
                    if (SerializationCodec.this.classLoader != null) {
                        Thread.currentThread().setContextClassLoader(SerializationCodec.this.classLoader);
                        inputStream = new CustomObjectInputStream(SerializationCodec.this.classLoader, in);
                    } else {
                        inputStream = new ObjectInputStream(in);
                    }

                    var6 = inputStream.readObject();
                } finally {
                    Thread.currentThread().setContextClassLoader(currentThreadClassLoader);
                }

                return var6;
            } catch (IOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new IOException(ex);
            }
        }
    }

    private static class NamelessClass_1 implements Encoder {
        NamelessClass_1() {
        }

        public ByteBuf encode(Object in) throws IOException {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();

            try {
                ByteBufOutputStream result = new ByteBufOutputStream(out);
                ObjectOutputStream outputStream = new ObjectOutputStream(result);
                outputStream.writeObject(in);
                outputStream.close();
                return result.buffer();
            } catch (IOException ex) {
                out.release();
                throw ex;
            }
        }
    }
}
