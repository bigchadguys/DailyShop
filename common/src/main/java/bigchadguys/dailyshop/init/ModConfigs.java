package bigchadguys.dailyshop.init;

import bigchadguys.dailyshop.config.*;

public class ModConfigs extends ModRegistries {

    public static ItemGroupsConfig ITEM_GROUPS;
    public static DailyShopConfig DAILY_SHOP;
    public static TradeTablesConfig TRADE_TABLES;

    public static void register() {
        ITEM_GROUPS = new ItemGroupsConfig().read();
        DAILY_SHOP = new DailyShopConfig().read();
        TRADE_TABLES = new TradeTablesConfig().read();
    }

}
