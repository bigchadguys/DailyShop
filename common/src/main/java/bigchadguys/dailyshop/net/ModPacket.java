package bigchadguys.dailyshop.net;

import bigchadguys.dailyshop.DailyShopMod;
import bigchadguys.dailyshop.data.bit.ArrayBitBuffer;
import bigchadguys.dailyshop.data.serializable.IBitSerializable;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

    @Override
    public void apply(T listener) {
        ThreadExecutor<?> engine = switch(Platform.getEnvironment()) {
            case CLIENT -> this.getClientEngine(listener);
            case SERVER -> this.getServerEngine(listener);
        };

        if(engine == null || engine.isOnThread()) {
            this.onReceive(listener);
            return;
        }

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

    @Environment(EnvType.SERVER)
    public ThreadExecutor<?> getServerEngine(T listener) {
        if(listener instanceof ServerPlayNetworkHandler handler) {
            return handler.player.getServer();
        }

        DailyShopMod.LOGGER.error("Failed to handle packet {}, engine {} is unknown", this, listener);
        return null;
    }

    @Environment(EnvType.CLIENT)
    public ThreadExecutor<?> getClientEngine(T listener) {
        if(listener instanceof ServerPlayNetworkHandler handler) {
            return handler.player.getServer();
        } else if(listener instanceof ClientPlayNetworkHandler) {
            return MinecraftClient.getInstance();
        }

        DailyShopMod.LOGGER.error("Failed to handle packet {}, engine {} is unknown", this, listener);
        return null;
    }

}
