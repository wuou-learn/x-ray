package io.github.x.ray.agent.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * ListUtils
 *
 * @author wuou
 */
public class ListUtils {

    public static <E extends Object> ArrayList<E> newArrayList(E... elements) {
        checkNotNull(elements);
        int capacity = computeArrayListCapacity(elements.length);
        ArrayList<E> list = new ArrayList<>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static int computeArrayListCapacity(int arraySize) {
        checkNonnegative(arraySize, "arraySize");

        // TODO(kevinb): Figure out the right behavior, and document it
        return saturatedCast(5L + arraySize + (arraySize / 10));
    }

    public static int checkNonnegative(int value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
        }
        return value;
    }

    public static int saturatedCast(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

}
