package com.example.learningAPISpring.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String firstName;
    private String lastName;

    @Email
    private String email;

    @Size(min=8, message = "Mat khau phai co it nhat 8 ky tu")
    @NotBlank
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Mat khau phai chua it nhat mot chu cai in hoa, mot chu cai in thuong, mot chu so va mot ky tu dac biet")
    private CharSequence password;

    private String phoneNumber;

}
