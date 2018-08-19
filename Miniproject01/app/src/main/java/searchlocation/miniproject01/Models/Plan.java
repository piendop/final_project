package searchlocation.miniproject01.Models;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import java.util.List;
import com.bumptech.glide.Glide;

public class Plan {
	//ID
    String objectId;
	//URL of Heading Image
	//String headingImage;
	//Title of the plan
	private String title;
	//Heading of plan
	//Store hashtag about that plan
	private String tags;
	//Store description of the plans
	private String desc;
	//image
    private Bitmap image;
	//Store list of plan one by one


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

}
