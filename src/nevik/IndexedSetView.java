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

import java.util.*;

/**
 * Created by Patrick Lehner on 2014-10-23.
 */
public class IndexedSetView<T> implements Iterable<T> {

	private final Set<T> itemSet;
	private final Map<T, Integer> itemMap;
	private final List<T> itemList;

	private final int size;

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
	 * @return
	 */
	@Override
	public Iterator<T> iterator() {
		return this.itemList.iterator();
	}

	/**
	 * Get an array view of the items in this indexed set, in the proper order. The array is a copy and not directly
	 * backed by this set, so changed to the array (i.e. the references it contains) have no effect on this set
	 * (however, changes to the referenced items themselves are naturally reflected in this set).
	 *
	 * @return
	 */
	public T[] asArray() {
		return (T[]) this.itemList.toArray();
	}

	/**
	 * <p>Get an unmodifiable list view of the items in this indexed set, in the proper order. Any attempt to modify the
	 * returned list results in an exception (see {@link java.util.Collections#unmodifiableList(java.util.List)}).</p>
	 *
	 * <p>The returned list is a random-access list (it implements {@link java.util.RandomAccess}), i.e. index-based
	 * access is possible in constant time.</p>
	 *
	 * @return
	 */
	public List<T> asList() {
		return this.itemList;
	}

	public Set<T> asSet() {
		return this.itemSet;
	}

	/**
	 * Get an unmodifiable map which connects each item in this indexed set to its index. Any attempt to modify the
	 * returned map results in an exception (see {@link java.util.Collections#unmodifiableMap(java.util.Map)}).
	 * @return
	 */
	public Map<T, Integer> getIndexMap() {
		return this.itemMap;
	}

	/**
	 * Get the index for an element of this indexed set. If the given argument is not an element of this set, -1 is
	 * returned.
	 *
	 * @param t the element whose index to retrieve; passing <code>null</code> causes a {@link java.lang.NullPointerException}
	 * @return the index of <code>t</code> in this indexed set, or -1 if <code>t</code> is not a member of this set
	 * @throws java.lang.NullPointerException if <code>t</code> is <code>null</code>
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
	 * @param t the element whose index to retrieve; passing <code>null</code> causes a {@link java.lang.NullPointerException}
	 * @return the index of <code>t</code> in this indexed set (if <code>t</code> is not a member of this set, an exception is thrown
	 * @throws java.lang.NullPointerException if <code>t</code> is <code>null</code> or if it is not an element of this set
	 *
	 * @see #getIndex(Object)
	 */
	public int getIndexEx(final T t) {
		return this.itemMap.get(t);
	}

	/**
	 *
	 * @param index
	 * @return
	 * @throws java.lang.IndexOutOfBoundsException if <code>index</code> is out of range (i.e. iff
	 *      <code>index < 0 || index >= {@link #getSize()}</code>)
	 */
	public T getElement(final int index) {
		return this.itemList.get(index);
	}

	public int getSize() {
		return this.size;
	}

	public BitSet getEmptyBitSet() {
		final BitSet bs = new BitSet(this.size);
		bs.clear(0, this.size-1);
		return bs;
	}

	public BitSet getFullBitSet() {
		final BitSet bs = new BitSet(this.size);
		bs.set(0, this.size - 1);
		return bs;
	}

	public BitSet getSingleBitSet(final int index) {
		if (index < 0 || index >= this.size)
			throw new IndexOutOfBoundsException("Index: " + index + "; Size: " + this.size);
		final BitSet bs = this.getEmptyBitSet();
		bs.set(index);
		return bs;
	}

	public BitSet getSingleBitSet(final T element) {
		return this.getSingleBitSet(this.getIndexEx(element));
	}

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

	@Override
	public int hashCode() {
		int result = itemList.hashCode();
		result = 31 * result + size;
		return result;
	}
}
