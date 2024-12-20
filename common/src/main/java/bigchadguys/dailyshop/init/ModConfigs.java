package bigchadguys.dailyshop.init;

import bigchadguys.dailyshop.config.*;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;

import java.util.ArrayList;
import java.util.List;

public class ModConfigs extends ModRegistries {

    public static List<Runnable> POST_LOAD = new ArrayList<>();

    public static TileGroupsConfig TILE_GROUPS;
    public static EntityGroupsConfig ENTITY_GROUPS;
    public static ItemGroupsConfig ITEM_GROUPS;

    public static DailyShopConfig DAILY_SHOP;
    public static TradeTablesConfig TRADE_TABLES;

    public static void register(boolean initialization) {
        TILE_GROUPS = new TileGroupsConfig().read();
        ENTITY_GROUPS = new EntityGroupsConfig().read();
        ITEM_GROUPS = new ItemGroupsConfig().read();

        DAILY_SHOP = new DailyShopConfig().read();
        TRADE_TABLES = new TradeTablesConfig().read();

        if(!initialization) {
            POST_LOAD.forEach(Runnable::run);
            POST_LOAD.clear();
        } else {
            LifecycleEvent.SETUP.register(() -> {
                POST_LOAD.forEach(Runnable::run);
                POST_LOAD.clear();
            });
        }
    }

}
