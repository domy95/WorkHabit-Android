package cat.coronout.workhabit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cat.coronout.workhabit.model.Setting;
import cat.coronout.workhabit.util.LocalStorage;

public class MainActivity extends AppCompatActivity {

    LocalStorage localStorage;
    Setting setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get LocalStorage instance
        localStorage = LocalStorage.getInstance(getApplicationContext());

        // Get current settings
        setting = localStorage.getCurrentSetting();

        // Setup data and listeners
        setupMonday();
        setupTuesday();
        setupWednesday();
        setupThursday();
        setupFriday();
        setupSaturday();
        setupSunday();
        setupBirthDate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                break;
            case R.id.reset:
                resetData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetData() {
        setting = localStorage.getCurrentSetting();
        setupMondayData();
        setupTuesdayData();
        setupWednesdayData();
        setupThursdayData();
        setupFridayData();
        setupSaturdayData();
        setupSundayData();
        setupBirthDateData();
    }

    private void setupMonday() {

    }

    private void setupMondayData() {

    }

    private void setupTuesday() {

    }

    private void setupTuesdayData() {

    }

    private void setupWednesday() {

    }

    private void setupWednesdayData() {

    }

    private void setupThursday() {

    }

    private void setupThursdayData() {

    }

    private void setupFriday() {

    }

    private void setupFridayData() {

    }

    private void setupSaturday() {

    }

    private void setupSaturdayData() {

    }

    private void setupSunday() {

    }

    private void setupSundayData() {

    }

    private void setupBirthDate() {

    }

    private void setupBirthDateData() {

    }

}
