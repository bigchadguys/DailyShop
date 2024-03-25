package bigchadguys.dailyshop;

import bigchadguys.dailyshop.init.ModRegistries;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DailyShopMod {

    public static final String ID = "dailyshop";
    public static final Logger LOGGER = LogManager.getLogger(ID);

    public static void onInitialize() {
        ModRegistries.register();
    }

    public static Identifier id(String path) {
        return new Identifier(ID, path);
    }

}
