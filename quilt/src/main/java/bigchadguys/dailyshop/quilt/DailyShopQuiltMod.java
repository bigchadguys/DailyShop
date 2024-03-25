package bigchadguys.dailyshop.quilt;

import bigchadguys.dailyshop.DailyShopMod;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class DailyShopQuiltMod implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        DailyShopMod.onInitialize();
    }

}
