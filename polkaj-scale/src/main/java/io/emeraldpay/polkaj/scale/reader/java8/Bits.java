package io.emeraldpay.polkaj.scale.reader.java8;

import java.nio.ByteBuffer;

/**
 * Created by Anton Zhilenkov on 06/08/2022.
 */
public class Bits {

    public static int getInt(ByteBuffer bb, int bi, boolean bigEndian) {
        return bigEndian ? getIntB(bb, bi) : getIntL(bb, bi);
    }

    private static int getIntB(ByteBuffer bb, int bi) {
        return makeInt(
                HeapByteBuffer._get(bb, bi),
                HeapByteBuffer._get(bb, bi + 1),
                HeapByteBuffer._get(bb, bi + 2),
                HeapByteBuffer._get(bb, bi + 3)
        );
    }

    private static int getIntL(ByteBuffer bb, int bi) {
        return makeInt(
                HeapByteBuffer._get(bb, bi + 3),
                HeapByteBuffer._get(bb, bi + 2),
                HeapByteBuffer._get(bb, bi + 1),
                HeapByteBuffer._get(bb, bi)
        );
    }

    static private int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return (((b3) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) << 8) |
                ((b0 & 0xff)));
    }
}
