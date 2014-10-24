package nevik;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class IndexedSetViewTest {

	@Test
	public void testEqualsAndHashCode() throws Exception {
		final List<String> l1 = Arrays.asList("a", "b", "c");
		final List<String> l2 = new ArrayList<>();
		l2.add("a");
		l2.add("b");
		l2.add("c");
		final List<String> l3 = Arrays.asList("a", "b", "d");

		if (!l1.equals(l1) || !l1.equals(l2) || l1.equals(l3))
			throw new IllegalArgumentException("List's equals() does not work as expected, something is wrong!");

		final IndexedSetView<String> isv1 = new IndexedSetView<>(l1);
		final IndexedSetView<String> isv2 = new IndexedSetView<>(l2);
		final IndexedSetView<String> isv3 = new IndexedSetView<>(l3);

		assertTrue(isv1.equals(isv1));
		assertTrue(isv2.equals(isv2));
		assertTrue(isv3.equals(isv3));

		assertTrue(isv1.equals(isv2));
		assertTrue(isv2.equals(isv1));

		assertFalse(isv1.equals(isv3));
		assertFalse(isv3.equals(isv1));
		assertFalse(isv2.equals(isv3));
		assertFalse(isv3.equals(isv2));

		for (final IndexedSetView<?> isv : new IndexedSetView<?>[]{isv1, isv2, isv3}){
			for (final List<?> l : new List<?>[]{l1, l2, l3})
				assertFalse(isv + " equals " + l + " but should not", isv.equals(l));
		}

		assertEquals(isv1.hashCode(), isv2.hashCode());
		assertNotEquals(isv1.hashCode(), isv3.hashCode());

		for (final IndexedSetView<?> isv : new IndexedSetView<?>[]{isv1, isv2, isv3}){
			for (final List<?> l : new List<?>[]{l1, l2, l3})
				assertNotEquals(isv.hashCode(), l.hashCode());
		}
	}
}