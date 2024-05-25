package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.util.WeightedList;
import bigchadguys.dailyshop.world.random.RandomSource;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
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
    public void validate(String path) {
        List<TradeEntry> entries = new ArrayList<>(this.entries.keySet());

        for(int i = 0; i < entries.size(); i++) {
           entries.get(i).validate("%s[%d]".formatted(path, i));
        }
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
