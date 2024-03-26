package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.data.item.ItemPredicate;
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
                        .addInput(1, ItemPredicate.of("clutter:silver_coin", true).orElseThrow(), IntRoll.ofConstant(10), 1)
                        .addOutput(Items.OAK_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.SPRUCE_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.BIRCH_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.JUNGLE_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.ACACIA_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.CHERRY_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.DARK_OAK_SAPLING, null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:mossy_glowshroom_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:pythadendron_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:cherry_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("clutter:kiwi_tree_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:hydralux_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:lucernia_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("clutter:kiwi_tree_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:umbrella_tree_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:tenanea_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:helix_tree_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:dragon_tree_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("betterend:lacugrove_sapling", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:apple_tree_sapling", null, IntRoll.ofConstant(1), 1), 1)
        ));

        this.put("seeds", new TableTradeEntry(IntRoll.ofConstant(1),
                new PoolTradeEntry()
                    .add(new DirectTradeEntry(null)
                        .addInput(1, ItemPredicate.of("clutter:silver_coin", true).orElseThrow(), IntRoll.ofConstant(25), 1)
                        .addOutput(Items.WHEAT_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.PUMPKIN_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.MELON_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.TORCHFLOWER_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput(Items.BEETROOT_SEEDS, null, IntRoll.ofConstant(1), 1)
                        .addOutput("bakery:strawberry_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("bakery:oat_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("candlelight:tomato_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("clutter:hops_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("clutter:thornbloom_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("clutter:kiwi_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:yellow_apricorn_seed", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:red_apricorn_seed", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:green_apricorn_seed", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:pink_apricorn_seed", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:blue_apricorn_seed", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:black_apricorn_seed", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:green_mint_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:cyan_mint_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:vivichoke_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:white_mint_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:pink_mint_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cobblemon:blue_mint_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("cozy:tomato_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:red_grape_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:white_grape_seeds", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:savanna_grape_seeds_red", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:savanna_grape_seeds_white", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:taiga_grape_seeds_red", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:taiga_grape_seeds_white", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:jungle_grape_seeds_red", null, IntRoll.ofConstant(1), 1)
                        .addOutput("vinery:jungle_grape_seeds_white", null, IntRoll.ofConstant(1), 1), 1)
        ));

        this.put("spawn_eggs", new TableTradeEntry(IntRoll.ofConstant(1),
                new PoolTradeEntry()
                    .add(new DirectTradeEntry(null)
                        .addInput(1, ItemPredicate.of("clutter:silver_coin", true).orElseThrow(), IntRoll.ofConstant(10), 1)
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

}
