package asesix.sussex.util.SharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

// @Module annotation is used over the class that
// creates construct object and provides dependencies
@Module
public class SharedPreferenceModule {
    private final Context context;

    // Context gets initialize from the constructor itself
    public SharedPreferenceModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return context;
    }

    // @Singleton indicates that only single instance
    // of dependency object is created
    // @Provide annotations used over the methods that
    // will provides the object of module class
    // This method will return the dependent object
    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
