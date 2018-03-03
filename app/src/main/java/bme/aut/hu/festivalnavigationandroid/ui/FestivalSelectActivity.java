package bme.aut.hu.festivalnavigationandroid.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import bme.aut.hu.festivalnavigationandroid.R;
import bme.aut.hu.festivalnavigationandroid.model.map.Map;
import bme.aut.hu.festivalnavigationandroid.model.map.MapContainer;
import bme.aut.hu.festivalnavigationandroid.network.NetworkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FestivalSelectActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    // TODO:
    /* Szerver visszaad egy map listát, ha semelyik mapon belül nem tartózkodunk éppen.
       Ilyenkor kiválaszthatjuk, hogy melyik mapot szeretnénk használni, de a navigációt
       le kéne tiltani.
       Ha valamelyik mapon belül tartózkodunk, akkor viszont nem kéne kiváalsztani a fesztivált,
       hanem rögtön azzal a fesztivállal indulhatna el a program.
       Ehhez az Activity indításos részt ki kéne szervezni külön függvénybe, és ha a szerver
       csak egy mapot ad vissza, akkor arra meghívni.
     */

    private static final String TAG = "FestivalSelectActivity";

    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 0;

    private View llFestivalSelect;

    private Map map;

    private Spinner spinnerFestivals;
    private Button btnSelectFestival;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_select);

        llFestivalSelect = findViewById(R.id.llFestivalSelect);

        requestLocationPermission();

        spinnerFestivals = findViewById(R.id.spinnerFestivals);


        btnSelectFestival = findViewById(R.id.btnSelectFestival);
        loadFestivals(0.0, 0.0);
        btnSelectFestival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedFromSpinner();
                proceedToFestival(map);
            }
        });
    }

    /**
     * Getting the list of the festivals from the server.
     *
     * @param lat latitude of the gps location
     * @param lon longitde of the gps location
     */
    private void loadFestivals(double lat, double lon) {
        NetworkManager.getInstance().getMaps(lat, lon).enqueue(new Callback<MapContainer>() {
            @Override
            public void onResponse(Call<MapContainer> call, Response<MapContainer> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        fillUpSpinner(response.body());
                } else {
                    Toast.makeText(FestivalSelectActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MapContainer> call, Throwable t) {
                Toast.makeText(FestivalSelectActivity.this, "Error in network request, check LOG", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Filling up the spinner with elements from the map list.
     *
     * @param maps the map list
     */
    private void fillUpSpinner(MapContainer maps) {
        /*List<String> mapNames = new ArrayList<>();
        for (Map i : maps.getMaps()) {
            mapNames.add(i.getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mapNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFestivals.setAdapter(spinnerAdapter);*/

        // Spinner's adapter needs an array of the objects, instead of a list...
        Map[] spinnerItems = maps.getMaps().toArray(new Map[maps.getMaps().size()]);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFestivals.setAdapter(spinnerAdapter);
    }

    /**
     * Getting the selected festival from the spinner.
     */
    private void getSelectedFromSpinner() {
        map = ((Map) spinnerFestivals.getSelectedItem());
    }

    /**
     * Selecting the festival with the map object
     *
     * @param map the map object
     */
    private void proceedToFestival(Map map) {
        Intent intent = new Intent(FestivalSelectActivity.this, MainActivity.class);
        intent.putExtra("map", map);
        startActivity(intent);
    }

    /**
     * Requests the {@link android.Manifest.permission#ACCESS_FINE_LOCATION} permission.
     * If an additional rationale should be displayed, the user has to launch the request from
     * a SnackBar that includes additional information.
     */
    private void requestLocationPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(llFestivalSelect, R.string.location_access_required, Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Request the permission
                    ActivityCompat.requestPermissions(FestivalSelectActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
                }
            }).show();
        } else {
            Snackbar.make(llFestivalSelect, R.string.location_access_unavailable, Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_ACCESS_FINE_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(llFestivalSelect, R.string.location_access_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                // Permission request was denied.
                Snackbar.make(llFestivalSelect, R.string.location_access_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }
}
