package searchlocation.miniproject01.UI.Editor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;


import searchlocation.miniproject01.Models.Place;
import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.UI.Database.AppDatabase;

public class PlaceViewModel extends ViewModel {

    private static final String TAG =PlaceViewModel.class.getSimpleName();

    private LiveData<Place> place;

    public PlaceViewModel(AppDatabase database, int placeId) {
        Log.d(TAG,"Actively retrieving the tasks from Database");
        place = database.placeDao().loadPlaceById(placeId);
    }


    public LiveData<Place> getPlace() {
        return place;
    }
}
