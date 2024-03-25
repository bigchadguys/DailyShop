package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.data.item.ItemPredicate;
import bigchadguys.dailyshop.trade.DirectTradeEntry;
import bigchadguys.dailyshop.trade.PoolTradeEntry;
import bigchadguys.dailyshop.trade.TableTradeEntry;
import bigchadguys.dailyshop.world.roll.IntRoll;
import net.minecraft.item.Items;

import java.lang.reflect.Type;

public class TradeTablesConfig extends RegistryConfig<TableTradeEntry> {

    @Override
    public String getFolder() {
        return "trade_tables";
    }

    @Override
    public Type getType() {
        return TableTradeEntry.class;
    }

    @Override
    public void process(String id, TableTradeEntry value) {
        value.setId(id);
    }

    @Override
    protected void reset() {
        this.put("daily_shop", new TableTradeEntry(IntRoll.ofConstant(5),
            new PoolTradeEntry()
                    .add(new DirectTradeEntry(IntRoll.ofConstant(2))
                        .addInput(1, ItemPredicate.of("#minecraft:saplings", true).orElseThrow(), IntRoll.ofConstant(2), 1)
                        .addInput(2, ItemPredicate.of("minecraft:emerald", true).orElseThrow(), IntRoll.ofUniform(1, 5), 1)
                        .addInput(3, ItemPredicate.of("minecraft:diamond", true).orElseThrow(), IntRoll.ofUniform(1, 5), 1)
                        .addOutput(Items.APPLE, null, IntRoll.ofUniform(10, 15), 1), 1)
        ));
    }

}
