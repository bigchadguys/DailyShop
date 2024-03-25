package bigchadguys.dailyshop.config;

import bigchadguys.dailyshop.data.nbt.PartialCompoundNbt;
import bigchadguys.dailyshop.data.tile.PartialBlockState;
import bigchadguys.dailyshop.data.tile.PartialTile;
import bigchadguys.dailyshop.data.tile.TilePredicate;
import com.google.gson.annotations.Expose;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TileGroupsConfig extends FileConfig {

    @Expose private Map<Identifier, Set<TilePredicate>> groups;

    @Override
    public String getPath() {
        return "tile_groups";
    }

    public boolean isInGroup(Identifier groupId, PartialBlockState state, PartialCompoundNbt nbt) {
        for(TilePredicate predicate : this.groups.getOrDefault(groupId, new HashSet<>())) {
            if(predicate.test(state, nbt)) {
                return true;
            }
        }

        return false;
    }

    public boolean isInGroup(Identifier groupId, PartialTile tile) {
        return this.isInGroup(groupId, tile.getState(), tile.getEntity());
    }

    @Override
    protected void reset() {
        this.groups = new LinkedHashMap<>();
    }

}
