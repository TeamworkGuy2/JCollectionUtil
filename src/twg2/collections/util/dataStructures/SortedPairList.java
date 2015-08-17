package twg2.collections.util.dataStructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Map implementation which allows duplicate keys and values 
 * (HashMap and LinkedHashMap do not allow duplicate keys)
 * The insertion order is the iteration order.
 * Performance is similar to {@link ArrayList}.
 * This class provides a mixture of Map and List methods along with some custom methods, everything should 
 * be self explanatory.
 * This is basically a {@code List<Map.Entry<K, V>>} with the ability to store duplicate key-value pairs.
 */
public class SortedPairList<K, V> implements RandomAccessCollection<K>, PairCollection<K, V> {
	private List<K> keys; // List of Map keys
	private List<V> values; // List of Map values
	private List<K> keysIm; // Immutable copy of the keys
	private List<V> valuesIm; // Immutable copy of the values
	private Comparator<K> comparator;


	/** Create a pair list from a {@link Map} of keys and values.
	 * Note: changes to the map are not reflected in this pair list
	 * @param keyValues the map of keys and values to put in this pair list
	 */
	public SortedPairList(Map<? extends K, ? extends V> keyValues, Comparator<K> comparator) {
		this(comparator);
		for(Map.Entry<? extends K, ? extends V> entry : keyValues.entrySet()) {
			addPair(entry.getKey(), entry.getValue());
		}
	}


	/** Create a pair list from two collections of keys and values.
	 * Both collections must be the same size.
	 * Note: changes to the collection are not reflected in this pair list
	 * @param keys the keys to put in this pair list
	 * @param values the values to put in this pair list
	 */
	public SortedPairList(Collection<? extends K> keys, Collection<? extends V> values, Comparator<K> comparator) {
		this(comparator);
		if(keys == null || values == null || keys.size() != values.size()) {
			throw new IllegalArgumentException("the number of keys (" + (keys != null ? keys.size() : "null") + ") " +
					"does not equal the number of values (" + (values != null ? values.size() : "null"));
		}

		Iterator<? extends K> keyIter = keys.iterator();
		Iterator<? extends V> valIter = values.iterator();
		while(keyIter.hasNext() && valIter.hasNext()) {
			K key = keyIter.next();
			V value = valIter.next();
			addPair(key, value);
		}
	}


	/** Create a PairList with a default size of 10.
	 */
	public SortedPairList(Comparator<K> comparator) {
		this.comparator = comparator;
		this.keys = new ArrayList<K>(); // Initialize key List
		this.values = new ArrayList<V>(); // Initialize values List
		this.keysIm = Collections.unmodifiableList(this.keys);
		this.valuesIm = Collections.unmodifiableList(this.values);
	}


	/** clear, removes all key-value pairs from this instance
	 */
	@Override
	public void clear() {
		keys.clear();
		values.clear();
	}


	/** containsKey
	 * @param key Object to check for in this instance's list of keys
	 * @return true if this instance contains a key which equals the 'key' parameter
	 */
	@Override
	public boolean containsKey(K key) {
		if(keys.contains(key)) {
			return true;
		}
		return false;
	}


	/** containsValue
	 * @param value Object to check for in this instance's list of values
	 * @return true if this instance contains a value which equals the 'value' parameter
	 */
	@Override
	public boolean containsValue(V value) {
		if(values.contains(value)) {
			return true;
		}
		return false;
	}


	/** get, returns the value matching the first occurrence of the specified key
	 * @param key key who's corresponding value is to be returned
	 * @return the value which matches the 'key' parameter, returns null if the key does not exist
	 */
	@Override
	public V get(K key) {
		int keyIndex = keys.indexOf(key);

		if(keyIndex < 0) {
			return null;
		}
		else {
			return values.get(keyIndex);
		}
	}


	/** indexOf, returns the index of the specified key
	 * @param key the key who's index is to be returned
	 * @return the index where the specified key was found, or -1 if the key cannot be found
	 */
	public int indexOf(K key) {
		int index = keys.indexOf(key);

		if(index > 0) {
			return index;
		}
		else {
			return -1;
		}
	}


	/** returns the key corresponding to the index given,
	 * same as {@link #getKey(int)} for compatibility with {@link RandomAccessCollection}.
	 * @param index the index of the key to be returned
	 * @return the key found at the specified index
	 */
	@Override
	public K get(int index) {
		return getKey(index);
	}


	/** getKey, returns the key corresponding to the index given
	 * @param index the index of the key to be returned
	 * @return the key found at the specified index
	 */
	@Override
	public K getKey(int index) {
		if(index < 0 || index > this.size()-1) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		else {
			return keys.get(index);
		}
	}


	/** getValue, returns the value corresponding to the index given
	 * @param index the index of the value to be returned
	 * @return the value found at the specified index
	 */
	@Override
	public V getValue(int index) {
		if(index < 0 || index > this.size()-1) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		else {
			return values.get(index);
		}
	}


	/** isEmpty
	 * @return true if this PairList instance has no key-value associates, returns false otherwise
	 */
	@Override
	public boolean isEmpty() {
		boolean res = keys.size() == 0;
		return res;
	}


	/** keyList, (Replaces the purpose of keySet)
	 * @return the List of keys from this map 
	 */
	@Override
	public List<K> keyList() {
		return this.keysIm;
	}


	/** valueList
	 * @return the List of values from this map 
	 */
	@Override
	public List<V> valueList() {
		return this.valuesIm;
	}


	/** put
	 * Always returns null because duplicate keys are allowed so all key-value pair passed to this method
	 * are added
	 * @param key key to add to this PairList instance
	 * @param value value to add to this PairList instance
	 */
	@Override
	public V put(K key, V value) {
		int index = keys.indexOf(key);
		if(index > -1) {
			V val = values.get(index);
			keys.set(index, key);
			values.set(index, value);
			return val;
		}
		else {
			add(key, value);
			return null;
		}
	}


	@Override
	public void add(K key, V value) {
		addPair(key, value);
	}


	@Override
	public V put(Map.Entry<K, V> keyValue) {
		put(keyValue.getKey(), keyValue.getValue());
		return null;
	}


	@Override
	public void add(Map.Entry<K, V> keyValue) {
		add(keyValue.getKey(), keyValue.getValue());
	}
	


	/** putAll
	 * Adds all of the pairs in the mapPairs parameter to this PairList instance
	 * @param mapPairs map to add to this PairList instance
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> mapPairs) {
		Set<? extends Map.Entry<? extends K, ? extends V>> entrySet = mapPairs.entrySet();
		for(Map.Entry<? extends K, ? extends V> entry : entrySet) {
			addPair(entry.getKey(), entry.getValue());
		}
	}


	/** putAll
	 * Adds all of the pairs in the listPairs to this PairList instance
	 * @param listPairs pairList to add to this pairList
	 */
	@Override
	public void putAll(PairCollection<? extends K, ? extends V> listPairs) {
		for(int i = 0, size = listPairs.size(); i < size; i++) {
			addPair(listPairs.get(i), listPairs.getValue(i));
		}
	}


	/** remove
	 * @param key key to remove along with it's corresponding value
	 * @return the previous value associated with the deleted key
	 */
	@Override
	public V remove(Object key) {
		int index = keys.indexOf(key);
		if(index > -1) {
			V removedValue = values.get(index); // Temp value we are about to remove, used as return value
			removeIndex(index);
			return removedValue;
		}
		return null;
	}


	public void removeIndex(int index) {
		values.remove(index);
		keys.remove(index);
	}


	/** size
	 * @return the size of this PairList instance
	 */
	@Override
	public int size() {
		return keys.size();
	}


	/** values
	 * @return a Collection of this instance's values
	 */
	@Override
	public Collection<V> values() {
		return valuesIm;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(64);
		builder.append('[');
		if(keys.size() > 0) {
			int sizeTemp = keys.size() - 1;
			for(int i = 0; i < sizeTemp; i++) {
				builder.append(keys.get(i));
				builder.append('=');
				builder.append(values.get(i));
				builder.append(", ");
			}
			builder.append(keys.get(sizeTemp));
			builder.append('=');
			builder.append(values.get(sizeTemp));
		}
		builder.append(']');

		return builder.toString();
	}


	private void addPair(K key, V value) {
		int index = calcInsertIndex(key);
		if(index >= this.keys.size()) {
			this.keys.add(key);
			this.values.add(value);
		}
		else {
			this.keys.add(index, key);
			this.values.add(index, value);
		}
	}


	private int calcInsertIndex(K key) {
		int insertIndex = Collections.binarySearch(this.keys, key, this.comparator);
		if(insertIndex > -1) {
			insertIndex++;
		}
		else {
			insertIndex = -insertIndex - 1;
		}
		return insertIndex;
	}

}
