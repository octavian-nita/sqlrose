package net.appfold.sqlrose.cache;

import org.junit.jupiter.api.*;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Octavian Theodor NITA (https://github.com/octavian-nita/)
 * @version 1.1, May 21, 2019
 */
class SoftCacheTest {

    protected SoftCache<String, String> cacheUnderTest;

    protected Function<String, String> computation;

    protected final int setUpBound = 2;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        cacheUnderTest = new SoftCache<>(setUpBound);
        computation = mock(Function.class);
    }

    @AfterEach
    public void tearDown() {
        cacheUnderTest = null;
        computation = null;
    }

    @Test
    public void newCache_BoundedAtN_SizeIs0AndMaxSizeIsN() {
        assertEquals(0, cacheUnderTest.size(), "Cache should store 0 initial entries (size)");
        assertEquals(setUpBound, cacheUnderTest.getMaxSize(), "Cache should have 2 available entries (max size)");
    }

    @Test
    public void getOrCompute_NewKeyGiven_ShouldComputeAndStoreValue() {
        final String arg = "arg";
        final String res = "res";

        when(computation.apply(arg)).thenReturn(res);
        cacheUnderTest.getOrCompute(arg, computation);

        verify(computation, description("getOrCompute() should invoke the computation once")).apply(arg);
        assertEquals(1, cacheUnderTest.size(), "Cache should store 1 entry");
        assertEquals(res, cacheUnderTest.get(arg), "Cache should store computed value");
    }

    @Test
    public void getOrCompute_KeyTwiceGiven_ShouldComputeOnlyOnce() {
        final String arg = "arg";
        final String res = "res";

        when(computation.apply(arg)).thenReturn(res);
        cacheUnderTest.getOrCompute(arg, computation);
        cacheUnderTest.getOrCompute(arg, computation);

        verify(computation, description("getOrCompute() should invoke the computation only once")).apply(arg);
        assertEquals(1, cacheUnderTest.size(), "Cache should store 1 entry");
        assertEquals(res, cacheUnderTest.get(arg), "Cache should store and retrieve computed value");
    }

    @Test
    public void getOrCompute_NewKeyGivenWhenFull_ShouldRemoveLRUEntry() {

        // Fixture

        String arg0 = "", argNMin1 = "", resNMin1 = "", argN = "", resN = "";
        for (int i = 0; i <= setUpBound; i++) {

            String arg = "arg-" + i, res = "res-" + i;
            when(computation.apply(arg)).thenReturn(res);

            switch (i) {
            case 0:
                arg0 = arg;
                break;
            case setUpBound - 1:
                argNMin1 = arg;
                resNMin1 = res;
                break;
            case setUpBound:
                argN = arg;
                resN = res;
                break;
            }
        }

        // Exercise

        for (int i = 0; i <= setUpBound; i++) {
            cacheUnderTest.getOrCompute("arg-" + i, computation);
        }

        // Assert / Verify

        assertEquals(setUpBound, cacheUnderTest.size(), "Cache should store " + setUpBound + " entries");
        assertTrue(cacheUnderTest.isFull(), "Cache should report as being full");

        assertFalse(cacheUnderTest.contains(arg0), "Cache should NOT contain first key given");
        assertNull(cacheUnderTest.get(arg0), "Cache should NOT store first computed value");

        assertTrue(cacheUnderTest.contains(argNMin1), "Cache should contain key N-1");
        assertEquals(resNMin1, cacheUnderTest.get(argNMin1), "Cache should store value N-1");

        assertTrue(cacheUnderTest.contains(argN), "Cache should contain last key given");
        assertEquals(resN, cacheUnderTest.get(argN), "Cache should store last computed value");
    }
}
