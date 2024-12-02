package bigchadguys.dailyshop.net;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.bit.BitBuffer;
import bigchadguys.dailyshop.world.data.DailyShopData;
import bigchadguys.dailyshop.world.data.DailyShopData.Entry;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.LinkedHashMap;
import java.util.Map;

public class DailyShopUpdateS2CPacket extends ModPacket<ClientPlayNetworkHandler> {

    private Map<String, Entry> entries;

    public DailyShopUpdateS2CPacket() {

    }

    public DailyShopUpdateS2CPacket(String id, Entry entry) {
        this.entries = new LinkedHashMap<>();
        this.entries.put(id, entry);
    }

    public DailyShopUpdateS2CPacket(Map<String, Entry> entries) {
        this.entries = entries;
    }

    @Override
    public void onReceive(ClientPlayNetworkHandler listener) {
        if(this.entries != null) {
            this.entries.forEach((id, shop) -> {
                if(shop != null) {
                    DailyShopData.CLIENT.getEntries().put(id, shop);
                } else {
                    DailyShopData.CLIENT.getEntries().remove(id);
                }
            });
        } else {
            DailyShopData.CLIENT.getEntries().clear();
        }
    }

    @Override
    public void writeBits(BitBuffer buffer) {
        Adapters.BOOLEAN.writeBits(this.entries != null, buffer);

        if(this.entries != null) {
           Adapters.INT_SEGMENTED_3.writeBits(this.entries.size(), buffer);

           this.entries.forEach((id, entry) -> {
               Adapters.UTF_8.asNullable().writeBits(id, buffer);
               Adapters.SHOP_ENTRY.writeBits(entry, buffer);
           });
        }
    }

    @Override
    public void readBits(BitBuffer buffer) {
        if(Adapters.BOOLEAN.readBits(buffer).orElseThrow()) {
            this.entries = new LinkedHashMap<>();
            int size = Adapters.INT_SEGMENTED_3.readBits(buffer).orElseThrow();

            for(int i = 0; i < size; i++) {
               this.entries.put(Adapters.UTF_8.asNullable().readBits(buffer).orElse(null),
                       Adapters.SHOP_ENTRY.readBits(buffer).orElse(null));
            }
        } else {
            this.entries = null;
        }
    }

}
