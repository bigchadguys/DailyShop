package bigchadguys.dailyshop.world.data;

import bigchadguys.dailyshop.data.serializable.INbtSerializable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.io.File;

public abstract class WorldData extends PersistentState implements INbtSerializable<NbtCompound> {

    @Override
    public final NbtCompound writeNbt(NbtCompound nbt) {
        return this.writeNbt().orElse(nbt);
    }

    @Override
    public void save(File file) {
        file.getParentFile().mkdirs();
        super.save(file);
    }

}
