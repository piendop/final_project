package searchlocation.miniproject01.Models;

public class Place {
	//ID of Place
	private String ID;
	//Name of place (Different name for diff plans)
	private String Name;
	//Note of that plans
	private String review;
	//Store hex number of the color tag
	private String colorTagHex;
	//Longtidute Latitute


	//Getter and Setter
	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getReview() {
		return review;
	}

	public void setNotes(String notes) {
		review = notes;
	}
}
