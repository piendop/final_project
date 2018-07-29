package searchlocation.miniproject01.Models;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import java.util.List;
import com.bumptech.glide.Glide;

public class Plan {
	//URL of Heading Image
	String headingImage;
	//Title of the plan
	private String title;
	//Heading of plan
	//Store hashtag about that plan
	private String tags;
	//Store description of the plans
	private String desc;
	//Duration
	private Integer duration;
	//Store list of plan one by one
	private List<Plan> planList;

	@BindingAdapter("headingImage")
	public static void loadImage(ImageView view, String imageUrl) {
		Glide.with(view.getContext())
				.load(imageUrl)
				.into(view);
	}

	public String getHeadingImage() {
		return headingImage;
	}

	public void setHeadingImage(String headingImage) {
		this.headingImage = headingImage;
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

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public List<Plan> getPlanList() {
		return planList;
	}

	public void setPlanList(List<Plan> planList) {
		this.planList = planList;
	}
}
