package bigchadguys.dailyshop.world.data;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.init.ModConfigs;
import bigchadguys.dailyshop.init.ModNetwork;
import bigchadguys.dailyshop.init.ModWorldData;
import bigchadguys.dailyshop.net.DailyShopUpdateS2CPacket;
import bigchadguys.dailyshop.trade.EmptyShop;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.world.random.JavaRandom;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class DailyShopData extends WorldData {

    public static final DailyShopData CLIENT = new DailyShopData();

    private Shop shop;
    private long lastUpdated;

    public DailyShopData() {
        this.shop = EmptyShop.INSTANCE;
    }

    public Shop getShop() {
        return this.shop;
    }

    public void setShop(Shop shop, MinecraftServer server) {
        this.shop = shop;
        this.shop.setChangeListener(this, () -> this.onChanged(server));
        this.lastUpdated = System.currentTimeMillis();
        this.onChanged(server);
    }

    public void reset() {
        this.lastUpdated = 0;
        this.setDirty(true);
    }

    public void onTick(MinecraftServer server) {
        if(ModConfigs.DAILY_SHOP.shouldUpdate(this.lastUpdated)) {
            this.setShop(ModConfigs.DAILY_SHOP.generate(JavaRandom.ofNanoTime()), server);
        }
    }

    public void onChanged(MinecraftServer server) {
        this.setDirty(true);

        if(server != null) {
            for(ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                this.sendUpdatesToClient(player);
            }
        }
    }

    private void sendUpdatesToClient(ServerPlayerEntity player) {
        ModNetwork.CHANNEL.sendToPlayer(player, new DailyShopUpdateS2CPacket(this.shop));
    }

    @Override
    public Optional<NbtCompound> writeNbt() {
        NbtCompound nbt = new NbtCompound();
        Adapters.SHOP.writeNbt(this.shop).ifPresent(value -> nbt.put("shop", value));
        Adapters.LONG.writeNbt(this.lastUpdated).ifPresent(value -> nbt.put("lastUpdated", value));
        return Optional.of(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.shop = Adapters.SHOP.readNbt(nbt.get("shop")).orElse(null);
        this.lastUpdated = Adapters.LONG.readNbt(nbt.get("lastUpdated")).orElse(0L);
    }

    public static void initCommon() {
        PlayerEvent.PLAYER_JOIN.register(player -> {
            DailyShopData data = ModWorldData.DAILY_SHOP.getGlobal(player.getServerWorld());
            data.sendUpdatesToClient(player);
        });

        TickEvent.SERVER_POST.register(server -> {
            DailyShopData data = ModWorldData.DAILY_SHOP.getGlobal(server);
            data.onTick(server);
        });
    }

}
