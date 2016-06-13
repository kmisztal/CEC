package cec.kolobokeCollections;

import com.koloboke.compile.KolobokeSet;
import com.koloboke.compile.NullKeyAllowed;

import java.util.Set;

/**
 * Created by mateusz on 13.06.2016.
 */
@KolobokeSet
abstract class MySet<E> implements Set<E> {
    public static <E> Set<E> withExpectedSize(int expectedSize) {
        return new KolobokeMySet<E>(expectedSize);
    }
}
