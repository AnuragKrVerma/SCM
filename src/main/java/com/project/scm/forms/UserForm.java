package com.project.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class UserForm {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @Size(min = 2, max = 1000, message = "About must be between 2 and 1000 characters")
    private String about;
    @Size(min = 8, max = 12, message = "Number must be between 8 and 12 characters")
    private String phoneNumber;


}
