package com.example.userservice.entity;

import com.example.userservice.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@ToString
@Table(name = "users"
//, uniqueConstraints = {
       // @UniqueConstraint(name ="uk_user_email", columnNames = {"email"})
//}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private Integer age;

    //null for dev , migration in prod
    @CreationTimestamp
    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    @Column(updatable = false)
    private Role role;

}
