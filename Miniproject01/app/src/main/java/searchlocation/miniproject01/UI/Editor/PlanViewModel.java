package searchlocation.miniproject01.UI.Editor;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import searchlocation.miniproject01.Models.Plan;
import searchlocation.miniproject01.UI.Database.AppDatabase;

public class PlanViewModel extends AndroidViewModel {

    private LiveData<Plan> plan;

    private static final String TAG =PlanViewModel.class.getSimpleName();
    public PlanViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG,"Actively retrieving the tasks from Database");
        plan = database.planDao().loadPlan();
    }

    public LiveData<Plan> getPlan() {
        return plan;
    }
}
