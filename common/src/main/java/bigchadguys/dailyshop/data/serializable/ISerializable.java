package bigchadguys.dailyshop.data.serializable;

import bigchadguys.dailyshop.data.bit.BitBuffer;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;

import net.minecraft.nbt.NbtElement;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Optional;

public interface ISerializable<N extends NbtElement, J extends JsonElement>
    extends IBitSerializable, IByteSerializable, IDataSerializable, INbtSerializable<N>, IJsonSerializable<J> {

    @Override
    default void writeBits(BitBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void readBits(BitBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void writeBytes(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void readBytes(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void writeData(DataOutput data) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void readData(DataInput data) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    default Optional<N> writeNbt() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void readNbt(N nbt) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Optional<J> writeJson() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void readJson(J json) {
        throw new UnsupportedOperationException();
    }

}
