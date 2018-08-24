package searchlocation.miniproject01.UI.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import searchlocation.miniproject01.Models.Plan;

/*@Database(entities = {Plan.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class PlanDatabase extends RoomDatabase {

    private static final String LOG_TAG = PlanDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "todolist";
    private static PlanDatabase sInstance;

    public static PlanDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PlanDatabase.class, PlanDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract PlanDao planDao();

}*/
