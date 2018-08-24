package searchlocation.miniproject01.UI.Editor;

import android.arch.lifecycle.ViewModelProvider;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import searchlocation.miniproject01.UI.Database.AppDatabase;


public class PlaceViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int placeId;

    public PlaceViewModelFactory(AppDatabase database, int placeId) {
        this.mDb = database;
        this.placeId = placeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PlaceViewModel(mDb,placeId);
    }
}
