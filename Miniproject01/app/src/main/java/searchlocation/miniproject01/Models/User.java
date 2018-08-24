package searchlocation.miniproject01.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class User extends BaseObservable {
	String ID;
	String name;
	String email;
	String profileImage;
	String createdAt;
	String description;

	// profile meta fields are ObservableField, will update the UI
	// whenever a new value is set
	public ObservableField<Long> numberOfFollowers = new ObservableField<>();
	public ObservableField<Long> numberOfPosts = new ObservableField<>();
	public ObservableField<Long> numberOfFollowing = new ObservableField<>();


	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@BindingAdapter({"profileImage"})
	public static void loadImage(ImageView view, String imageUrl) {
		Glide.with(view.getContext())
				.load(imageUrl)
				.apply(RequestOptions.circleCropTransform())
				.into(view);
	}
	@Bindable
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;

	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ObservableField<Long> getNumberOfFollowers() {
		return numberOfFollowers;
	}

	public void setNumberOfFollowers(ObservableField<Long> numberOfFollowers) {
		this.numberOfFollowers = numberOfFollowers;
	}

	public ObservableField<Long> getNumberOfPosts() {
		return numberOfPosts;
	}

	public void setNumberOfPosts(ObservableField<Long> numberOfPosts) {
		this.numberOfPosts = numberOfPosts;
	}

	public ObservableField<Long> getNumberOfFollowing() {
		return numberOfFollowing;
	}

	public void setNumberOfFollowing(ObservableField<Long> numberOfFollowing) {
		this.numberOfFollowing = numberOfFollowing;
	}
}
