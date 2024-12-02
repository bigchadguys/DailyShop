package bigchadguys.dailyshop.block.entity;

import bigchadguys.dailyshop.data.adapter.Adapters;
import bigchadguys.dailyshop.init.ModBlocks;
import bigchadguys.dailyshop.init.ModWorldData;
import bigchadguys.dailyshop.screen.handler.DailyShopScreenHandler;
import bigchadguys.dailyshop.trade.EmptyShop;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.world.data.DailyShopData;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class DailyShopBlockEntity extends BaseBlockEntity implements SidedInventory, ExtendedMenuProvider {

    public static final int[] AVAILABLE_SLOTS = IntStream.range(0, 27).toArray();

    private String id;
    private final DefaultedList<ItemStack> inventory;

    public DailyShopBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlocks.Entities.DAILY_SHOP.get(), pos, state);
    }

    public DailyShopBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void tick(World world, BlockPos pos, BlockState state, DailyShopBlockEntity entity) {

    }

    @Override
    public void writeNbt(NbtCompound nbt, UpdateType type) {
        Adapters.UTF_8.writeNbt(this.id).ifPresent(tag -> nbt.put("shop", tag));
        NbtList inventory = new NbtList();

        for(int slotIndex = 0; slotIndex < this.inventory.size(); slotIndex++) {
            ItemStack stack = this.inventory.get(slotIndex);
            if(stack.isEmpty()) continue;

            NbtCompound entry = new NbtCompound();
            stack.writeNbt(entry).putByte("slot", (byte)slotIndex);
            inventory.add(entry);
        }

        if(!inventory.isEmpty()) {
            nbt.put("inventory", inventory);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt, UpdateType type) {
        this.id = Adapters.UTF_8.readNbt(nbt.get("shop")).orElse(null);
        this.inventory.clear();
        NbtList inventory = nbt.getList("inventory", NbtElement.COMPOUND_TYPE);

        for(int i = 0; i < inventory.size(); i++) {
            NbtCompound entry = inventory.getCompound(i);
            int slotIndex = Byte.toUnsignedInt(entry.getByte("slot"));
            if(slotIndex >= this.inventory.size()) continue;
            this.inventory.set(i, ItemStack.fromNbt(entry));
        }
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack stack = Inventories.splitStack(this.inventory, slot, amount);

        if(!stack.isEmpty()) {
            this.markDirty();
        }

        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);

        if(stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        this.markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return Inventory.canPlayerUse(this, player);
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.daily_shop");
    }

    @Override
    public void onOpen(PlayerEntity player) {
        DailyShopData data = ModWorldData.DAILY_SHOP.getGlobal(player.getWorld());
        data.onAcknowledge(this.id, player);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        Shop shop = ModWorldData.DAILY_SHOP.getGlobal(player.getWorld()).getShop(this.id).orElse(EmptyShop.INSTANCE);
        return new DailyShopScreenHandler(syncId, playerInventory, this, this.id, shop);
    }

    @Override
    public void saveExtraData(PacketByteBuf buffer) {
        buffer.writeBoolean(this.id != null);

        if(this.id != null) {
            buffer.writeString(this.id);
        }
    }

}
