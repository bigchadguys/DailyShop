package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.init.ModConfigs;
import bigchadguys.dailyshop.trade.ArrayShop;
import bigchadguys.dailyshop.trade.ReferenceTradeEntry;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.trade.TradeEntry;
import bigchadguys.dailyshop.world.random.RandomSource;
import com.google.gson.annotations.Expose;

public class DailyShopConfig extends FileConfig {

    @Expose private long startEpoch;
    @Expose private long refreshDelay;
    @Expose private TradeEntry trades;

    public boolean shouldUpdate(long lastUpdated) {
        long refreshes = (lastUpdated - this.startEpoch) / this.refreshDelay;
        long nextRefresh = this.startEpoch + (refreshes + 1) * this.refreshDelay;
        return System.currentTimeMillis() >= nextRefresh;
    }

    public Shop generate(RandomSource random) {
        return new ArrayShop(this.trades.generate(random).toList());
    }

    public void validate(String path) {
        this.trades.validate(path + ".trades");
    }

    @Override
    public String getPath() {
        return "daily_shop";
    }

    @Override
    protected void reset() {
        this.startEpoch = 0;
        this.refreshDelay = 60 * 1000;
        this.trades = new ReferenceTradeEntry("daily_shop");
    }

    @Override
    public <T extends Config> T read() {
        T config = super.read();
        ModConfigs.POST_LOAD.add(() -> ((DailyShopConfig)config).validate(this.getPath()));
        return config;
    }

}
