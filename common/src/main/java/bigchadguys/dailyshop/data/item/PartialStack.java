package bigchadguys.dailyshop.data.item;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.data.adapter.ISimpleAdapter;
import bigchadguys.dailyshop.data.nbt.PartialCompoundNbt;
import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Optional;

public class PartialStack implements ItemPlacement<PartialStack> {

    protected PartialItem item;
    protected PartialCompoundNbt nbt;

    protected PartialStack(PartialItem item, PartialCompoundNbt nbt) {
        this.item = item;
        this.nbt = nbt;
    }

    public static PartialStack of(PartialItem item, PartialCompoundNbt nbt) {
        return new PartialStack(item, nbt);
    }

    public static PartialStack of(ItemStack stack) {
        return new PartialStack(PartialItem.of(stack), PartialCompoundNbt.of(stack));
    }

    public PartialItem getItem() {
        return this.item;
    }

    public PartialCompoundNbt getNbt() {
        return this.nbt;
    }

    public void setItem(PartialItem item) {
        this.item = item;
    }

    public void setNbt(PartialCompoundNbt nbt) {
        this.nbt = nbt;
    }

    @Override
    public boolean isSubsetOf(PartialStack other) {
        if(!this.item.isSubsetOf(other.item)) return false;
        if(!this.nbt.isSubsetOf(other.nbt)) return false;
        return true;
    }

    @Override
    public boolean isSubsetOf(ItemStack stack) {
        if(!this.item.isSubsetOf(stack)) return false;
        if(!this.nbt.isSubsetOf(stack)) return false;
        return true;
    }

    @Override
    public void fillInto(PartialStack other) {
        this.item.fillInto(other.item);
        this.nbt.fillInto(other.nbt);
    }

    @Override
    public Optional<ItemStack> generate(int count) {
        return this.item.generate(count).map(stack -> {
            stack.setNbt(this.nbt.asWhole().orElse(null));
            return stack;
        });
    }

    @Override
    public boolean test(PartialItem item, PartialCompoundNbt nbt) {
        return this.isSubsetOf(PartialStack.of(item, nbt));
    }

    @Override
    public PartialStack copy() {
        return new PartialStack(this.item.copy(), this.nbt.copy());
    }

    @Override
    public String toString() {
        return this.item.toString() + this.nbt.toString();
    }

    public static class Adapter implements ISimpleAdapter<PartialStack, NbtElement, JsonElement> {
        @Override
        public Optional<NbtElement> writeNbt(PartialStack value) {
            if(value == null) {
                return Optional.empty();
            }

            NbtCompound nbt = new NbtCompound();
            Adapters.PARTIAL_ITEM.writeNbt(value.item).ifPresent(tag -> nbt.put("item", tag));
            Adapters.PARTIAL_BLOCK_ENTITY.writeNbt(value.nbt).ifPresent(tag -> nbt.put("nbt", tag));
            return Optional.of(nbt);
        }

        @Override
        public Optional<PartialStack> readNbt(NbtElement nbt) {
            if(nbt instanceof NbtCompound compound) {
                PartialItem item = Adapters.PARTIAL_ITEM.readNbt(compound.get("item")).orElseThrow();
                PartialCompoundNbt tag = Adapters.PARTIAL_BLOCK_ENTITY.readNbt(compound.get("nbt")).orElseGet(PartialCompoundNbt::empty);
                return Optional.of(PartialStack.of(item, tag));
            }

            return Optional.empty();
        }
    }

    public static Optional<PartialStack> parse(String string, boolean logErrors) {
        try {
            return Optional.of(parse(new StringReader(string)));
        } catch(CommandSyntaxException | IllegalArgumentException e) {
            if(logErrors) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }

    public static PartialStack parse(String string) throws CommandSyntaxException {
        return parse(new StringReader(string));
    }

    public static PartialStack parse(StringReader reader) throws CommandSyntaxException {
        return PartialStack.of(PartialItem.parse(reader), PartialCompoundNbt.parse(reader));
    }

}
