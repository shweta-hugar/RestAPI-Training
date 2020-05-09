package Testing.UST;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse {
	
	int code;
	String message;
	String type;
	
	public UserResponse()
	{
		
	}
	public UserResponse(int c, String message, String type)
	{
		this.code = c;
	    this.message = message;
	    this.type = type;
	}

}
