package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.bit.BitBuffer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ArrayShop extends Shop {

    private final List<Trade> trades;

    public ArrayShop() {
        this.trades = new ArrayList<>();
    }

    public ArrayShop(Collection<Trade> trades) {
        this();
        this.trades.addAll(trades);
    }

    @Override
    public Stream<Trade> getTrades() {
        return this.trades.stream();
    }

    @Override
    public void writeBits(BitBuffer buffer) {
        Adapters.INT_SEGMENTED_3.writeBits(this.trades.size(), buffer);

        for(Trade trade : this.trades) {
           trade.writeBits(buffer);
        }
    }

    @Override
    public void readBits(BitBuffer buffer) {
        this.trades.clear();
        int size = Adapters.INT_SEGMENTED_3.readBits(buffer).orElseThrow();

        for(int i = 0; i < size; i++) {
            Trade trade = new Trade();
            trade.readBits(buffer);
            this.trades.add(trade);
        }
    }

    @Override
    public Optional<NbtCompound> writeNbt() {
        NbtCompound nbt = new NbtCompound();
        NbtList list = new NbtList();

        for(Trade trade : this.trades) {
           trade.writeNbt().ifPresent(list::add);
        }

        nbt.put("trades", list);
        return Optional.of(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.trades.clear();
        NbtList list = nbt.getList("trades", NbtElement.COMPOUND_TYPE);

        for(NbtElement element : list) {
            if(element instanceof NbtCompound compound) {
                Trade trade = new Trade();
                trade.readNbt(compound);
                this.trades.add(trade);
            }
        }
    }

}
