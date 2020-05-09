package Testing.UST;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PET {
	
	int id;
	Category category;
	String name;
	String[] photoUrls;
	Tag[] tags;
	String status;

	public PET(int id, Category category, String name,String[] photos, Tag[] tags,String status)
	{
		this.id= id;
		this.category = category;
		this.name=name;
		this.photoUrls = photos;
		this.tags= tags;
		this.status= status;
	}
	public PET()
	{
		
	}
}
