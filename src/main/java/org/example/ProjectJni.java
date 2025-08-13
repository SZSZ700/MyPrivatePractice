package org.example;

public class ProjectJni { // Main project class to load the native C library

    static { System.loadLibrary("projectjni"); } // Load the compiled C library (.dll)

    // --- Person Class ---
    public class Person {
        private long ptr; // Pointer to the C struct representing a person

        public Person(String name, int age, String id, String phone, String email) {
            long namePtr = createCustomString(name); // Create native C string for name
            long idPtr = createCustomString(id); // Create native C string for ID
            long phonePtr = createCustomString(phone); // Create native C string for phone
            long emailPtr = createCustomString(email); // Create native C string for email
            ptr = createPerson(namePtr, age, idPtr, phonePtr, emailPtr); // Create person struct with C pointers
        }

        public long getPtr() { return ptr; } // Return pointer to the native Person struct

        public void print() { printPerson(ptr); } // Print person details (native)

        public void free() {
            freePerson(ptr); // Free native memory
            ptr = 0; // Invalidate pointer in Java
        }

        // Native methods (C side)
        private native long createPerson(long namePtr, int age, long idPtr, long phonePtr, long emailPtr);
        private native void printPerson(long ptr);
        private native void freePerson(long ptr);
    }

    // --- PeopleList Class ---
    public class PeopleList {
        private long ptr; // Pointer to the C struct representing the people list

        public PeopleList() { ptr = createPeopleList(); } // Create the list on native side

        public void addPerson(Person p) { addPerson(ptr, p.getPtr()); } // Add a Person to list

        public void removePerson(int index) { removePerson(ptr, index); } // Remove person at index

        public void print() { printPeopleList(ptr); } // Print list of people

        public int findPersonByName(String name) {
            long namePtr = createCustomString(name); // Create native string for search
            int index = findPersonByName(ptr, namePtr); // Search person in native side
            freeCString(namePtr); // Free search string after use
            return index;
        }

        public void updatePerson(int index, String newName, int newAge, String newId, String newPhone, String newEmail) {
            long namePtr = createCustomString(newName); // Create new C string for name
            long idPtr = createCustomString(newId); // Create new C string for ID
            long phonePtr = createCustomString(newPhone); // Create new C string for phone
            long emailPtr = createCustomString(newEmail); // Create new C string for email

            updatePerson(ptr, index, namePtr, newAge, idPtr, phonePtr, emailPtr); // Update person fields (native)

            // Important: DO NOT free here - strings now belong to the updated Person object
        }

        public void free() {
            freePeopleList(ptr); // Free entire list in native memory
            ptr = 0; // Invalidate pointer
        }

        // Native methods (C side)
        private native long createPeopleList();
        private native void addPerson(long listPtr, long personPtr);
        private native void removePerson(long listPtr, int index);
        private native void printPeopleList(long listPtr);
        private native int findPersonByName(long listPtr, long namePtr);
        private native void updatePerson(long listPtr, int index, long newNamePtr, int newAge, long newIdPtr, long newPhonePtr, long newEmailPtr);
        private native void freePeopleList(long listPtr);
    }

    // --- Other Native Methods ---
    public native long createMatrix(int n); // Create a 2D matrix
    public native void printMatrix(long ptr, int n); // Print matrix
    public native int sumArray(long ptr, int length); // Sum values in array
    public native int getMatrixValue(long ptr, int row, int col); // Get specific matrix value
    public native void setMatrixValue(long ptr, int row, int col, int value); // Set specific matrix value
    public native void freeMatrix(long ptr, int n); // Free matrix memory

    public native long createCustomString(String str); // Create native C string
    public native void freeCString(long ptr); // Free C string

    // --- Main Program Execution ---
    public static void main(String[] args) {
        ProjectJni jni = new ProjectJni(); // Create instance of the project

        // Matrix demo
        long matrixPtr = jni.createMatrix(3); // Create 3x3 matrix
        System.out.println("--- Original Matrix ---");
        jni.printMatrix(matrixPtr, 3); // Print matrix

        jni.setMatrixValue(matrixPtr, 1, 1, 99); // Change value
        System.out.println("--- Matrix After Update ---");
        jni.printMatrix(matrixPtr, 3); // Print updated matrix

        jni.freeMatrix(matrixPtr, 3); // Free matrix

        // PeopleList demo
        PeopleList peopleList = jni.new PeopleList(); // Create list

        Person p1 = jni.new Person("Sharbel Zarzour", 27, "123456789", "+972-50-1234567", "sharbel@example.com");
        Person p2 = jni.new Person("John Doe", 35, "987654321", "+972-50-7654321", "john@example.com");

        peopleList.addPerson(p1); // Add first person
        peopleList.addPerson(p2); // Add second person

        System.out.println("\n--- After Adding People ---");
        peopleList.print(); // Print all people

        int index = peopleList.findPersonByName("John Doe"); // Find "John Doe"
        if (index != -1) {
            System.out.println("\nJohn Doe found at index: " + index);
            peopleList.updatePerson(index, "Johnny Updated", 36, "000000000", "+972-50-0000000", "johnny@example.com"); // Update details
        }

        System.out.println("\n--- After Update ---");
        peopleList.print(); // Print updated list

        peopleList.removePerson(0); // Remove first person

        System.out.println("\n--- After Removal ---");
        peopleList.print(); // Print after removal

        peopleList.free(); // Free the people list

        System.out.println("\nProgram finished successfully."); // End
    }
}
