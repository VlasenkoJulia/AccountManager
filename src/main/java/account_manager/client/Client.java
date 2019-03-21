package account_manager.client;

public class Client {
    private Integer id;
    private String lastName;
    private String firstName;

    public Client(int id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Client(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

}
