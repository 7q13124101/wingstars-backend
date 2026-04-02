package wingstars.backend.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    private String fullName;

    private String gender;

//    private Date dateOfBirth;

    @Convert(converter = RoleConverter.class)
    @Column(length = 13, nullable = false)
    private Role role;

    private String imageUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Boolean isDeleted = false;

    @Override
    public String toString() {
        return userName;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof User otherU))
            return false;
        return this.userName.equals(otherU.userName);
    }

    @Override
    public int hashCode() {
        return this.userName.hashCode();
    }
}
