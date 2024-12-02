package bigchadguys.dailyshop.item;

import bigchadguys.dailyshop.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.Optional;

public class DailyShopItem extends BlockItem {

    public DailyShopItem(Block block) {
        super(block, new Settings());
    }

    public static ItemStack create(String id) {
        ItemStack stack = new ItemStack(ModBlocks.DAILY_SHOP.get());
        DailyShopItem.setId(stack, id);
        return stack;
    }

    public static Optional<String> getId(ItemStack stack) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);
        if(nbt == null) return Optional.empty();
        String id = nbt.contains("id", NbtElement.STRING_TYPE) ? nbt.getString("id") : null;
        return Optional.ofNullable(id);
    }

    public static void setId(ItemStack stack, String id) {
        NbtCompound nbt = BlockItem.getBlockEntityNbt(stack);

        if(id == null) {
            if(nbt != null) {
                nbt.remove("id");
            }
        } else {
            nbt = new NbtCompound();
            nbt.putString("id", id);
        }

        if(nbt != null) {
            BlockItem.setBlockEntityNbt(stack, ModBlocks.Entities.DAILY_SHOP.get(), nbt);
        }
    }

}
