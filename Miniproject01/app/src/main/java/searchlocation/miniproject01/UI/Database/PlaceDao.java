package searchlocation.miniproject01.UI.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import searchlocation.miniproject01.Models.Place;

@Dao
public interface PlaceDao {
    @Query("SELECT * FROM place ORDER BY created_at")
    LiveData<List<Place>> loadAllPlaces();

    @Insert
    void insertPlace(Place place);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlace(Place place);

    @Delete
    void deletePlace(Place place);

    @Query("SELECT * FROM place WHERE id = :id")
    LiveData<Place> loadPlaceById(int id);
}
