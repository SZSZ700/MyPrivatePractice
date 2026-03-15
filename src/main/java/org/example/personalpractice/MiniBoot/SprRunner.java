package org.example.personalpractice.MiniBoot;

public class SprRunner {

    // Entry point of the application
    public static void main(String[] args) throws Exception {

        // Create a new instance of the container
        MyContainer container = new MyContainer();

        // Create an array that simulates all classes inside a package
        Class<?>[] classes = {
                UserRepository.class,
                UserService.class
        };

        // Scan the classes and register only annotated services
        container.scanAndRegister(classes);

        // Retrieve the UserService bean from the container
        UserService service = (UserService) container.getBean(UserService.class);

        // Execute the business logic
        service.work();
    }
}
