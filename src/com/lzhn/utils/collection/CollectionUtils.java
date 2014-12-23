package com.lzhn.utils.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 集合类操作工具类
 * 
 * @author lzhn
 * 
 */
public class CollectionUtils {

	public static <T> void print(Collection<T> collection) {

	}

	public static <T> String parseCollectionToString(Collection<T> collection) {
		if (collection == null || collection.size() < 1) {
			return new String();
		}
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (T t : collection) {
			builder.append(t.toString()).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("]");
		return builder.toString();
	}

	public static <T> List<T> copyList(List<T> sourceList) {
		List<T> destList = new ArrayList<T>();
		for (T t : sourceList) {
			destList.add(t);
		}
		return destList;
	}

	public static <T> void add(List<T> list, T[] t, int len) {
		for (int i = 0; i < len; i++) {
			list.add(t[i]);
		}
	}

	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}

	public static <T> List<T> newLinkedList() {
		return new LinkedList<T>();
	}

	public static <T> Set<T> newHashSet() {
		return new HashSet<T>();
	}

	public static <T> Set<T> newTreeSet() {
		return new TreeSet<T>();
	}

	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <K, V> Map<K, V> newTreeMap() {
		return new TreeMap<K, V>();
	}
}
