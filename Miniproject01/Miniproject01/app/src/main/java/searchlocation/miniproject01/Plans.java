package searchlocation.miniproject01;

public class Plans {

	//Store title of the plans
	private String title;
	private String tags;
	//Store description of the plans
	private String desc;
	//Store Id of Header Image of the plans
	private int imgId;


	public Plans(String title, String tags, String desc, int imgId) {
		this.title = title;
		this.tags = tags;
		this.desc = desc;
		this.imgId = imgId;
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

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
}
