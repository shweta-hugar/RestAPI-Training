package Testing.UST;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

	int id;
	String username;
	String firstName;
	String lastName;
	String password;
	String email;
	String phone;
	int userStatus;

	public User(int id, String userName, String firstName, String lastName, String password, String email, String phone,
			int status) {
		this.id = id;
		this.username = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.userStatus = status;
	}

}
