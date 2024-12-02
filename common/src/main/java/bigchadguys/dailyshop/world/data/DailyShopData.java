package bigchadguys.dailyshop.world.data;

import bigchadguys.dailyshop.DailyShopMod;
import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.bit.BitBuffer;
import bigchadguys.dailyshop.data.serializable.INbtSerializable;
import bigchadguys.dailyshop.data.serializable.ISerializable;
import bigchadguys.dailyshop.init.ModConfigs;
import bigchadguys.dailyshop.init.ModNetwork;
import bigchadguys.dailyshop.init.ModWorldData;
import bigchadguys.dailyshop.net.DailyShopUpdateS2CPacket;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.world.random.JavaRandom;
import com.google.gson.JsonObject;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.*;

public class DailyShopData extends WorldData {

    public static final DailyShopData CLIENT = new DailyShopData();

    private final Map<String, Entry> entries;

    public DailyShopData() {
        this.entries = new LinkedHashMap<>();
    }

    public Map<String, Entry> getEntries() {
        return this.entries;
    }

    public Optional<Shop> getShop(String id) {
        return Optional.ofNullable(this.entries.get(id)).map(entry -> entry.shop);
    }

    public void onAcknowledge(String id, PlayerEntity player) {
        Entry entry = this.entries.get(id);
        if(entry == null) return;
        entry.acknowledgments.add(player.getUuid());

        if(player.getServer() != null) {
            for(ServerPlayerEntity other : player.getServer().getPlayerManager().getPlayerList()) {
                ModNetwork.CHANNEL.sendToPlayer(other, new DailyShopUpdateS2CPacket(id, entry));
            }
        }
    }

    public void setShop(String id, Shop shop, MinecraftServer server) {
        if(shop == null) {
            this.entries.remove(id);
        } else {
            Entry entry = new Entry(shop, System.currentTimeMillis());
            this.entries.put(id, entry);

            shop.setChangeListener(this, () -> {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    ModNetwork.CHANNEL.sendToPlayer(player, new DailyShopUpdateS2CPacket(id, entry));
                }

                this.setDirty(true);
            });
        }

        for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ModNetwork.CHANNEL.sendToPlayer(player, new DailyShopUpdateS2CPacket(id, this.entries.get(id)));
        }
    }

    public void reset() {
        this.entries.clear();
        this.setDirty(true);
    }

    public void onTick(MinecraftServer server) {
        Map<String, Entry> changes = new LinkedHashMap<>();

        ModConfigs.DAILY_SHOP.getShops().forEach((id, entry) -> {
            if(!this.entries.containsKey(id) || entry.shouldUpdate(this.entries.get(id).lastUpdated)) {
                this.setShop(id, entry.generate(JavaRandom.ofNanoTime()), server);

                if(id == null) {
                    DailyShopMod.LOGGER.info("Refreshed daily shop!");
                } else {
                    DailyShopMod.LOGGER.info("Refreshed daily shop %s!".formatted(id));
                }

                changes.put(id, this.entries.get(id));
            }
        });

        this.entries.keySet().removeIf(id -> {
            if(!ModConfigs.DAILY_SHOP.getShops().containsKey(id)) {
                changes.put(id, null);
                return true;
            }

            return false;
        });

        if(!changes.isEmpty()) {
            this.setDirty(true);
        }

        for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ModNetwork.CHANNEL.sendToPlayer(player, new DailyShopUpdateS2CPacket(changes));
        }
    }

    @Override
    public Optional<NbtCompound> writeNbt() {
        NbtCompound nbt = new NbtCompound();

        if(this.entries.containsKey(null)) {
            this.entries.get(null).writeNbt().ifPresent(tag -> nbt.put("default", tag));
        }

        NbtCompound custom = new NbtCompound();

        this.entries.forEach((id, entry) -> {
            if(id == null) return;

            entry.writeNbt().ifPresent(tag -> {
                custom.put(id, tag);
            });
        });

        nbt.put("custom", custom);
        return Optional.of(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.entries.clear();

        if(!nbt.contains("custom")) {
            Shop shop = Adapters.SHOP.readNbt(nbt.get("shop")).orElse(null);
            long lastUpdated = Adapters.LONG.readNbt(nbt.get("lastUpdated")).orElse(0L);

            if(shop != null) {
                this.entries.put(null, new Entry(shop, lastUpdated));
            }
        } else {
            if(nbt.contains("default")) {
                Entry entry = new Entry(null, 0L);
                entry.readNbt(nbt.getCompound("default"));

                if(entry.shop != null) {
                    this.entries.put(null, entry);
                }
            }

            NbtCompound custom = nbt.getCompound("custom");

            for(String id : custom.getKeys()) {
                Entry entry = new Entry(null, 0L);
                entry.readNbt(custom.getCompound(id));

                if(entry.shop != null) {
                    this.entries.put(id, entry);
                }
            }
        }
    }

    public static void initCommon() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            DailyShopData data = ModWorldData.DAILY_SHOP.getGlobal(player.getServerWorld());
            ModNetwork.CHANNEL.sendToPlayer(player, new DailyShopUpdateS2CPacket(data.entries));
        });

        TickEvent.SERVER_POST.register(server -> {
            DailyShopData data = ModWorldData.DAILY_SHOP.getGlobal(server);
            data.onTick(server);
        });
    }

    public static class Entry implements ISerializable<NbtCompound, JsonObject> {
        private Shop shop;
        private long lastUpdated;
        private final Set<UUID> acknowledgments;

        public Entry() {
            this.acknowledgments = new HashSet<>();
        }

        public Entry(Shop shop, long lastUpdated) {
            this.shop = shop;
            this.lastUpdated = lastUpdated;
            this.acknowledgments = new HashSet<>();
        }

        public Shop getShop() {
            return this.shop;
        }

        public long getLastUpdated() {
            return this.lastUpdated;
        }

        public Set<UUID> getAcknowledgments() {
            return this.acknowledgments;
        }

        @Override
        public void writeBits(BitBuffer buffer) {
            Adapters.SHOP.writeBits(this.shop, buffer);
            Adapters.LONG.writeBits(this.lastUpdated, buffer);
            Adapters.INT_SEGMENTED_3.writeBits(this.acknowledgments.size(), buffer);

            this.acknowledgments.forEach((uuid) -> {
               Adapters.UUID.writeBits(uuid, buffer);
            });
        }

        @Override
        public void readBits(BitBuffer buffer) {
            this.shop = Adapters.SHOP.readBits(buffer).orElseThrow();
            this.lastUpdated = Adapters.LONG.readBits(buffer).orElseThrow();

            int size = Adapters.INT_SEGMENTED_3.readBits(buffer).orElseThrow();
            this.acknowledgments.clear();

            for(int i = 0; i < size; i++) {
                this.acknowledgments.add(Adapters.UUID.readBits(buffer).orElseThrow());
            }
        }

        @Override
        public Optional<NbtCompound> writeNbt() {
            return Optional.of(new NbtCompound()).map(nbt -> {
                Adapters.SHOP.writeNbt(this.shop).ifPresent(value -> nbt.put("shop", value));
                Adapters.LONG.writeNbt(this.lastUpdated).ifPresent(value -> nbt.put("lastUpdated", value));

                NbtList acknowledgments = new NbtList();

                this.acknowledgments.forEach(player -> {
                    Adapters.UUID.writeNbt(player).ifPresent(acknowledgments::add);
                });

                nbt.put("acknowledgments", acknowledgments);
                return nbt;
            });
        }

        @Override
        public void readNbt(NbtCompound nbt) {
            this.shop = Adapters.SHOP.readNbt(nbt.get("shop")).orElse(null);
            this.lastUpdated = Adapters.LONG.readNbt(nbt.get("lastUpdated")).orElse(0L);

            this.acknowledgments.clear();

            if(nbt.get("acknowledgments") instanceof NbtList acknowledgments) {
                for(NbtElement element : acknowledgments) {
                   Adapters.UUID.readNbt(element).ifPresent(uuid -> {
                       this.acknowledgments.add(uuid);
                   });
                }
            }
        }
    }

}
