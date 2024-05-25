package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.DailyShopMod;
import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.init.ModConfigs;
import bigchadguys.dailyshop.world.random.RandomSource;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Optional;
import java.util.stream.Stream;

public class ReferenceTradeEntry extends TradeEntry {

    private String id;

    public ReferenceTradeEntry() {

    }

    public ReferenceTradeEntry(String id) {
        this.id = id;
    }

    @Override
    public Stream<Trade> generate(RandomSource random) {
        return ModConfigs.TRADE_TABLES.get(this.id).map(table -> table.generate(random)).orElse(Stream.empty());
    }

    @Override
    public void validate(String path) {
        if(ModConfigs.TRADE_TABLES.get(this.id).isEmpty()) {
            DailyShopMod.LOGGER.error("%s: Unregistered trade table <%s>".formatted(path, this.id));
        }
    }

    @Override
    public Optional<JsonElement> writeJson() {
        return Adapters.UTF_8.writeJson(this.id);
    }

    @Override
    public void readJson(JsonElement json) {
        if(json instanceof JsonPrimitive primitive && primitive.isString()) {
            this.id = primitive.getAsString();
        } else {
            throw new UnsupportedOperationException();
        }
    }

}
