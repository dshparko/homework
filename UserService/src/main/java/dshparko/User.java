package dshparko;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private int age;

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    public User(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

}
