package bigchadguys.dailyshop.net;

import bigchadguys.dailyshop.DailyShopMod;
import bigchadguys.dailyshop.data.bit.ArrayBitBuffer;
import bigchadguys.dailyshop.data.serializable.IBitSerializable;
import bigchadguys.dailyshop.data.serializable.IByteSerializable;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.thread.ThreadExecutor;

public abstract class ModPacket<T extends PacketListener> implements Packet<T>, IBitSerializable {

    public abstract void onReceive(T listener);

    @Override
    public final void write(PacketByteBuf buf) {
        ArrayBitBuffer buffer = ArrayBitBuffer.empty();
        this.writeBits(buffer);
        buf.writeLongArray(buffer.toLongArray());
    }

    public final void read(PacketByteBuf buf) {
        ArrayBitBuffer buffer = ArrayBitBuffer.backing(buf.readLongArray(), 0);
        this.readBits(buffer);
    }

    public void apply(T listener) {
        ThreadExecutor<?> engine = null;

        if(listener instanceof ServerPlayNetworkHandler handler) {
            engine = handler.player.getServer();
        } else if(listener instanceof ClientPlayNetworkHandler) {
            engine = MinecraftClient.getInstance();
        } else {
            DailyShopMod.LOGGER.error("Failed to handle packet {}, engine {} is unknown", this, listener);
        }

        if(engine == null || engine.isOnThread()) {
            this.onReceive(listener);
        } else {
            engine.executeSync(() -> {
                if(listener.isConnectionOpen()) {
                    try {
                        this.apply(listener);
                    } catch(Exception exception) {
                        if(listener.shouldCrashOnException()) {
                            throw exception;
                        }

                        DailyShopMod.LOGGER.error("Failed to handle packet {}, suppressing error", this, exception);
                    }
                } else {
                    DailyShopMod.LOGGER.debug("Ignoring packet due to disconnection: {}", this);
                }
            });
        }
    }

}
