package bigchadguys.dailyshop.data.tile;

import bigchadguys.dailyshop.data.item.PartialItem;
import bigchadguys.dailyshop.data.item.ItemPredicate;
import bigchadguys.dailyshop.data.nbt.PartialCompoundNbt;

import java.util.Arrays;

public class OrItemPredicate implements ItemPredicate {

    private ItemPredicate[] children;

    public OrItemPredicate(ItemPredicate... children) {
        this.children = children;
    }

    public ItemPredicate[] getChildren() {
        return this.children;
    }

    @Override
    public boolean test(PartialItem item, PartialCompoundNbt nbt) {
        for(ItemPredicate child : this.children) {
            if(child.test(item, nbt)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.children);
    }

}
