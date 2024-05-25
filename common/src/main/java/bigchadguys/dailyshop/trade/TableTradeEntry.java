package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.world.random.RandomSource;
import bigchadguys.dailyshop.world.roll.IntRoll;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TableTradeEntry extends TradeEntry {

    private String id;

    private IntRoll roll;
    private TradeEntry pool;

    public TableTradeEntry() {

    }

    public TableTradeEntry(IntRoll roll, TradeEntry pool) {
        this.roll = roll;
        this.pool = pool;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Stream<Trade> generate(RandomSource random) {
        return IntStream.range(0, this.roll.get(random))
                .mapToObj(i -> this.pool.generate(random))
                .flatMap(Function.identity());
    }

    @Override
    public void validate(String path) {
        this.pool.validate(path);
    }

    @Override
    public Optional<JsonElement> writeJson() {
        return Adapters.TRADE.writeJson(this.pool).map(json -> {
            if(json instanceof JsonObject object) {
                JsonObject copy = new JsonObject();
                Adapters.INT_ROLL.writeJson(this.roll).ifPresent(value -> copy.add("roll", value));

                for(String key : object.keySet()) {
                    copy.add(key, object.get(key));
                }

                return copy;
            } else {
                JsonObject object = new JsonObject();
                Adapters.INT_ROLL.writeJson(this.roll).ifPresent(value -> object.add("roll", value));
                object.add("pool", json);
                return object;
            }
        });
    }

    @Override
    public void readJson(JsonElement json) {
        if(json instanceof JsonObject object) {
            this.roll = Adapters.INT_ROLL.readJson(object.get("roll")).orElseThrow();

            if(object.has("pool")) {
                this.pool = Adapters.TRADE.readJson(object.get("pool")).orElseThrow();
            } else {
                object.remove("roll");
                this.pool = Adapters.TRADE.readJson(object).orElseThrow();
            }
        }
    }

}
