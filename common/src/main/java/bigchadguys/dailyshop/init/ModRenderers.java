package bigchadguys.dailyshop.init;

import bigchadguys.dailyshop.block.entity.DailyShopBlockEntity;
import bigchadguys.dailyshop.block.entity.renderer.DailyShopBlockEntityRenderer;
import bigchadguys.dailyshop.screen.DailyShopScreen;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

import java.util.Map;

public class ModRenderers {

    public static class BlockEntities extends ModRenderers {
        public static BlockEntityRendererFactory<DailyShopBlockEntity> DAILY_SHOP;

        public static void register(Map<BlockEntityType<?>, BlockEntityRendererFactory<?>> registry) {
            try {
                DAILY_SHOP = register(registry, ModBlocks.Entities.DAILY_SHOP.get(), DailyShopBlockEntityRenderer::new);
            } catch(Exception e) {
                ClientLifecycleEvent.CLIENT_SETUP.register(minecraft -> {
                    DAILY_SHOP = register(registry, ModBlocks.Entities.DAILY_SHOP.get(), DailyShopBlockEntityRenderer::new);
                });
            }
        }
    }

    public static <T extends BlockEntity> BlockEntityRendererFactory<T> register(
            Map<BlockEntityType<?>, BlockEntityRendererFactory<?>> registry,
            BlockEntityType<? extends T> type, BlockEntityRendererFactory<T> renderer) {
        registry.put(type, renderer);
        return renderer;
    }

}
