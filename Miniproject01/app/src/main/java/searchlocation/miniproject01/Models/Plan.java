package searchlocation.miniproject01.Models;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import java.util.List;
import com.bumptech.glide.Glide;

@Entity(tableName = "plan")
public class Plan {

    @ColumnInfo (name = "objectId")
    private String objectId;
    //ID
    @PrimaryKey(autoGenerate = true)
    private int id;
	//URL of Heading Image
	//String headingImage;
	//Title of the plan
    @ColumnInfo(name = "title")
	private String title;
	//Heading of plan
	//Store hashtag about that plan
    @ColumnInfo(name = "tags")
	private String tags;
	//Store description of the plans
    @ColumnInfo(name = "desc")
	private String desc;
	//image
    @Ignore
    private Bitmap image;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] data;
	//Store list of plan one by one
    private String userId;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectId() {
        return objectId;
    }

    @BindingAdapter("headingImage")
	public static void loadImage(ImageView view, String imageUrl) {
		Glide.with(view.getContext())
				.load(imageUrl)
				.into(view);
	}

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image=image;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


    @Ignore
    public Plan( String title, String hashtag, String desc, String userId, byte[] data){
	    this.title = title;
	    this.tags = hashtag;
	    this.desc = desc;
	    this.userId = userId;
	    this.data = data;
    }


    public Plan(){}

}
