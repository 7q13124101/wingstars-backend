package wingstars.backend.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserRequest implements Serializable {
    @NotEmpty
    String userName;

    @NotEmpty
    String password;

    @NotEmpty
    String phoneNumber;

    @NotEmpty
    String email;

    @NotEmpty
    String fullName;

    @NotEmpty
    String gender;

    @NotEmpty
    String imageUrl;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    String role;

}
