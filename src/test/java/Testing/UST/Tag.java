package Testing.UST;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tag {

	int id;
	String name;

	public Tag(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Tag() {

	}

}
