package cec.kolobokeCollections;

import com.koloboke.collect.map.hash.HashObjObjMap;
import com.koloboke.compile.KolobokeMap;
import com.koloboke.compile.NullKeyAllowed;

import static javafx.scene.input.KeyCode.H;

/**
 * Created by Mateusz Reczkowski on 13.06.2016.
 */
@KolobokeMap
@NullKeyAllowed
abstract class StringHashDoubleMap implements HashObjObjMap<String, ExtendedMap<String,String>> {
    static StringHashDoubleMap withExpectedSize(int expectedSize) {
        return new KolobokeStringHashDoubleMap(expectedSize);
    }
}