package bigchadguys.dailyshop.net;

import bigchadguys.dailyshop.DailyShopMod;
import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.bit.BitBuffer;
import bigchadguys.dailyshop.init.ModWorldData;
import bigchadguys.dailyshop.screen.handler.DailyShopScreenHandler;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.trade.Trade;
import bigchadguys.dailyshop.util.TradeExecutor;
import bigchadguys.dailyshop.world.data.DailyShopData;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class DailyShopTradeC2SPacket extends ModPacket<ServerPlayNetworkHandler> {

    private int index;

    public DailyShopTradeC2SPacket() {

    }

    public DailyShopTradeC2SPacket(int index) {
        this.index = index;
    }

    @Override
    public void writeBits(BitBuffer buffer) {
        Adapters.INT_SEGMENTED_3.writeBits(this.index, buffer);
    }

    @Override
    public void readBits(BitBuffer buffer) {
        this.index = Adapters.INT_SEGMENTED_3.readBits(buffer).orElseThrow();
    }

    @Override
    public void onReceive(ServerPlayNetworkHandler listener) {
        ServerPlayerEntity player = listener.getPlayer();
        ScreenHandler handler = player.currentScreenHandler;

        if(handler instanceof DailyShopScreenHandler shopHandler) {
            if(!shopHandler.canUse(player)) {
                DailyShopMod.LOGGER.debug("Player {} interacted with invalid menu {}", player, shopHandler);
                return;
            }

            DailyShopData data = ModWorldData.DAILY_SHOP.getGlobal(player.getWorld());
            Shop shop = data.getShop(shopHandler.getId()).orElse(null);
            if(shop == null) return;
            List<Trade> trades = shop.getTrades().toList();
            if(this.index < 0 || this.index >= trades.size()) return;
            Trade trade = trades.get(this.index);

            if(TradeExecutor.test(trade, shopHandler).canTrade()) {
                TradeExecutor.execute(trade, shopHandler);
                trade.onTrade(1);
                shop.onChanged();

                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1.0F, 2.0F);
            }
        }
    }

}
