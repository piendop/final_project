package searchlocation.miniproject01.UI.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import searchlocation.miniproject01.Models.Plan;

@Dao
public interface PlanDao {

    @Query("SELECT * FROM `plan`")
    LiveData<Plan> loadPlan();

    @Insert
    void insertPlan(Plan plan);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlan(Plan plan);

    @Delete
    void deletePlan(Plan plan);

    @Query("DELETE FROM `plan`")
    public void nukeTable();
}
