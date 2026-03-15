package org.example.personalpractice.MiniBoot;

// Import reflection API for accessing constructors dynamically
import java.lang.reflect.Constructor;

// Import HashMap to store created beans
import java.util.HashMap;

// Define a simple container class that will manage beans
@MyService
public class MyContainer {

    // Map that stores bean instances using their class as the key
    @SuppressWarnings("FieldMayBeFinal")
    private HashMap<Class<?>, Object> beans;

    // Constructor of the container
    @SuppressWarnings("unused")
    public MyContainer() {

        // Initialize the bean storage map
        this.beans = new HashMap<>();
    }

    // Method that scans an array of classes and registers annotated services
    @SuppressWarnings("unused")
    public void scanAndRegister(Class<?>[] classes) throws Exception {

        // Iterate over all provided classes
        for (Class<?> aClass : classes) {

            // Check whether the current class is marked with MyService
            if (aClass.isAnnotationPresent(MyService.class)) {

                // Register the current class as a bean
                registerBean(aClass);
            }
        }
    }

    // Method that registers a bean and creates it if necessary
    public Object registerBean(Class<?> clazz) throws Exception {

        // Check if the bean was already created
        if (this.beans.containsKey(clazz)) {

            // Return the existing bean
            return this.beans.get(clazz);
        }

        // Check if the class has the MyService annotation
        if (!clazz.isAnnotationPresent(MyService.class)) {

            // If not annotated, do not create a bean
            return null;
        }

        // Retrieve all constructors of the class
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();

        // Select the first constructor
        Constructor<?> constructor = constructors[0];

        // Get the parameter types of the constructor
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        // Create an array to hold constructor arguments
        Object[] params = new Object[parameterTypes.length];

        // Iterate over constructor parameters
        for (int i = 0; i < parameterTypes.length; i++) {

            // Recursively register and create dependency beans
            params[i] = registerBean(parameterTypes[i]);
        }

        // Create a new instance of the class using reflection
        Object obj = constructor.newInstance(params);

        // Store the created bean in the container
        this.beans.put(clazz, obj);

        // Return the created bean
        return obj;
    }

    // Method that retrieves a bean from the container
    @SuppressWarnings("unused")
    public Object getBean(Class<?> clazz) {

        // Return the stored bean instance
        return this.beans.get(clazz);
    }
}
