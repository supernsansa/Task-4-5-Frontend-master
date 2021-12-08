//package asesix.sussex;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import javax.inject.Inject;
//
//import asesix.sussex.util.SharedPreference.SharedPreferenceModule;
//import asesix.sussex.util.SharedPreference.sharedPreferenceInterface.DaggerSharedPreferenceComponent;
//import asesix.sussex.util.SharedPreference.sharedPreferenceInterface.SharedPreferenceComponent;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    EditText editText;
//    TextView textView;
//    Button saveBtn, getBtn;
//    private SharedPreferenceComponent sharedPreferenceComponent;
//
//    // @Inject is used to tell which activity,
//    // fragment or service is allowed to request
//    // dependencies declared in Module class
//    @Inject
//    SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Referencing the EditText, TextView and Buttons
//        editText = (EditText) findViewById(R.id.inputField);
//        textView = (TextView) findViewById(R.id.outputField);
//        saveBtn = (Button) findViewById(R.id.saveBtn);
//        getBtn = (Button) findViewById(R.id.getBtn);
//
//        // Setting onClickListener behavior on button to reference
//        // to the current activity(this MainActivity)
//        saveBtn.setOnClickListener(this);
//        getBtn.setOnClickListener(this);
//
//        // Here we are binding dagger to our application
//        // Dagger keyword will be prefix to the component name
//        sharedPreferenceComponent = DaggerSharedPreferenceComponent.builder().sharedPreferenceModule(
//                new SharedPreferenceModule(this)).build();
//
//        // we are injecting the shared preference dependent object
//        sharedPreferenceComponent.inject(this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.saveBtn:
//                // Saving data to shared preference
//                // inputField acts as key and editText data as value to that key
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("inputField", editText.getText().toString().trim());
//                editor.apply();
//                break;
//            case R.id.getBtn:
//                // getting shared preferences data and set it to textview
//                // s1: is the default string, You can write any thing there or leave it
//                textView.setText(sharedPreferences.getString("inputField", ""));
//                break;
//        }
//    }
//}
