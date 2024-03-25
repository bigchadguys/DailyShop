package bigchadguys.dailyshop.net;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.bit.BitBuffer;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.world.data.DailyShopData;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class DailyShopUpdateS2CPacket extends ModPacket<ClientPlayNetworkHandler> {

    private Shop shop;

    public DailyShopUpdateS2CPacket() {

    }

    public DailyShopUpdateS2CPacket(Shop shop) {
        this.shop = shop;
    }

    @Override
    public void onReceive(ClientPlayNetworkHandler listener) {
        DailyShopData.CLIENT.setShop(this.shop, null);
    }

    @Override
    public void writeBits(BitBuffer buffer) {
        Adapters.SHOP.writeBits(this.shop, buffer);
    }

    @Override
    public void readBits(BitBuffer buffer) {
        this.shop = Adapters.SHOP.readBits(buffer).orElse(null);
    }

}
