package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.data.adapter.ISimpleAdapter;
import bigchadguys.dailyshop.data.serializable.ISerializable;
import bigchadguys.dailyshop.world.random.RandomSource;
import com.google.gson.*;
import net.minecraft.nbt.NbtElement;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class TradeEntry implements ISerializable<NbtElement, JsonElement> {

    public abstract Stream<Trade> generate(RandomSource random);

    @Override
    public Optional<JsonElement> writeJson() {
        return Optional.of(JsonNull.INSTANCE);
    }

    @Override
    public void readJson(JsonElement json) {

    }

    public static class Adapter implements ISimpleAdapter<TradeEntry, NbtElement, JsonElement> {
        @Override
        public Optional<JsonElement> writeJson(TradeEntry value) {
            return value.writeJson();
        }

        @Override
        public Optional<TradeEntry> readJson(JsonElement json) {
            if(json instanceof JsonArray || json instanceof JsonObject object && object.has("weight")) {
                PoolTradeEntry pool = new PoolTradeEntry();
                pool.readJson(json);
                return Optional.of(pool);
            } else if(json instanceof JsonObject object && object.has("roll")) {
                TableTradeEntry table = new TableTradeEntry();
                table.readJson(json);
                return Optional.of(table);
            } else if(json instanceof JsonPrimitive primitive && primitive.isString()) {
                ReferenceTradeEntry reference = new ReferenceTradeEntry();
                reference.readJson(json);
                return Optional.of(reference);
            } else {
                DirectTradeEntry direct = new DirectTradeEntry();
                direct.readJson(json);
                return Optional.of(direct);
            }
        }
    }

}
