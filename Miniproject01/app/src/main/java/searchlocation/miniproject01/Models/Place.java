package searchlocation.miniproject01.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

import java.util.Date;

@Entity(tableName = "place")
public class Place {
	//ID of Place

    @Ignore
	private String placeId;

    @PrimaryKey(autoGenerate = true)
	private int id;
	//Name of place (Different name for diff plans)
	private String Name;
	//Note of that plans
    @ColumnInfo(name = "review")
	private String review;
	//Longtidute Latitute
	private double latitude;
	private double longitude;

	private String address;
	private int colorNumber=1;

	@ColumnInfo(name = "created_at")
    private Date createdAt;

	//Getter and Setter
	public int getColorNumber() {return  colorNumber;}

	public void setColorNumber(int colorNumber) {this.colorNumber = colorNumber;}


	public String getID() {
		return placeId;
	}

	public void setID(String ID) {
		this.placeId = ID;
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

    @Ignore
    public Place( String name, String review, String address, double latitude,double longitude, Date createdAt){
	    this.Name = name;
	    this.review = review;
	    this.address = address;
	    this.latitude = latitude;
	    this.longitude = longitude;
	    this.createdAt = createdAt;
    }

    public Place(String review){
        this.review = review;
    }


    public Place(){

    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
