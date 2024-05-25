package bigchadguys.dailyshop.trade;

import bigchadguys.dailyshop.DailyShopMod;
import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.item.ItemPredicate;
import bigchadguys.dailyshop.data.serializable.IJsonSerializable;
import bigchadguys.dailyshop.init.ModBlocks;
import bigchadguys.dailyshop.util.WeightedList;
import bigchadguys.dailyshop.world.random.RandomSource;
import bigchadguys.dailyshop.world.roll.IntRoll;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class DirectTradeEntry extends TradeEntry {

    private final WeightedList<Input> input1;
    private final WeightedList<Input> input2;
    private final WeightedList<Input> input3;
    private final WeightedList<Output> output;
    private IntRoll trades;

    public DirectTradeEntry() {
        this.input1 = new WeightedList<>();
        this.input2 = new WeightedList<>();
        this.input3 = new WeightedList<>();
        this.output = new WeightedList<>();
    }

    public DirectTradeEntry(IntRoll trades) {
        this();
        this.trades = trades;
    }

    @Override
    public void validate(String path) {
        this.validateList(this.input1, (input, i) -> input.validate("%s.input1[%d]".formatted(path, i)));
        this.validateList(this.input2, (input, i) -> input.validate("%s.input2[%d]".formatted(path, i)));
        this.validateList(this.input3, (input, i) -> input.validate("%s.input3[%d]".formatted(path, i)));
        this.validateList(this.output, (output, i) -> output.validate("%s.output[%d]".formatted(path, i)));
    }

    private <T> void validateList(WeightedList<T> list, BiConsumer<T, Integer> action) {
        List<Map.Entry<T, Double>> entries = new ArrayList<>(list.entrySet());

        for(int i = 0; i < entries.size(); i++) {
           action.accept(entries.get(i).getKey(), i);
        }
    }

    public DirectTradeEntry addInput(int index, ItemPredicate filter, IntRoll count, double weight) {
        WeightedList<Input> input = switch(index) {
            case 1 -> this.input1;
            case 2 -> this.input2;
            case 3 -> this.input3;
            default -> throw new UnsupportedOperationException();
        };

        input.add(new Input(filter, count), weight);
        return this;
    }

    public DirectTradeEntry addOutput(Item item, NbtCompound nbt, IntRoll count, double weight) {
        this.output.add(new Output(item, nbt, count), weight);
        return this;
    }

    public DirectTradeEntry addOutput(String itemId, NbtCompound nbt, IntRoll count, double weight) {
        this.output.add(new Output(new Identifier(itemId), nbt, count), weight);
        return this;
    }

    @Override
    public Stream<Trade> generate(RandomSource random) {
        Input input1 = this.input1.getRandom(random).orElse(Input.AIR);
        Input input2 = this.input2.getRandom(random).orElse(Input.AIR);
        Input input3 = this.input3.getRandom(random).orElse(Input.AIR);
        Output output = this.output.getRandom(random).orElseGet(() -> new Output(ModBlocks.ERROR.get().asItem(), null, IntRoll.ofConstant(1)));

        return Stream.of(new Trade(input1.generate(random), input2.generate(random), input3.generate(random),
                output.generate(random), 0, this.trades == null ? -1 : this.trades.get(random)));
    }

    @Override
    public Optional<JsonElement> writeJson() {
        JsonObject json = new JsonObject();
        this.input1.writeJson(Input::writeJson).ifPresent(value -> json.add("input1", value));
        this.input2.writeJson(Input::writeJson).ifPresent(value -> json.add("input2", value));
        this.input3.writeJson(Input::writeJson).ifPresent(value -> json.add("input3", value));
        this.output.writeJson(Output::writeJson).ifPresent(value -> json.add("output", value));
        Adapters.INT_ROLL.writeJson(this.trades).ifPresent(value -> json.add("trades", value));
        return Optional.of(json);
    }

    @Override
    public void readJson(JsonElement json) {
        if(json instanceof JsonObject object) {
            this.input1.readJson(object.get("input1"), Input::new, Input::readJson);
            this.input2.readJson(object.get("input2"), Input::new, Input::readJson);
            this.input3.readJson(object.get("input3"), Input::new, Input::readJson);
            this.output.readJson(object.get("output"), Output::new, Output::readJson);
            this.trades = Adapters.INT_ROLL.readJson(object.get("trades")).orElse(null);
        }
    }

    public static class Input implements IJsonSerializable<JsonObject> {
        public static final Input AIR = new Input(ItemPredicate.TRUE, IntRoll.ofConstant(0));

        private ItemPredicate filter;
        private IntRoll count;

        public Input() {

        }

        public Input(ItemPredicate filter, IntRoll count) {
            this.filter = filter;
            this.count = count;
        }

        public Trade.Input generate(RandomSource random) {
            return new Trade.Input(this.filter, this.count.get(random));
        }

        public void validate(String path) {
            this.filter.validate(path);
        }

        @Override
        public Optional<JsonObject> writeJson() {
            JsonObject json = new JsonObject();
            Adapters.ITEM_PREDICATE.writeJson(this.filter).ifPresent(value -> json.add("filter", value));
            Adapters.INT_ROLL.writeJson(this.count).ifPresent(value -> json.add("count", value));
            return Optional.of(json);
        }

        @Override
        public void readJson(JsonObject json) {
            this.filter = Adapters.ITEM_PREDICATE.readJson(json.get("filter")).orElse(null);
            this.count = Adapters.INT_ROLL.readJson(json.get("count")).orElse(null);
        }
    }

    public static class Output implements IJsonSerializable<JsonObject> {
        private Identifier item;
        private NbtCompound nbt;
        private IntRoll count;

        public Output() {

        }

        public Output(Item item, NbtCompound nbt, IntRoll count) {
            this(Registries.ITEM.getId(item), nbt, count);
        }

        public Output(Identifier item, NbtCompound nbt, IntRoll count) {
            this.item = item;
            this.nbt = nbt;
            this.count = count;
        }

        public ItemStack generate(RandomSource random) {
            Item item = Registries.ITEM.getOrEmpty(this.item).orElse(ModBlocks.ERROR.get().asItem());
            ItemStack stack = new ItemStack(item, this.count.get(random));

            if(this.nbt != null) {
                stack.setNbt(this.nbt.copy());
            }

            return stack;
        }

        public void validate(String path) {
            if(Registries.ITEM.getOrEmpty(this.item).isEmpty()) {
                DailyShopMod.LOGGER.error("%s: Unregistered item <%s>".formatted(path, this.item));
            }
        }

        @Override
        public Optional<JsonObject> writeJson() {
            JsonObject json = new JsonObject();
            Adapters.IDENTIFIER.writeJson(this.item).ifPresent(value -> json.add("item", value));
            Adapters.COMPOUND_NBT.writeJson(this.nbt).ifPresent(value -> json.add("nbt", value));
            Adapters.INT_ROLL.writeJson(this.count).ifPresent(value -> json.add("count", value));
            return Optional.of(json);
        }

        @Override
        public void readJson(JsonObject json) {
            this.item = Adapters.IDENTIFIER.readJson(json.get("item")).orElse(null);
            this.nbt = Adapters.COMPOUND_NBT.readJson(json.get("nbt")).orElse(null);
            this.count = Adapters.INT_ROLL.readJson(json.get("count")).orElse(null);
        }
    }

}
