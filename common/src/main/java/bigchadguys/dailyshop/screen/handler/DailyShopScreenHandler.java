package bigchadguys.dailyshop.screen.handler;

import bigchadguys.dailyshop.init.ModScreenHandlers;
import bigchadguys.dailyshop.trade.Shop;
import bigchadguys.dailyshop.world.data.DailyShopData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class DailyShopScreenHandler extends ScreenHandler {

    private final Shop shop;
    private final Inventory shopInventory;

    public DailyShopScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buffer) {
        this(syncId, playerInventory, new SimpleInventory(27), DailyShopData.CLIENT.getShop());
    }

    public DailyShopScreenHandler(int syncId, PlayerInventory playerInventory, Inventory shopInventory, Shop shop) {
        super(ModScreenHandlers.DAILY_SHOP.get(), syncId);
        this.shop = shop;
        ScreenHandler.checkSize(shopInventory, 27);
        this.shopInventory = shopInventory;

        for(int x = 0; x < 9; x++) {
            for(int y = 0; y < 3; y++) {
                this.addSlot(new Slot(shopInventory, x + y * 9, 108 + x * 18 - 1, 18 + y * 18 - 1));
            }
        }

        for(int x = 0; x < 9; x++) {
            for(int y = 0; y < 3; y++) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 108 + x * 18 - 1, 84 + y * 18 - 1));
            }
        }

        for(int x = 0; x < 9; x++) {
            this.addSlot(new Slot(playerInventory, x, 108 + x * 18 - 1, 142 - 1));
        }

        this.shopInventory.onOpen(playerInventory.player);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.shopInventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

        if(!slot.hasStack()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getStack();
        ItemStack copy = stack.copy();

        if(slotIndex < this.shopInventory.size()) {
            if(!this.insertItem(stack, this.shopInventory.size(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if(!this.insertItem(stack, 0, this.shopInventory.size(), false)) {
                return ItemStack.EMPTY;
            }
        }

        if(stack.isEmpty()) {
            slot.setStack(ItemStack.EMPTY);
        } else {
            slot.markDirty();
        }

        return copy;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.shopInventory.onClose(player);
        this.shop.removeChangeListener(this);
    }

}
