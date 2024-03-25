package bigchadguys.dailyshop.data.serializable;

import bigchadguys.dailyshop.data.bit.BitBuffer;

public interface IBitSerializable {

	void writeBits(BitBuffer buffer);

	void readBits(BitBuffer buffer);

}
