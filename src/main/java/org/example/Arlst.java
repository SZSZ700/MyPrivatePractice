package org.example;

import java.util.Arrays;
import java.util.Objects;

/**
 * A simple generic dynamic array implementation similar to ArrayList.
 * Supports add, insert, remove, erase, clear, contains, indexOf, lastIndexOf,
 * ensureCapacity, trimToSize, toArray, get, set, isEmpty, and toString.
 *
 * @param <T> the type of elements stored in the list
 */
public class Arlst<T> {

    /** internal array that holds the elements */
    private T[] array;

    /** current capacity of the array */
    private int size;

    /** number of elements actually stored */
    private int current;

    /**
     * Default constructor.
     * Initializes the array with capacity 10 and sets current size to 0.
     */
    @SuppressWarnings("unchecked")
    public Arlst() {
        this.size = 10;                         // set initial capacity to 10
        this.array = (T[]) new Object[this.size]; // create internal array
        this.current = 0;                       // initially no elements
    }

    /**
     * Returns the element at a given index.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public T get(int index) {
        if (index < 0 || index >= this.current) { // check if index is out of bounds
            throw new IndexOutOfBoundsException(); // throw exception if invalid
        }
        return this.array[index];                 // return the element at index
    }

    /**
     * Returns the number of elements actually stored (logical size).
     *
     * @return number of stored elements
     */
    public int size() {
        return this.current; // return logical size (not capacity)
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if no elements, false otherwise
     */
    public boolean isEmpty() {
        return this.current == 0; // return true if current size is 0
    }

    /**
     * Adds a new element at the end of the list.
     * Doubles capacity if the array is full.
     *
     * @param valueToAdd the element to add
     */
    @SuppressWarnings("unchecked")
    public void add(T valueToAdd) {
        if (this.current == this.size) {            // if array is full
            this.size *= 2;                         // double capacity
            var temp = (T[]) new Object[this.size]; // create a new larger array

            for (int i = 0; i < this.current; i++) { // copy old elements
                temp[i] = this.array[i];
            }

            this.array = temp;                      // replace old array
        }

        this.array[current++] = valueToAdd;         // insert new element and update count
    }

    /**
     * Inserts an element at a specific index.
     * Shifts elements to the right as needed.
     *
     * @param index the position to insert into
     * @param value the element to insert
     * @throws IndexOutOfBoundsException if index is invalid
     */
    @SuppressWarnings("unchecked")
    public void insert(int index, T value) {
        if (index < 0 || index > this.current) {   // check valid index
            throw new IndexOutOfBoundsException(); // throw if invalid
        }

        if (this.current == this.size) {            // if array is full
            this.size *= 2;                         // double capacity
            T[] temp = (T[]) new Object[this.size]; // create new larger array
            for (int i = 0; i < this.current; i++) { // copy elements
                temp[i] = this.array[i];
            }
            this.array = temp;                      // replace old array
        }

        for (int i = this.current; i > index; i--) { // shift elements right
            this.array[i] = this.array[i - 1];
        }

        this.array[index] = value;                  // place new element
        this.current++;                             // update logical size
    }

    /**
     * Sets a value at a given index.
     *
     * @param index the position to update
     * @param value the new value
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public void set(int index, T value) {
        if (index < 0 || index >= this.current) {   // check bounds
            throw new IndexOutOfBoundsException();  // throw if invalid
        }

        this.array[index] = value;                  // update element
    }

    /**
     * Removes the first occurrence of a value.
     *
     * @param value the value to remove
     * @return true if an element was removed, false if not found
     */
    public boolean remove(T value) {
        for (int i = 0; i < this.current; i++) {          // iterate over elements
            if (Objects.equals(value, this.array[i])) {   // check equality
                for (int j = i; j < this.current - 1; j++) { // shift elements left
                    this.array[j] = this.array[j + 1];
                }

                this.array[--this.current] = null;        // clear last slot and reduce size

                return true;                              // element removed
            }
        }

        return false;                                     // not found
    }

    /**
     * Removes all occurrences of a value.
     * Rebuilds a new array with reduced size.
     *
     * @param value the value to erase
     * @return the number of removed elements
     */
    @SuppressWarnings("unchecked")
    public int erase(T value) {
        int countToRemove = 0;                            // counter of removed elements
        for (int i = 0; i < this.current; i++) {          // iterate over elements
            if (Objects.equals(value, this.array[i])) {   // check equality
                countToRemove++;                          // count matches
            }
        }

        if (countToRemove == 0) {                         // if nothing to remove
            return 0;
        }

        int newSize = this.current - countToRemove;       // new logical size
        T[] newArray = (T[]) new Object[Math.max(newSize, 10)]; // new array
        int newIndex = 0;                                 // index for new array

        for (int i = 0; i < this.current; i++) {          // copy only non-matching values
            if (!Objects.equals(value, this.array[i])) {
                newArray[newIndex++] = this.array[i];
            }
        }

        this.array = newArray;                            // replace internal array
        this.current = newSize;                           // update logical size
        this.size = this.array.length;                    // update capacity

        return countToRemove;                             // return number removed
    }

    /**
     * Clears all elements from the list.
     * Resets to default capacity of 10.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        this.array = (T[]) new Object[10]; // create new array with default capacity
        this.size = 10;                    // reset capacity
        this.current = 0;                  // reset logical size
    }

    /**
     * Checks if the list contains a specific value.
     *
     * @param value the value to search for
     * @return true if value exists, false otherwise
     */
    public boolean contains(T value) {
        for (int i = 0; i < this.current; i++) {     // iterate over elements
            if (Objects.equals(this.array[i], value)) { // check equality
                return true;                         // found
            }
        }

        return false;                                // not found
    }

    /**
     * Returns the index of the first occurrence of a value.
     *
     * @param value the value to find
     * @return the index of the value, or -1 if not found
     */
    public int indexOf(T value) {
        for (int i = 0; i < this.current; i++) {     // iterate over elements
            if (Objects.equals(this.array[i], value)) { // check equality
                return i;                            // return index
            }
        }

        return -1;                                   // not found
    }

    /**
     * Returns the index of the last occurrence of a value.
     *
     * @param value the value to find
     * @return the last index of the value, or -1 if not found
     */
    public int lastIndexOf(T value) {
        for (int i = this.current - 1; i >= 0; i--) {    // iterate backwards
            if (Objects.equals(this.array[i], value)) {  // check equality
                return i;                                // return index
            }
        }

        return -1;                                       // not found
    }

    /**
     * Ensures the array has at least the given capacity.
     * Grows if needed.
     *
     * @param minCapacity minimum required capacity
     */
    @SuppressWarnings("unchecked")
    public void ensureCapacity(int minCapacity) {
        if (minCapacity > this.size) {                       // if required > current capacity
            this.size = Math.max(this.size * 2, minCapacity); // update to max of double or required
            T[] temp = (T[]) new Object[this.size];           // create new array

            for (int i = 0; i < this.current; i++) {          // copy elements
                temp[i] = this.array[i];
            }

            this.array = temp;                                // replace array
        }
    }

    /**
     * Reduces the capacity to match the logical size.
     */
    @SuppressWarnings("unchecked")
    public void trimToSize() {
        if (this.current < this.size) {                 // if current < capacity
            T[] temp = (T[]) new Object[this.current];  // new smaller array

            for (int i = 0; i < this.current; i++) {    // copy only used elements
                temp[i] = this.array[i];
            }

            this.array = temp;                          // replace array
            this.size = this.current;                   // update capacity
        }
    }

    /**
     * Returns a new array containing all elements in the list.
     *
     * @return an array copy of the current elements
     */
    public T[] toArray() {
        return Arrays.copyOf(this.array, this.current); // return copy of used elements
    }

    /**
     * Returns a string representation of the list.
     *
     * @return string with current elements
     */
    @Override
    public String toString() {
        return "Arlst{" +
                "array=" + Arrays.toString(Arrays.copyOf(array, current)) +
                '}'; // show only current elements
    }
}

