package bigchadguys.dailyshop.init;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

import java.util.Map;

public class ModRenderers extends ModRegistries {

    public static class RenderLayers {
        public static void register(Map<Block, RenderLayer> registry, boolean fancyGraphicsOrBetter) {
            ClientLifecycleEvent.CLIENT_SETUP.register(minecraft -> {
                //ModRenderers.register(registry, ModBlocks.DAILY_SHOP.get(), RenderLayer.getCutout());
            });
        }
    }

    public static void register(Map<Block, RenderLayer> registry, Block block, RenderLayer layer) {
        registry.put(block, layer);
    }

}
