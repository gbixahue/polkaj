package io.emeraldpay.polkaj.scale.reader.java8;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * Created by Anton Zhilenkov on 06/08/2022.
 */
public class HeapByteBuffer {

    public static int nextGetIndex(ByteBuffer bb, int nb) {
        int limit = bb.limit();
        int position = bb.position();
        if (limit - position < nb) throw new BufferUnderflowException();

        int p = position;
        bb.position(position + nb);

        return p;
    }

    public static int ix(ByteBuffer bb, int i) {
        return bb.arrayOffset() + i;
    }

    public static byte _get(ByteBuffer bb, int i) {
        return bb.array()[i];
    }
}