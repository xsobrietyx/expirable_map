package custom.expirable.map;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by xsobrietyx on 25-September-2019 time 16:23
 */
public class ExpirableMapTest {

    private ExpirableMap<Integer, String> testableCache;

    @Before
    public void setup(){
        testableCache = new ExpirableHashMap();
        testableCache.setTTL(1000L);
    }

    @Test
    public void testSimplePassCase(){
        String res = testableCache.put(1, "A");

        assertEquals("", res);
        assertEquals("A", testableCache.get(1));
    }

    @Test
    public void testNotPassingCaseWithDelay() throws InterruptedException {
        testableCache.put(1, "A");

        Thread.sleep(1100L);

        assertEquals("", testableCache.get(1));
    }

    @Test
    public void testPassingCaseWithDelay() throws InterruptedException {
        testableCache.put(1, "A");

        Thread.sleep(900L);

        assertEquals("A", testableCache.get(1));
    }

    @Test
    public void testCacheErasing(){
        testableCache.put(1, "A");

        testableCache.clean();

        assertEquals(0, testableCache.getSize());
    }

}
