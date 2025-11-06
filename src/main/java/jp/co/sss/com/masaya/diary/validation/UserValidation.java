package jp.co.sss.com.masaya.diary.validation;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserValidation {
	@NotBlank
	private String username;
	private String displayName;
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	@Size(min = 8)
	@NotBlank
	private String passwordHash;
	private String confirmPassword;
	@NotBlank
	@Email
	private String email;

	@AssertTrue(message = "passwords.match")
	public boolean isPasswordValid() {
		if (passwordHash == null || passwordHash.isEmpty() || confirmPassword == null) {
			return true;
		}
		return passwordHash.equals(confirmPassword);
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String user_name) {
		this.username = user_name;
	}

	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	} 

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}