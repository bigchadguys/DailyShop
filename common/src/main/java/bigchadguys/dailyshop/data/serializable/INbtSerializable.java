package bigchadguys.dailyshop.data.serializable;

import net.minecraft.nbt.NbtElement;

import java.util.Optional;

public interface INbtSerializable<N extends NbtElement> {

    Optional<N> writeNbt();

    void readNbt(N nbt);

}
