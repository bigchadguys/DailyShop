package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.trade.ArrayShop;
import bigchadguys.dailyshop.trade.ReferenceTradeEntry;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.trade.TradeEntry;
import bigchadguys.dailyshop.world.random.RandomSource;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

public class DailyShopConfig extends FileConfig {

    @Expose private String updateTime;
    @Expose private TradeEntry trades;

    public boolean shouldUpdate(long lastUpdated) {
        String[] raw = this.updateTime.split(Pattern.quote(":"));

        long expectedLastUpdate = LocalDateTime.now()
                .withHour(Integer.parseInt(raw[0]))
                .withMinute(Integer.parseInt(raw[1]))
                .withSecond(Integer.parseInt(raw[2]))
                .minusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        return lastUpdated < expectedLastUpdate;
    }

    public Shop generate(RandomSource random) {
        return new ArrayShop(this.trades.generate(random).toList());
    }

    @Override
    public String getPath() {
        return "daily_shop";
    }

    @Override
    protected void reset() {
        this.updateTime = "06:00:00";
        this.trades = new ReferenceTradeEntry("daily_shop");
    }

}
