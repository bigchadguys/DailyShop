package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.bit.BitBuffer;
import bigchadguys.dailyshop.data.item.*;
import bigchadguys.dailyshop.data.serializable.ISerializable;
import bigchadguys.dailyshop.data.tile.OrItemPredicate;
import bigchadguys.dailyshop.init.ModBlocks;
import bigchadguys.dailyshop.init.ModConfigs;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Trade implements ISerializable<NbtCompound, JsonObject> {

    public static final Trade ERROR = new Trade(
            new Input(PartialItem.of(ModBlocks.ERROR.get().asItem()), 1),
            new Input(PartialItem.of(ModBlocks.ERROR.get().asItem()), 1),
            new Input(PartialItem.of(ModBlocks.ERROR.get().asItem()), 1),
            new ItemStack(ModBlocks.ERROR.get().asItem()), 0, 0);

    private final Input input1;
    private final Input input2;
    private final Input input3;
    private ItemStack output;
    private int currentTrades;
    private int maximumTrades;

    public Trade() {
        this.input1 = new Input();
        this.input2 = new Input();
        this.input3 = new Input();
    }

    public Trade(Input input1, Input input2, Input input3, ItemStack output, int currentTrades, int maximumTrades) {
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.output = output;
        this.currentTrades = currentTrades;
        this.maximumTrades = maximumTrades;
    }

    public Input getInput(int index) {
        return switch(index) {
            case 1 -> this.input1;
            case 2 -> this.input2;
            case 3 -> this.input3;
            default -> throw new IllegalArgumentException("Invalid index: " + index);
        };
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public boolean isAvailable() {
        return this.maximumTrades >= 0 && this.currentTrades < this.maximumTrades;
    }

    public void onTrade(int count) {
        this.currentTrades += count;
    }

    @Override
    public void writeBits(BitBuffer buffer) {
        this.input1.writeBits(buffer);
        this.input2.writeBits(buffer);
        this.input3.writeBits(buffer);
        Adapters.ITEM_STACK.writeBits(this.output, buffer);
        Adapters.INT_SEGMENTED_3.writeBits(this.currentTrades, buffer);
        Adapters.INT_SEGMENTED_3.writeBits(this.maximumTrades, buffer);
    }

    @Override
    public void readBits(BitBuffer buffer) {
        this.input1.readBits(buffer);
        this.input2.readBits(buffer);
        this.input3.readBits(buffer);
        this.output = Adapters.ITEM_STACK.readBits(buffer).orElseThrow();
        this.currentTrades = Adapters.INT_SEGMENTED_3.readBits(buffer).orElseThrow();
        this.maximumTrades = Adapters.INT_SEGMENTED_3.readBits(buffer).orElseThrow();
    }

    @Override
    public Optional<NbtCompound> writeNbt() {
        NbtCompound nbt = new NbtCompound();
        this.input1.writeNbt().ifPresent(value -> nbt.put("input1", value));
        this.input2.writeNbt().ifPresent(value -> nbt.put("input2", value));
        this.input3.writeNbt().ifPresent(value -> nbt.put("input3", value));
        Adapters.ITEM_STACK.writeNbt(this.output).ifPresent(value -> nbt.put("output", value));
        Adapters.INT_SEGMENTED_3.writeNbt(this.currentTrades).ifPresent(value -> nbt.put("currentTrades", value));
        Adapters.INT_SEGMENTED_3.writeNbt(this.maximumTrades).ifPresent(value -> nbt.put("maximumTrades", value));
        return Optional.of(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        this.input1.readNbt(nbt.getCompound("input1"));
        this.input2.readNbt(nbt.getCompound("input2"));
        this.input3.readNbt(nbt.getCompound("input3"));
        this.output = Adapters.ITEM_STACK.readNbt(nbt.get("output")).orElseThrow();
        this.currentTrades = Adapters.INT_SEGMENTED_3.readNbt(nbt.get("currentTrades")).orElseThrow();
        this.maximumTrades = Adapters.INT_SEGMENTED_3.readNbt(nbt.get("maximumTrades")).orElseThrow();
    }

    public static class Input implements ISerializable<NbtCompound, JsonObject> {
        private ItemPredicate filter;
        private int count;

        private List<Entry> cache;

        public Input() {

        }

        public Input(ItemPredicate filter, int count) {
            this.filter = filter;
            this.count = count;
        }

        public ItemPredicate getFilter() {
            return this.filter;
        }

        public int getCount() {
            return this.count;
        }

        public ItemStack getDisplay(double time) {
            if(this.cache == null) {
                this.cache = new ArrayList<>();
                this.iterate(this.filter, new NbtCompound(), this.cache);
            }

            if(this.cache.isEmpty()) {
                return new ItemStack(Items.AIR, this.count);
            }

            int index = (int)(time / 30.0D) % this.cache.size();
            return this.cache.get(index).toStack(this.count);
        }

        private void iterate(ItemPredicate filter, NbtCompound nbt, List<Entry> entries) {
            if(filter instanceof OrItemPredicate or) {
                for(ItemPredicate predicate : or.getChildren()) {
                    this.iterate(predicate, nbt, entries);
                }
            } else if(filter instanceof PartialItem item) {
                entries.add(new Entry(item.asWhole().orElse(ModBlocks.ERROR.get().asItem()), null));
            } else if(filter instanceof PartialItemGroup group) {
                for(ItemPredicate child : ModConfigs.ITEM_GROUPS.getGroup(group.getId())) {
                    NbtCompound copy = nbt.copy();
                    group.getNbt().asWhole().ifPresent(copy::copyFrom);
                    this.iterate(child, copy, entries);
                }
            } else if(filter instanceof PartialItemTag tag) {
                NbtCompound copy = nbt.copy();
                tag.getNbt().asWhole().ifPresent(copy::copyFrom);

                for(Item item : Registries.ITEM) {
                   if(Registries.ITEM.getEntry(item).streamTags().anyMatch(itemTagKey -> tag.getId().equals(itemTagKey.id()))) {
                       entries.add(new Entry(item, copy));
                   }
                }
            } else if(filter instanceof PartialStack stack) {
                stack.generate(1).ifPresent(_stack -> entries.add(new Entry(_stack.getItem(), _stack.getNbt())));
            }
        }

        @Override
        public void writeBits(BitBuffer buffer) {
            Adapters.ITEM_PREDICATE.writeBits(this.filter, buffer);
            Adapters.INT_SEGMENTED_3.writeBits(this.count, buffer);
        }

        @Override
        public void readBits(BitBuffer buffer) {
            this.filter = Adapters.ITEM_PREDICATE.readBits(buffer).orElseThrow();
            this.count = Adapters.INT_SEGMENTED_3.readBits(buffer).orElseThrow();
        }

        @Override
        public Optional<NbtCompound> writeNbt() {
            NbtCompound nbt = new NbtCompound();
            Adapters.ITEM_PREDICATE.writeNbt(this.filter).ifPresent(value -> nbt.put("filter", value));
            Adapters.INT_SEGMENTED_3.writeNbt(this.count).ifPresent(value -> nbt.put("count", value));
            return Optional.of(nbt);
        }

        @Override
        public void readNbt(NbtCompound nbt) {
            this.filter = Adapters.ITEM_PREDICATE.readNbt(nbt.get("filter")).orElseThrow();
            this.count = Adapters.INT_SEGMENTED_3.readNbt(nbt.get("count")).orElseThrow();
        }

        private record Entry(Item item, NbtCompound nbt) {
            public ItemStack toStack(int count) {
                ItemStack stack = new ItemStack(this.item, count);
                stack.setNbt(this.nbt);
                return stack;
            }
        }
    }

}
