package org.example.personalpractice.TDeque;

/**
 * A double-ended queue interface.
 * Elements can be added and removed from both ends.
 *
 * @param <T> The type of elements stored in the deque.
 */
@SuppressWarnings("unused")
public interface TDeque<T> {
    /**
     * Returns the number of elements in the deque.
     *
     * @return The current size.
     */
    int size();

    /**
     * Checks whether the deque is empty.
     *
     * @return True if the deque is empty, otherwise false.
     */
    boolean isEmpty();

    /**
     * Adds an element to the front of the deque.
     *
     * @param value The element to add.
     */
    void addFirst(T value);

    /**
     * Adds an element to the end of the deque.
     *
     * @param value The element to add.
     */
    void addLast(T value);

    /**
     * Removes and returns the first element.
     *
     * @return The removed element, or null if empty.
     */
    T pollFirst();

    /**
     * Removes and returns the last element.
     *
     * @return The removed element, or null if empty.
     */
    T pollLast();

    /**
     * Returns the first element without removing it.
     *
     * @return The first element, or null if empty.
     */
    T peekFirst();

    /**
     * Returns the last element without removing it.
     *
     * @return The last element, or null if empty.
     */
    T peekLast();

    /**
     * Removes the first occurrence of the specified value.
     *
     * @param value The value to remove.
     * @return True if an element was removed, otherwise false.
     */
    boolean removeFirstOccurrence(T value);

    /**
     * Removes the last occurrence of the specified value.
     *
     * @param value The value to remove.
     * @return True if an element was removed, otherwise false.
     */
    boolean removeLastOccurrence(T value);
}