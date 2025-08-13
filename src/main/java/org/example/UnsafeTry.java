package org.example;
import sun.misc.Unsafe;
import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.foreign.ValueLayout.JAVA_FLOAT;

public class UnsafeTry {

    //internal class MyData
     public static class MyData {
        private int x;

        public MyData(){ this.x = 7; }

        public int getX() { return x; }

        public void setX(int x) { this.x = x; }

        @Override
        public String toString() { return "MyData{" + "x=" + x + '}'; }
    }

    public static void main(String[] args) throws Exception {
        //--- Reflection - for fields, methods, constructors, super class, interfaces access ---//
        System.out.println("Reflection: ");
        MyData a1 = new MyData();

        // Field:
        // A Field provides information about, and dynamic access to,
        // a single field of a class or an interface.
        // The reflected field may be a class (static) field or an instance field.

        // getDeclaredField():
        // Returns a Field object that reflects the specified declared field of the class or interface
        // represented by this Class object.
        // The name parameter is a String that specifies the simple name of the desired field.
        Field x = MyData.class.getDeclaredField("x");

        //Set the accessible flag for this reflected object to the indicated boolean value.
        x.setAccessible(true);

        // Sets the field represented by this Field object on the specified object argument
        // to the specified new value.
        x.set(a1, 8);

        // Returns the value of the field represented by this Field, on the specified object.
        System.out.println("x = " + x.get(a1) + "\n");

        // fields array:
        // getFields():
        // Returns an array containing Field objects
        // reflecting all the accessible public fields of the class
        // or interface represented by this Class object
        System.out.println("all the fields of the class MyData: ");
        Field [] allFields = a1.getClass().getDeclaredFields();
        for (Field currentField : allFields) { System.out.print(currentField.getName() + " | "); }
        System.out.println("\n");

        // methods array:
        // getMethods():
        // Returns an array containing Method objects
        // reflecting all the public methods of the class
        // or interface represented by this Class object,
        // including those declared by the class or interface
        // and those inherited from superclasses and superinterfaces.
        System.out.println("all the methods of the class MyData: ");
        Method [] allFunctiones = a1.getClass().getMethods();
        for (Method functione : allFunctiones) { System.out.print(functione.getName() + " | "); }
        System.out.println("\n");

        // constructors array:
        // getDeclaredConstructors():
        // Returns an array of Constructor objects reflecting all the constructors
        // implicitly or explicitly declared by the class represented by this Class object.
        System.out.println("all the constructors of the class MyData: ");
        Constructor<?>[] constructors = a1.getClass().getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) { System.out.println(constructor.getName() + " | "); }
        System.out.println("\n");

        // super class:
        // getSuperclass():
        // Returns the Class representing the direct superclass of the entity.
        System.out.println("the parent class of MyData is:" + a1.getClass().getSuperclass() + "\n");

        // interfaces array:
        // getInterfaces():
        // Returns the interfaces directly implemented by the class
        // or interface represented by this Class object.
        System.out.println("all the interfaces that class MyData implements: ");
        Class<?>[] interfaces = a1.getClass().getInterfaces();
        for (Class<?> anInterface : interfaces) { System.out.println(anInterface.getName()); }
        System.out.println("");
        //--- Reflection - for fields, methods, constructors, super class, interfaces access ---//


        //------ Unsafe + offset  ------//
        System.out.println("unsafe + offset: ");
        // private static final Unsafe theUnsafe = new Unsafe();
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        // Set the accessible flag for this reflected object to the indicated boolean value.
        f.setAccessible(true);
        // Unsafe unsafe = Unsafe.theUnsafe; -> get the value of the field , המרה מפורשת לאובייקט UNSAFE
        Unsafe unsafe = (Unsafe) f.get(null);


        // Allocates an instance but does not run any constructor.
        // Initializes the class if it has not yet been.
        System.out.println("instance of class MyData created without using any constructors!");
        MyData myData = (MyData) unsafe.allocateInstance(MyData.class);

        // Reports the location of a given field in the storage allocation of its class.
        long offset = unsafe.objectFieldOffset(x);
        // Stores a value into a given Java variable.
        unsafe.putInt(myData, offset, 77);
        // Fetches a value from a given Java variable.
        System.out.println("x = " + unsafe.getInt(myData, offset) + "\n");
        //------ Unsafe + offset  ------//



        //------ unsafe native memory allocation ------//
        System.out.println("unsafe native memory allocation: ");
        // int *num = (int*)malloc(sizeOf(int))
        // &num;
        long intAddress = unsafe.allocateMemory(4);
        System.out.println("address of premitive int value is: " + intAddress);
        // *num = 88;
        unsafe.putInt(intAddress, 100);
        // *num;
        int intValue = unsafe.getInt(intAddress);
        System.out.println("value = " + intValue);
        // free(num)
        unsafe.freeMemory(intAddress);

        // int *arr = (int*)malloc(sizeOf(int) * length)
        int length = 7; // array length
        int sizeOfInt = 4; // size of int variable (bytes)
        long baseAddress = unsafe.allocateMemory(length * sizeOfInt); // כתובת התא הראשון במערך

        // fill the array
        for (int i = 0; i < length; i++) {
            unsafe.putInt(baseAddress + i * sizeOfInt, i * 10); // כתיבה למערך
        }
        unsafe.freeMemory(baseAddress); // שחרור המערך מהזיכרון
        System.out.println("\n");
        //------ native memory allocation ------//



        //------ varHandle - for fields access ------//
        System.out.println("varHandle (faster that using regular reflection): ");
        MyData m1 = new MyData();

        // A VarHandle is a dynamically strongly typed reference to a variable,
        // or to a parametrically-defined family of variables,
        // including static fields, non-static fields, array elements,
        // or components of an off-heap data structure.
        // Access to such variables is supported under various access modes,
        // including plain read/write access, volatile read/write access, and compare-and-set.
        VarHandle vh = MethodHandles
                .privateLookupIn(MyData.class, MethodHandles.lookup())
                .findVarHandle(MyData.class, "x", int.class);

        // Sets the value of a variable to the newValue,
        // with memory semantics of setting as if the variable was declared non-volatile and non-final.
        // Commonly referred to as plain write access.
        vh.set(m1, 99);

        // Returns the value of a variable,
        // with memory semantics of reading as if the variable was declared non-volatile.
        // Commonly referred to as plain read access.
        int value = (int) vh.get(m1); // get
        System.out.println("value = " + value + "\n");
        //------ varHandle ------//



        //------ MemorySegment - safe native memory allocation ------//
        System.out.println("MemorySegment - safe native memory allocation: ");
        System.out.println("instead of using Unsafe obj!");
        // An arena controls the lifecycle of native memory segments,
        // providing both flexible allocation and timely deallocation.
        Arena arena = Arena.ofConfined();

        // A memory segment provides access to a contiguous region of memory.
        MemorySegment segment = arena.allocate(ValueLayout.JAVA_INT);

        // Writes an int into this segment at the given offset, with the given layout.
        segment.set(ValueLayout.JAVA_INT, 0, 100);

        // Reads an int from this segment at the given offset, with the given layout.
        var val = segment.get(ValueLayout.JAVA_INT, 0);
        System.out.println("value = " + val);
        System.out.println("");


        // arrays
        System.out.println("create native array: ");
        //create arena obj
        Arena arena1 = Arena.ofShared();
        // create MemorySeg 0bj, using arena allocation
        MemorySegment array = arena1.allocate(ValueLayout.JAVA_INT.withByteAlignment(4), 8);
        // size of the Segment in bytes
        System.out.println(
                "size of the array (bytes): " + array.byteSize()
                + ", the array found at address: "
                + array.address()
        );

        // fill the array
        System.out.println("array values: ");
        for (int i = 0; i < 8; i++) {
            // *(array + i) = i * 10;
            // (layout - גודל הקפיצה     , offset - מס קפיצות ,   value - ערך להשמה)
            array.setAtIndex(ValueLayout.JAVA_INT, i, i * 10);
            // *(array + i)
            array.getAtIndex(ValueLayout.JAVA_INT, i);
        }

        arena1.close(); // close the arena obj
        System.out.println("shared arena got closed!");


        // structs
        System.out.println("creating a native struct (using unique memory layout): ");

        // שלב 1: הגדרת מבנה Struct עם שדות - unique layout
        MemoryLayout personLayout = MemoryLayout.structLayout(
                ValueLayout.JAVA_INT.withName("age"),
                ValueLayout.JAVA_FLOAT.withName("height"),
                ValueLayout.JAVA_BYTE.withName("gender")
        );

        // שלב 2: הקצאה של struct בזיכרון
        //create arena obj
        Arena parena = Arena.ofConfined();
        // create MemorySeg 0bj, using arena allocation
        MemorySegment person = parena.allocate(personLayout);

        // שלב 3: כתיבה לזיכרון
        person.set(ValueLayout.JAVA_INT, 0, 25); // age באופסט 0
        person.set(ValueLayout.JAVA_FLOAT, 4, 1.83f); // height באופסט 4
        person.set(ValueLayout.JAVA_BYTE, 8, (byte)'M'); // gender באופסט 8

        // שלב 4: קריאה מהזיכרון
        int age = person.get(ValueLayout.JAVA_INT, 0);
        float height = person.get(ValueLayout.JAVA_FLOAT, 4);
        char gender = (char) person.get(ValueLayout.JAVA_BYTE, 8);

        System.out.println("Age: " + age + ", Height: " + height + ", Gender: " + gender);

        /*
        typdef struct {
            int *age;
            float *height;
            char *gender;
        }Person;

        Person* createPerson(int *age, float *height, char *gender){
            Person *p = (Person*)malloc(sizeof(Person));
            p->age = age;
            p->height = height;
            p->gender = gender;

            return p;
        }
         */
        //------ MemorySegment - safe native memory allocation ------//
    }

} // end of class UnsafeTry
