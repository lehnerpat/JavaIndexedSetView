/*****************************************************************************
 *
 * Copyright (c) 2014, Patrick Lehner <lehner (dot) patrick (at) gmx (dot) de>
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 *
 *****************************************************************************/

package nevik;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Patrick Lehner on 2014-10-23.
 * 
 * @author Patrick Lehner
 */
public class IndexedSetView<T> implements Iterable<T> {

	private final Set<T> itemSet;
	private final Map<T, Integer> itemMap;
	private final List<T> itemList;

	private final int size;

	/**
	 * Create a new indexed set view of the given collection. The collection may include duplicates, which will be trimmed (the collection is
	 * first passed into a Set implementation of the Java standard library to do this, and then converted to a list to derive an index for each
	 * element). Therefore, if you pass an ordered collection into this constructor (e.g. any list), the order of items in this set <b>is likely
	 * to be different</b> from the input list!
	 * 
	 * @param items
	 *            the collection of items to be included in this indexed set
	 */
	public IndexedSetView(final Collection<T> items) {
		this.itemSet = Collections.unmodifiableSet(new HashSet<>(items));
		this.size = itemSet.size();
		this.itemList = Collections.unmodifiableList(new ArrayList<>(this.itemSet));

		final HashMap<T, Integer> map = new HashMap<>(this.size);
		for (int i = 0; i < this.size; ++i)
			map.put(this.itemList.get(i), i);
		this.itemMap = Collections.unmodifiableMap(map);
	}


	/**
	 * Get an iterator over the items in this indexed set, in the order of increasing indices
	 * 
	 * @return
	 */
	@Override
	public Iterator<T> iterator() {
		return this.itemList.iterator();
	}

	/**
	 * Get an array view of the items in this indexed set, in the proper order. The array is a copy and not directly backed by this set, so
	 * changed to the array (i.e. the references it contains) have no effect on this set (however, changes to the referenced items themselves are
	 * naturally reflected in this set).
	 * 
	 * @return
	 */
	public T[] asArray() {
		return (T[]) this.itemList.toArray();
	}

	/**
	 * <p>
	 * Get an unmodifiable list view of the items in this indexed set, in the proper order. Any attempt to modify the returned list results in an
	 * exception (see {@link java.util.Collections#unmodifiableList(java.util.List)}).
	 * </p>
	 * 
	 * <p>
	 * The returned list is a random-access list (it implements {@link java.util.RandomAccess}), i.e. index-based access is possible in constant
	 * time.
	 * </p>
	 * 
	 * @return
	 */
	public List<T> asList() {
		return this.itemList;
	}

	/**
	 * Get an unmodifiable set view of the items in this indexed set. Any attempt to modify the returned set results in an exception (see
	 * {@link java.util.Collections#unmodifiableSet(Set)}).
	 * 
	 * @return
	 */
	public Set<T> asSet() {
		return this.itemSet;
	}

	/**
	 * Get an unmodifiable map which connects each item in this indexed set to its index. Any attempt to modify the returned map results in an
	 * exception (see {@link java.util.Collections#unmodifiableMap(java.util.Map)}).
	 * 
	 * @return
	 */
	public Map<T, Integer> getIndexMap() {
		return this.itemMap;
	}

	/**
	 * Get the index for an element of this indexed set. If the given argument is not an element of this set, -1 is returned.
	 * 
	 * @param t
	 *            the element whose index to retrieve; passing <code>null</code> causes a {@link java.lang.NullPointerException}
	 * @return the index of <code>t</code> in this indexed set, or -1 if <code>t</code> is not a member of this set
	 * @throws java.lang.NullPointerException
	 *             if <code>t</code> is <code>null</code>
	 * 
	 * @see #getIndexEx(Object)
	 */
	public int getIndex(final T t) {
		if (t == null)
			throw new NullPointerException();
		final Integer i = this.itemMap.get(t);
		if (i == null)
			return -1;
		return i;
	}

	/**
	 * Get the index for an element of this indexed set. If the given argument is not an element of this set, a
	 * {@link java.lang.NullPointerException} is thrown.
	 * 
	 * @param t
	 *            the element whose index to retrieve; passing <code>null</code> causes a {@link java.lang.NullPointerException}
	 * @return the index of <code>t</code> in this indexed set (if <code>t</code> is not a member of this set, an exception is thrown
	 * @throws java.lang.NullPointerException
	 *             if <code>t</code> is <code>null</code> or if it is not an element of this set
	 * 
	 * @see #getIndex(Object)
	 */
	public int getIndexEx(final T t) {
		return this.itemMap.get(t);
	}

	/**
	 * Get the element with the given index from this indexed set. Throws an exception if the given index is outside of the bounds of this set.
	 * 
	 * @param index
	 * @return
	 * @throws java.lang.IndexOutOfBoundsException
	 *             if <code>index</code> is out of range (i.e. iff <code>index < 0 || index >= {@link #getSize()}</code>)
	 */
	public T getElement(final int index) {
		return this.itemList.get(index);
	}

	/**
	 * Get the size of this set (the number of elements in it). This is also the size of all structures returned by {@link #asArray()},
	 * {@link #asList()}, {@link #asSet()} and {@link #getIndexMap()}.
	 * 
	 * @return
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Get a {@link BitSet} with the same size as this indexed set, and with all bits set to {@code false}.
	 * 
	 * @return
	 */
	public BitSet getEmptyBitSet() {
		final BitSet bs = new BitSet(this.size);
		bs.clear(0, this.size - 1);
		return bs;
	}

	/**
	 * Get a {@link BitSet} with the same size as this indexed set, and with all bits set to {@code true}.
	 * 
	 * @return
	 */
	public BitSet getFullBitSet() {
		final BitSet bs = new BitSet(this.size);
		bs.set(0, this.size - 1);
		return bs;
	}

	/**
	 * Get a {@link BitSet} with the same size as this indexed set, with a single bit set to {@code true} (the one with the specified
	 * <b>index</b>), and all other bits set to {@code false}.
	 * 
	 * @param index
	 * @return
	 */
	public BitSet getSingleBitSet(final int index) {
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException("Index: " + index + "; Size: " + this.size);
		final BitSet bs = this.getEmptyBitSet();
		bs.set(index);
		return bs;
	}

	/**
	 * Get a {@link BitSet} with the same size as this indexed set, with a single bit set to {@code true} (the one corresponding to the specified
	 * <b>element</b>), and all other bits set to {@code false}.
	 * 
	 * @param element
	 * @return
	 */
	public BitSet getSingleBitSet(final T element) {
		return this.getSingleBitSet(this.getIndexEx(element));
	}

	/**
	 * Determine if the given object is equal to this indexed set. Indexed sets are considered equal iff they contain exactly the same elements
	 * in exactly the same order. This is the same equality contract as that of {@link List}.
	 * 
	 * @see #hashCode()
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final IndexedSetView that = (IndexedSetView) o;
		return itemList.equals(that.itemList);
	}

	/**
	 * Get the hashcode for this indexed set view. The hashcode is currently based on the hashcode of the contained list of elements (see
	 * {@link List#hashCode()} and {@link ArrayList#hashCode()}. Therefore, the order of elements has an effect on the resulting hash code.
	 * However, the hashcode for this indexed set is modified so that a list containing the same elements and in the same order as this set will
	 * still have a different hashcode.
	 */
	@Override
	public int hashCode() {
		int result = itemList.hashCode();
		result = 31 * result + size;
		return result;
	}
}
