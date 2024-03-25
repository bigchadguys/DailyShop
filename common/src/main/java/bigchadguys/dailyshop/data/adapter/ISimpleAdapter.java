package bigchadguys.dailyshop.data.adapter;

import bigchadguys.dailyshop.data.bit.BitBuffer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonSerializationContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NbtElement;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

public interface ISimpleAdapter<T, N extends NbtElement, J extends JsonElement> extends IAdapter<T, N, J, Object> {

    default void writeBits(T value, BitBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    default Optional<T> readBits(BitBuffer buffer) {
        throw new UnsupportedOperationException();
    }

    default void writeBytes(T value, ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    default Optional<T> readBytes(ByteBuf buffer) {
        throw new UnsupportedOperationException();
    }

    default void writeData(T value, DataOutput data) throws IOException {
        throw new UnsupportedOperationException();
    }

    default Optional<T> readData(DataInput data) throws IOException {
        throw new UnsupportedOperationException();
    }

    default Optional<N> writeNbt(T value) {
        throw new UnsupportedOperationException();
    }

    default Optional<T> readNbt(N nbt) {
        throw new UnsupportedOperationException();
    }

    default Optional<J> writeJson(T value) {
        throw new UnsupportedOperationException();
    }

    default Optional<T> readJson(J json) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void writeBits(T value, BitBuffer buffer, Object context) {
        this.writeBits(value, buffer);
    }

    @Override
    default Optional<T> readBits(BitBuffer buffer, Object context) {
        return this.readBits(buffer);
    }

    @Override
    default void writeBytes(T value, ByteBuf buffer, Object context) {
        this.writeBytes(value, buffer);
    }

    @Override
    default Optional<T> readBytes(ByteBuf buffer, Object context) {
        return this.readBytes(buffer);
    }

    @Override
    default void writeData(T value, DataOutput data, Object context) throws IOException {
        this.writeData(value, data);
    }

    @Override
    default Optional<T> readData(DataInput data, Object context) throws IOException {
        return this.readData(data);
    }

    @Override
    default Optional<N> writeNbt(T value, Object context) {
        return this.writeNbt(value);
    }

    @Override
    default Optional<T> readNbt(N nbt, Object context) {
        return this.readNbt(nbt);
    }

    @Override
    default Optional<J> writeJson(T value, Object context) {
        return this.writeJson(value);
    }

    @Override
    default Optional<T> readJson(J json, Object context) {
        return this.readJson(json);
    }

    @Override
    default JsonElement serialize(T value, Type source, JsonSerializationContext context) {
        return this.writeJson(value, null).map(json -> (JsonElement)json).orElse(JsonNull.INSTANCE);
    }

    @Override
    default T deserialize(JsonElement json, Type source, JsonDeserializationContext context) {
        return this.readJson((J)json, null).orElse(null);
    }

}
