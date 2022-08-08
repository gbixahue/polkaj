package io.emeraldpay.polkaj.scale.reader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.emeraldpay.polkaj.scale.ScaleCodecReader;
import io.emeraldpay.polkaj.scale.ScaleReader;
import io.emeraldpay.polkaj.scale.reader.java8.Bits;
import io.emeraldpay.polkaj.scale.reader.java8.HeapByteBuffer;

/**
 * Read Java Integer encoded as 4 byte SCALE value. Please note that since Java Integer is signed type, it may
 * read negative values for some of the byte representations (i.e. when highest bit is set to 1). If you expect
 * to read positive numbers for all of the possible range, you should use Uint32Reader, which returns Long values.
 *
 * @see UInt32Reader
 */
public class Int32Reader implements ScaleReader<Integer> {

    @Override
    public Integer read(ScaleCodecReader rdr) {
        ByteBuffer buf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        buf.put(rdr.readByte());
        buf.put(rdr.readByte());
        buf.put(rdr.readByte());
        buf.put(rdr.readByte());

        // this part is converted to java8
        // buf.flip().getInt();
        buf.flip();
        int nextIndex = HeapByteBuffer.nextGetIndex(buf, 4);
        int ix = HeapByteBuffer.ix(buf, nextIndex);
        return Bits.getInt(buf, ix, false);
    }
}
