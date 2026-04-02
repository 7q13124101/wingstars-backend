package wingstars.backend.dtos.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest implements Serializable {
    @NotEmpty
    String userId;
    @NotEmpty
    String name;
    @NotEmpty
    String password;
    String role;
}
