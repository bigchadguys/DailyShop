package bigchadguys.dailyshop.init;

import bigchadguys.dailyshop.screen.DailyShopScreen;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;

public class ModScreens extends ModRegistries {

    public static void register() {
        ClientLifecycleEvent.CLIENT_SETUP.register(minecraft -> {
            MenuRegistry.registerScreenFactory(ModScreenHandlers.DAILY_SHOP.get(), DailyShopScreen::new);
        });
    }

}
