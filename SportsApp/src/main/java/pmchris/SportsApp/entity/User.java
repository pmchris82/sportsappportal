package pmchris.SportsApp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.support.SimpleTriggerContext;
import pmchris.SportsApp.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "name is required")
    private String names;
    @NotBlank(message = "last names are required")
    private String lastNames;
    @NotBlank(message = "password is required")
    private String password;
    @Column(name = "phone_number")
    @NotBlank(message = "Phone Number is required")
    private String phoneNumber;


    private UserRole role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Address address;

    @Column(name = "created_at")
    private final LocalDateTime createdAt = LocalDateTime.now();









}
