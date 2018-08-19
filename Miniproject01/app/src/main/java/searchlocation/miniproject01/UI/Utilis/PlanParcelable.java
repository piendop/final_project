package searchlocation.miniproject01.UI.Utilis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import searchlocation.miniproject01.Models.Plan;

public class PlanParcelable implements Parcelable{

    private Plan mPlan;

    protected PlanParcelable(Parcel in) {
        mPlan.setTitle(in.readString());
        mPlan.setDesc(in.readString());
        mPlan.setTags(in.readString());
        mPlan.setImage((Bitmap) in.readParcelable(Bitmap.class.getClassLoader()));
    }

    public static final Creator<PlanParcelable> CREATOR = new Creator<PlanParcelable>() {
        @Override
        public PlanParcelable createFromParcel(Parcel in) {
            return new PlanParcelable(in);
        }

        @Override
        public PlanParcelable[] newArray(int size) {
            return new PlanParcelable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPlan.getTitle());
        dest.writeString(mPlan.getDesc());
        dest.writeString(mPlan.getTags());
        dest.writeParcelable(mPlan.getImage(),0);
    }
}
