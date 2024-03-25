package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.data.adapter.basic.TypeSupplierAdapter;
import bigchadguys.dailyshop.data.bit.BitBuffer;
import bigchadguys.dailyshop.data.serializable.IBitSerializable;
import bigchadguys.dailyshop.data.serializable.INbtSerializable;
import bigchadguys.dailyshop.data.serializable.ISerializable;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class Shop implements ISerializable<NbtCompound, JsonObject> {

    protected Map<Object, Runnable> changeListeners;

    public Shop() {
        this.changeListeners = new HashMap<>();
    }

    public abstract Stream<Trade> getTrades();

    public final void setChangeListener(Object reference, Runnable listener) {
        this.changeListeners.put(reference, listener);
    }

    public final void removeChangeListener(Object reference) {
        this.changeListeners.remove(reference);
    }

    protected final void onChanged() {
        this.changeListeners.values().forEach(Runnable::run);
    }

    @Override
    public void writeBits(BitBuffer buffer) {

    }

    @Override
    public void readBits(BitBuffer buffer) {

    }

    @Override
    public Optional<NbtCompound> writeNbt() {
        return Optional.of(new NbtCompound());
    }

    public void readNbt(NbtCompound nbt) {

    }

    public static class Adapter extends TypeSupplierAdapter<Shop> {
        public Adapter() {
            super("type", true);
            this.register("empty", EmptyShop.class, EmptyShop::new);
            this.register("array", ArrayShop.class, ArrayShop::new);
        }
    }

}
