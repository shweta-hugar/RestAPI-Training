package Testing.UST;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category {

	int id;
	String name;

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Category() {

	}

}
