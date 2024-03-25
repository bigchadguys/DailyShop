package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.data.item.ItemPredicate;
import bigchadguys.dailyshop.data.item.PartialItem;
import bigchadguys.dailyshop.data.item.PartialStack;
import bigchadguys.dailyshop.data.nbt.PartialCompoundNbt;
import com.google.gson.annotations.Expose;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ItemGroupsConfig extends FileConfig {

    @Expose private Map<Identifier, Set<ItemPredicate>> groups;

    @Override
    public String getPath() {
        return "item_groups";
    }

    public Set<ItemPredicate> getGroup(Identifier groupId) {
        return this.groups.getOrDefault(groupId, new HashSet<>());
    }

    public boolean isInGroup(Identifier groupId, PartialItem item, PartialCompoundNbt nbt) {
        for(ItemPredicate predicate : this.groups.getOrDefault(groupId, new HashSet<>())) {
            if(predicate.test(item, nbt)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInGroup(Identifier groupId, PartialStack stack) {
        return this.isInGroup(groupId, stack.getItem(), stack.getNbt());
    }

    @Override
    protected void reset() {
        this.groups = new LinkedHashMap<>();
    }

}
