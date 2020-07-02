package account_manager.repository.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "social_number")
    private String socialNumber;

    @Column
    private String city;

    @Column
    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    @Column
    private String apartment;

    public ClientEntity(Integer id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientEntity)) return false;
        ClientEntity clientEntity = (ClientEntity) o;
        return Objects.equals(getId(), clientEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
