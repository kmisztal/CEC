package cec.kolobokeCollections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mateusz Reczkowski on 12.06.2016.
 */
public class Test {

    public static void main(String[] args) {
        Map<String, String> map = MyMap.withExpectedSize(10);
        map.put("adam","kowalski");
        map.put("jan","nowak");
        map.put(null,"null");
        System.out.println(map.entrySet());

        Set<String> set = MySet.withExpectedSize(10);
        set.add("kasia");
        set.add("mariola");
        System.out.println(set.toString());

        ExtendedMap<String,String> map2 = ExtendedMap.withExpectedSize(10);
        map2.ensureCapacity(10000000);
        map2.put("stas","kozlowski");
        map2.put("janek","kras");
        for (int i = 0; i < 1000; i++) {
            map2.put("tomek"+(char)i,"kwiek"+(char)i);
        }
        map2.shrink();
        System.out.println(map2.entrySet());

        OptimizedMap<String> map3 = OptimizedMap.withExpectedSize(10);
        map3.justPut("apples", 10);
        map3.justPut("oranges", 20);
        map3.justPut("kiwi",30);
        System.out.println(map3.toString());
        map3.justRemove("apples");
        System.out.println(map3.toString());

        StringHashDoubleMap stringHashDoubleMap = StringHashDoubleMap.withExpectedSize(20000);
        long a = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            stringHashDoubleMap.put("jan"+(char)i,map2);
        }
        long b = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            System.out.println(stringHashDoubleMap.get("jan"+(char)i).get("tomek"+(char)i));
        }
        long c = b-a;
        System.out.println("Time elapsed: ");
        System.out.println((double)c / 1000000000.0);

        OptimizedMap<Integer> zz = OptimizedMap.withExpectedSize(100000);
        long a2 = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            zz.justPut(i,i);
        }
        long b2 = System.nanoTime();
        long c2 = b2-a2;

        Map<Integer,Integer> zz2 = new HashMap<>();
        long a3 = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            zz2.put(i,i);
        }
        long b3 = System.nanoTime();
        long c3 = b3-a3;

        System.out.println("Time elapsed for optimized map:");
        System.out.println((double)c2 / 1000000000.0);
        System.out.println("Time elapsed for plain map:");
        System.out.println((double)c3 / 1000000000.0);


    }
}
