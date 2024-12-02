package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.init.ModConfigs;
import bigchadguys.dailyshop.trade.ArrayShop;
import bigchadguys.dailyshop.trade.ReferenceTradeEntry;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.trade.TradeEntry;
import bigchadguys.dailyshop.world.random.RandomSource;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DailyShopConfig extends FileConfig {

    @Expose @SerializedName("default") private Entry defaultShop;
    @Expose @SerializedName("custom") private Map<String, Entry> customShops;

    public Map<String, Entry> getShops() {
        Map<String, Entry> shops = new LinkedHashMap<>();
        shops.put(null, this.defaultShop);
        shops.putAll(this.customShops);
        return shops;
    }

    public boolean shouldUpdate(String id, long lastUpdated) {
        Entry entry = id == null ? this.defaultShop : this.customShops.get(id);
        if(entry == null) return false;
        return entry.shouldUpdate(lastUpdated);
    }

    public Optional<Shop> generate(String id, RandomSource random) {
        Entry entry = id == null ? this.defaultShop : this.customShops.get(id);
        if(entry == null) return Optional.empty();
        return Optional.of(entry.generate(random));
    }

    public void validate(String path) {
        if(this.defaultShop != null) {
            this.defaultShop.trades.validate(path + ".default");
        }

        this.customShops.forEach((id, entry) -> {
            entry.trades.validate(path + ".custom." + id);
        });
    }

    @Override
    public String getPath() {
        return "daily_shop";
    }

    @Override
    protected void reset() {
        this.defaultShop = new Entry(0, 60 * 1000, new ReferenceTradeEntry("daily_shop"));
        this.customShops = new LinkedHashMap<>();
        this.customShops.put("some_custom_shop", new Entry(0, 30 * 1000, new ReferenceTradeEntry("daily_shop")));
    }

    @Override
    public <T extends Config> T read() {
        T config = super.read();
        ModConfigs.POST_LOAD.add(() -> ((DailyShopConfig)config).validate(this.getPath()));
        return config;
    }

    public static class Entry {
        @Expose private long startEpoch;
        @Expose private long refreshDelay;
        @Expose private TradeEntry trades;

        public Entry(long startEpoch, long refreshDelay, TradeEntry trades) {
            this.startEpoch = startEpoch;
            this.refreshDelay = refreshDelay;
            this.trades = trades;
        }

        public boolean shouldUpdate(long lastUpdated) {
            long refreshes = (lastUpdated - this.startEpoch) / this.refreshDelay;
            long nextRefresh = this.startEpoch + (refreshes + 1) * this.refreshDelay;
            return System.currentTimeMillis() >= nextRefresh;
        }

        public Shop generate(RandomSource random) {
            return new ArrayShop(this.trades.generate(random).toList());
        }
    }

}
