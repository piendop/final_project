package searchlocation.miniproject01.UI.Editor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;



import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.UI.Database.AppDatabase;

public class ReviewViewModel extends AndroidViewModel {

    private static final String TAG =ReviewViewModel.class.getSimpleName();

    private LiveData<List<Place>> places;

    public ReviewViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG,"Actively retrieving the tasks from Database");
        places = database.placeDao().loadAllPlaces();
    }

    public LiveData<List<Place>> getPlaces() {
        return places;
    }
}
