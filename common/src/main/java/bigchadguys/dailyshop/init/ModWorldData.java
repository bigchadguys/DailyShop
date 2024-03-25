package bigchadguys.dailyshop.init;

import bigchadguys.dailyshop.world.data.DailyShopData;
import bigchadguys.dailyshop.world.data.WorldDataType;

public class ModWorldData extends ModRegistries {

    public static WorldDataType<DailyShopData> DAILY_SHOP;

    public static void register() {
        DAILY_SHOP = new WorldDataType<>("dailyshop.daily_shop", DailyShopData::new);
        DailyShopData.initCommon();
    }

}
