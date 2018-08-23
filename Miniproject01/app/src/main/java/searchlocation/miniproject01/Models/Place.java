package searchlocation.miniproject01.Models;

import android.location.Location;

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
	private double latitude;
	private double longitude;

	private String address;
	private int colorNumber=1;


	//Getter and Setter
	public int getColorNumber() {return  colorNumber;}

	public void setColorNumber(int colorNumber) {this.colorNumber = colorNumber;}


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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
