package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.util.WeightedList;
import bigchadguys.dailyshop.world.random.RandomSource;
import com.google.gson.JsonElement;

import java.util.Optional;
import java.util.stream.Stream;

public class PoolTradeEntry extends TradeEntry {

    public WeightedList<TradeEntry> entries;

    public PoolTradeEntry() {
        this.entries = new WeightedList<>();
    }

    public PoolTradeEntry add(TradeEntry entry, double weight) {
        this.entries.add(entry, weight);
        return this;
    }

    @Override
    public Stream<Trade> generate(RandomSource random) {
        return this.entries.getRandom(random).map(entry -> entry.generate(random)).orElse(Stream.empty());
    }

    @Override
    public Optional<JsonElement> writeJson() {
        return this.entries.writeJson(Adapters.TRADE::writeJson);
    }

    @Override
    public void readJson(JsonElement json) {
        this.entries.readJson(json, Adapters.TRADE::readJson);
    }

}
