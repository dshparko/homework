import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashMapTest {
    @Test
    void testPutAndGet() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);

        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
    }

    @Test
    void testOverwriteValue() {
        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value1");
        map.put("key", "value2");

        assertEquals("value2", map.get("key"));
    }

    @Test
    void testRemove() {
        HashMap<String, String> map = new HashMap<>();
        map.put("deleteMe", "bye");
        assertEquals("bye", map.get("deleteMe"));

        map.remove("deleteMe");
        assertNull(map.get("deleteMe"));
    }

    @Test
    void testNullKey() {
        HashMap<String, String> map = new HashMap<>();
        map.put(null, "nullValue");

        assertEquals("nullValue", map.get(null));
    }

    @Test
    void testResize() {
        HashMap<Integer, String> map = new HashMap<>(2);
        for (int i = 0; i < 20; i++) {
            map.put(i, "val" + i);
        }

        for (int i = 0; i < 20; i++) {
            assertEquals("val" + i, map.get(i));
        }
    }
}