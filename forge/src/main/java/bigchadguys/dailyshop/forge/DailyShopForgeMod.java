package bigchadguys.dailyshop.forge;

import dev.architectury.platform.forge.EventBuses;
import bigchadguys.dailyshop.DailyShopMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DailyShopMod.ID)
public class DailyShopForgeMod {

    public DailyShopForgeMod() {
        EventBuses.registerModEventBus(DailyShopMod.ID, FMLJavaModLoadingContext.get().getModEventBus());
        DailyShopMod.onInitialize();
    }

}
