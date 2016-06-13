package cec.kolobokeCollections;

import com.koloboke.compile.KolobokeMap;

/**
 * Created by mateusz on 13.06.2016.
 */
@KolobokeMap
public abstract class OptimizedMap<K> {

    static <K> OptimizedMap<K> withExpectedSize(int expectedSize) {
        return new KolobokeOptimizedMap<K>(expectedSize);
    }

    abstract void justPut(K key, int value);

    abstract int getInt(K key);

    abstract boolean justRemove(K key);
}
