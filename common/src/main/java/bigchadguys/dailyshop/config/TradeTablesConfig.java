package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.data.item.PartialItem;
import bigchadguys.dailyshop.init.ModConfigs;
import bigchadguys.dailyshop.trade.DirectTradeEntry;
import bigchadguys.dailyshop.trade.PoolTradeEntry;
import bigchadguys.dailyshop.trade.ReferenceTradeEntry;
import bigchadguys.dailyshop.trade.TableTradeEntry;
import bigchadguys.dailyshop.world.roll.IntRoll;
import net.minecraft.item.Items;

import java.lang.reflect.Type;

public class TradeTablesConfig extends RegistryConfig<TableTradeEntry> {

    @Override
    public String getFolder() {
        return "trade_tables";
    }

    public void validate(String path) {
        this.getAll().forEach((key, entry) -> {
            entry.validate(path + "." + key);
        });
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
        this.put("saplings", new TableTradeEntry(IntRoll.ofConstant(1),
            new PoolTradeEntry()
                    .add(new DirectTradeEntry(null)
                        .addInput(1, PartialItem.of(Items.EMERALD), IntRoll.ofConstant(10), 1)
                        .addInput(2, PartialItem.of(Items.DIAMOND), IntRoll.ofConstant(10), 1)
                        .addInput(3, PartialItem.of(Items.IRON_INGOT), IntRoll.ofConstant(10), 1)
                        .addOutput(Items.OAK_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SPRUCE_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.BIRCH_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.JUNGLE_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.ACACIA_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.CHERRY_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.DARK_OAK_SAPLING, null, IntRoll.ofConstant(1), 1), 1)
        ));

        this.put("seeds", new TableTradeEntry(IntRoll.ofConstant(1),
                new PoolTradeEntry()
                    .add(new DirectTradeEntry(null)
                        .addInput(1, PartialItem.of(Items.EMERALD), IntRoll.ofConstant(10), 1)
                        .addInput(2, PartialItem.of(Items.DIAMOND), IntRoll.ofConstant(10), 1)
                        .addInput(3, PartialItem.of(Items.IRON_INGOT), IntRoll.ofConstant(10), 1)
                        .addOutput(Items.WHEAT_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.PUMPKIN_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.MELON_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.TORCHFLOWER_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.BEETROOT_SEEDS, null, IntRoll.ofConstant(1), 1), 1)
        ));

        this.put("spawn_eggs", new TableTradeEntry(IntRoll.ofConstant(1),
                new PoolTradeEntry()
                    .add(new DirectTradeEntry(null)
                        .addInput(1, PartialItem.of(Items.EMERALD), IntRoll.ofConstant(10), 1)
                        .addInput(2, PartialItem.of(Items.DIAMOND), IntRoll.ofConstant(10), 1)
                        .addInput(3, PartialItem.of(Items.IRON_INGOT), IntRoll.ofConstant(10), 1)
                        .addOutput(Items.BLAZE_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.AXOLOTL_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.FROG_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.CHICKEN_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.COW_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.CREEPER_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.DROWNED_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.ENDERMAN_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.EVOKER_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.GHAST_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.GUARDIAN_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.HOGLIN_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.HUSK_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.MAGMA_CUBE_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.PILLAGER_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.PIGLIN_BRUTE_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.PIGLIN_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.PIG_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SHEEP_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SHULKER_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SKELETON_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SLIME_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.STRAY_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SQUID_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SPIDER_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.VINDICATOR_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.WITCH_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.WITHER_SKELETON_SPAWN_EGG, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.ZOMBIFIED_PIGLIN_SPAWN_EGG, null, IntRoll.ofConstant(1), 1), 1)
        ));

        this.put("daily_shop", new TableTradeEntry(IntRoll.ofConstant(60),
                new PoolTradeEntry()
                    .add(new ReferenceTradeEntry("saplings"), 1)
                    .add(new ReferenceTradeEntry("seeds"), 1)
                    .add(new ReferenceTradeEntry("spawn_eggs"), 1)
        ));
    }

    @Override
    public <C extends Config> C read() {
        C config = super.read();
        ModConfigs.POST_LOAD.add(() -> ((TradeTablesConfig)config).validate(this.getFolder()));
        return config;
    }

}
