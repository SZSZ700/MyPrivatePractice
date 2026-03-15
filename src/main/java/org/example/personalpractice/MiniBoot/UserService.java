package org.example.personalpractice.MiniBoot;
// Mark this class as a service so the container will manage it
@MyService
// Define the service class
public class UserService {
    // Declare a dependency on UserRepository
    @SuppressWarnings("FieldMayBeFinal")
    private UserRepository repo;

    // Constructor that receives the repository dependency
    public UserService(UserRepository repo) {
        // Assign the injected repository to the field
        this.repo = repo;
    }

    // Define a method that performs some work
    @SuppressWarnings("unused")
    public void work() {
        // Call the repository method
        this.repo.printData();
    }
}