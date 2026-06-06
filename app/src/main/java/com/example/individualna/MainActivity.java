package com.example.individualna;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.individualna.data.CompanyRepository;
import com.example.individualna.data.api.CompanyDto;
import com.example.individualna.databinding.ActivityMainBinding;
import com.example.individualna.model.Category;
import com.example.individualna.model.Company;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Главна активност. Содржи Application Bar (Toolbar со лого и наслов),
 * TabLayout и ViewPager2. Навигацијата меѓу табовите се прави со клик на таб
 * или со swipe (повлекување со прст) лево/десно.
 *
 * Дополнително (Задача 3): додека апликацијата е отворена ја следи локацијата
 * на корисникот и прикажува Toast кога ќе се приближи на < 50 м до некоја компанија.
 */
public class MainActivity extends AppCompatActivity {

    private static final float NEAR_THRESHOLD_M = 50f;
    private static final float RESET_THRESHOLD_M = 100f;

    private ActivityMainBinding binding;
    private LocationManager locationManager;

    /** Компании со координати, за проверка на близина. */
    private List<Company> companies = new ArrayList<>();

    /** Компании за кои веќе е прикажан Toast (за да не се повторува). */
    private final Set<Long> notifiedIds = new HashSet<>();

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            checkProximity(location);
        }
    };

    private final ActivityResultLauncher<String> requestLocationPermission =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) startLocationUpdates();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.viewPager.setAdapter(new CategoryPagerAdapter(this));

        // Поврзување на табовите со страниците + поставување на насловите на табовите.
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(Category.fromPosition(position).titleResId)
        ).attach();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ensureLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Освежи ги компаниите (со координати) од серверот за проверка на близина.
        CompanyRepository.getAll().enqueue(new Callback<List<CompanyDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<CompanyDto>> call,
                                   @NonNull Response<List<CompanyDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Company> list = new ArrayList<>();
                    for (CompanyDto dto : response.body()) list.add(dto.toCompany());
                    companies = list;
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CompanyDto>> call, @NonNull Throwable t) {
                // Тивко: листите по табови сами пријавуваат грешка ако серверот е недостапен.
            }
        });
        if (hasLocationPermission()) startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }

    // --- Геолокација ---

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void ensureLocationPermission() {
        if (!hasLocationPermission()) {
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void startLocationUpdates() {
        if (!hasLocationPermission()) return;
        try {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0f, locationListener);
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000L, 0f, locationListener);
            }
        } catch (SecurityException ignored) {
            // Нема дозвола — игнорирај.
        }
    }

    /** За секоја компанија со координати проверува дали корисникот е < 50 м. */
    private void checkProximity(Location userLocation) {
        for (Company company : companies) {
            Double lat = company.getLatitude();
            Double lon = company.getLongitude();
            if (lat == null || lon == null) continue;

            float[] result = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), lat, lon, result);
            float distance = result[0];

            if (distance < NEAR_THRESHOLD_M) {
                if (notifiedIds.add(company.getId())) {
                    Toast.makeText(this,
                            "Близу сте до " + company.getName() + " (" + (int) distance + " м)",
                            Toast.LENGTH_LONG).show();
                }
            } else if (distance > RESET_THRESHOLD_M) {
                // Хистереза: дозволи повторно известување откако ќе се оддалечи.
                notifiedIds.remove(company.getId());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(this, AddCompanyActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
