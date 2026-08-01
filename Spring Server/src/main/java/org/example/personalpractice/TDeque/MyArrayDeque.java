package org.example.personalpractice.TDeque;

@SuppressWarnings("unused")
public class MyArrayDeque<T> implements Cloneable, TDeque<T> {
    private T[] elements; // The array that stores the elements.
    private int head; // The index of the first element.
    private int tail; // The index after the last element.
    private int size; // The number of elements inside the deque.

    // O(1) ✅
    // Suppress the unchecked cast warning.
    @SuppressWarnings("unchecked")
    public MyArrayDeque() {
        // Create an array with power-of-two length.
        elements = (T[]) new Object[8];
        this.head = 0; // Set the first element index to zero.
        this.tail = 0; // Set the next insertion index to zero.
        this.size = 0; // Set the size to zero.
    }

    // O(1) ✅
    public int size() { return this.size; } // Returns the number of elements.

    // O(1) ✅
    // Checks if the deque is empty.
    public boolean isEmpty() { return this.size == 0; }

    // O(1) ✅
    // Adds a value to the end of the deque.
    public void addLast(T value) throws NullPointerException {
        // Check if the value is null.
        // Throw an exception because null values are not allowed.
        if (value == null) { throw new NullPointerException(); }

        // Check if the array is full.
        // Grow the array before inserting.
        if (this.size == this.elements.length) { this.grow(); }

        // Store the value at the tail index.
        this.elements[this.tail] = value;

        // Move tail forward in a circular way.
        this.tail = (this.tail + 1) & (this.elements.length - 1);

        // Increase the size.
        this.size++;
    }

    // O(1) ✅
    // Adds a value to the beginning of the deque.
    public void addFirst(T value) throws NullPointerException {
        // Check if the value is null.
        // Throw an exception because null values are not allowed.
        if (value == null) { throw new NullPointerException(); }

        // Check if the array is full.
        // Grow the array before inserting.
        if (size == elements.length) { this.grow(); }

        // Move head backward in a circular way.
        this.head = (this.head - 1) & (this.elements.length - 1);

        // Store the value at the new head index.
        this.elements[this.head] = value;

        // Increase the size.
        this.size++;
    }

    // O(1) ✅
    // Removes and returns the first value.
    public T pollFirst() {
        // Check if the deque is empty.
        // Return null when there is no value.
        if (this.isEmpty()) { return null; }

        T value = this.elements[this.head]; // Save the first value.

        this.elements[this.head] = null; // Clear the old position.

        // Move head forward in a circular way.
        this.head = (this.head + 1) & (this.elements.length - 1);

        this.size--; // Decrease the size.

        return value; // Return the removed value.
    }

    // O(1) ✅
    // Removes and returns the last value.
    public T pollLast() {
        // Check if the deque is empty.
        // Return null when there is no value.
        if (this.isEmpty()) { return null; }

        // Move tail backward in a circular way.
        this.tail = (this.tail - 1) & (this.elements.length - 1);

        T value = this.elements[this.tail]; // Save the last value.

        this.elements[this.tail] = null; // Clear the old position.

        this.size--; // Decrease the size.

        return value; // Return the removed value.
    }

    // O(1) ✅
    // Returns the first value without removing it.
    public T peekFirst() {
        // Check if the deque is empty.
        // Return null when there is no value.
        if (this.isEmpty()) { return null; }

        return this.elements[this.head]; // Return the first value.
    }

    // O(1) ✅
    // Returns the last value without removing it.
    public T peekLast() {
        // Check if the deque is empty.
        // Return null when there is no value.
        if (this.isEmpty()) { return null; }

        // Calculate the index of the last value.
        var lastIndex = (this.tail - 1) & (this.elements.length - 1);

        return this.elements[lastIndex]; // Return the last value.
    }

    // O(n) ✅
    // Removes the first occurrence of the given value.
    public boolean removeFirstOccurrence(T value) {
        // Check if the searched value is null.
        // Return false because null values are not stored.
        if (value == null) { return false; }

        // Check if the deque is empty.
        // Return false because there is nothing to remove.
        if (this.isEmpty()) { return false; }

        // Check if the logical data is not wrapped.
        if (this.head < this.tail) {
            // Scan from head to tail in physical order.
            for (var i = this.head; i < this.tail; i++) {
                // Check if the current element equals the searched value.
                if (elements[i].equals(value)) {
                    deleteAt(i); // Remove the element at this index.
                    // Return true because an element was removed.
                    return true;
                }
            }
        } else {
            // Scan from head to the physical end of the array.
            for (var i = this.head; i < this.elements.length; i++) {
                // Check that the cell is active and equals the searched value.
                if (this.elements[i] != null && this.elements[i].equals(value)) {
                    this.deleteAt(i); // Remove the element at this index.
                    // Return true because an element was removed.
                    return true;
                }
            }

            // Scan from the physical start of the array to tail.
            for (var i = 0; i < this.tail; i++) {
                // Check that the cell is active and equals the searched value.
                if (this.elements[i] != null && this.elements[i].equals(value)) {
                    this.deleteAt(i); // Remove the element at this index.
                    // Return true because an element was removed.
                    return true;
                }
            }
        }

        return false; // Return false because the value was not found.
    }

    // O(n) ✅
    // Removes the last occurrence of the given value.
    public boolean removeLastOccurrence(T value) {
        // Check if the searched value is null.
        // Return false because null values are not stored.
        if (value == null) { return false; }

        // Check if the deque is empty.
        // Return false because there is nothing to remove.
        if (this.isEmpty()) { return false; }

        // Check if the logical data is not wrapped.
        if (this.head < this.tail) {
            // Scan backward from tail minus one to head.
            for (var i = this.tail - 1; i >= this.head; i--) {
                // Check if the current element equals the searched value.
                if (this.elements[i].equals(value)) {
                    this.deleteAt(i); // Remove the element at this index.
                    // Return true because an element was removed.
                    return true;
                }
            }
        } else {
            // Scan backward from tail minus one to the physical start.
            for (var i = this.tail - 1; i >= 0; i--) {
                // Check that the cell is active and equals the searched value.
                if (this.elements[i] != null && this.elements[i].equals(value)) {
                    this.deleteAt(i); // Remove the element at this index.
                    // Return true because an element was removed.
                    return true;
                }
            }

            // Scan backward from the physical end of the array to head.
            for (var i = this.elements.length - 1; i >= this.head; i--) {
                // Check that the cell is active and equals the searched value.
                if (this.elements[i] != null && this.elements[i].equals(value)) {
                    this.deleteAt(i); // Remove the element at this index.
                    // Return true because an element was removed.
                    return true;
                }
            }
        }

        return false; // Return false because the value was not found.
    }

    // O(n) ✅
    // Deletes an element from a real array index.
    private void deleteAt(int index) {
        // Calculate the real index of the last active element.
        var lastIndex = (this.tail - 1) & (this.elements.length - 1);

        // Shift elements left only until the old last element.
        for (var i = index; i != lastIndex; i = (i + 1) & (this.elements.length - 1)) {
            // Calculate the next circular index.
            var nextIndex = (i + 1) & (this.elements.length - 1);

            // Move the next active element into the current place.
            this.elements[i] = this.elements[nextIndex];
        }

        // Move tail to the old last element position.
        this.tail = lastIndex;

        // Clear the removed duplicate last slot.
        this.elements[this.tail] = null;

        // Decrease the size.
        this.size--;
    }

    // O(n) ✅
    // Creates a copy of the deque.
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public MyArrayDeque<T> clone() {
        // Create a new deque object.
        var copy = new MyArrayDeque<T>();

        // Replace the default array with an array of the same capacity.
        copy.elements = this.elements.clone();

        // Copy the head index.
        copy.head = this.head;

        // Copy the tail index.
        copy.tail = this.tail;

        // Copy the size.
        copy.size = this.size;

        // Return the copied deque.
        return copy;
    }

    // O(n) ✅
    // Suppress the unchecked cast warning.
    @SuppressWarnings("unchecked")
    private void grow() {
        // Create a new array with double capacity.
        T[] newElements = (T[]) new Object[this.elements.length * 2];

        // Copy all elements in logical order.
        for (var i = 0; i < this.size; i++) {
            // Copy the element from the circular old array.
            newElements[i] = this.elements[(this.head + i) & (this.elements.length - 1)];
        }

        // Replace the old array with the new array.
        this.elements = newElements;

        // Reset head to the first index.
        this.head = 0;

        // Set tail after the last copied element.
        this.tail = size;
    }
}