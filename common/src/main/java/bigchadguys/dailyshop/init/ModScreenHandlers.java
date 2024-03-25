package bigchadguys.dailyshop.init;

import bigchadguys.dailyshop.screen.handler.DailyShopScreenHandler;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.screen.ScreenHandlerType;

public class ModScreenHandlers extends ModRegistries {

    public static RegistrySupplier<ScreenHandlerType<DailyShopScreenHandler>> DAILY_SHOP;

    public static void register() {
        DAILY_SHOP = ModScreenHandlers.register(SCREEN_HANDLERS, "daily_shop", () -> MenuRegistry.ofExtended(DailyShopScreenHandler::new));
    }

}
