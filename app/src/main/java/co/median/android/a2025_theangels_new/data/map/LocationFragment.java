// =======================================
// IMPORTS
// =======================================
package co.median.android.a2025_theangels_new.data.map;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import co.median.android.a2025_theangels_new.data.map.MapStyleHelper;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

import co.median.android.a2025_theangels_new.R;
import co.median.android.a2025_theangels_new.ui.events.create.NewEventViewModel;
import androidx.lifecycle.ViewModelProvider;

// =======================================
// LocationFragment - Displays a map with optional manual address input
// =======================================
public class LocationFragment extends Fragment implements OnMapReadyCallback {

    // =======================================
    // VARIABLES
    // =======================================
    private GoogleMap mMap;
    private LocationService locationService;
    private Marker locationMarker;
    private LinearLayout locationBox;
    private TextView tvAddress;
    private LinearLayout manualInputContainer;
    private TextInputEditText etManualAddress;
    private Button btnManualLocation;
    private Button btnConfirmManual;
    private Button btnUseCurrent;
    private TextView tvManualMode;
    private boolean manualMode = false;
    private LatLng manualLatLng;

    private ListView lvAddressSuggestions;
    private ArrayAdapter<String> suggestionsAdapter;
    private final java.util.ArrayList<String> suggestionList = new java.util.ArrayList<>();
    private final java.util.Map<String, String> suggestionIdMap = new java.util.HashMap<>();
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;

    private NewEventViewModel viewModel;

    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showMap();
                } else {
                    showPermissionBox();
                }
            });

    // =======================================
    // onCreateView - Inflates the layout for the fragment
    // =======================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    // =======================================
    // onViewCreated - Initializes map and manual location logic
    // =======================================
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(NewEventViewModel.class);

        manualInputContainer = view.findViewById(R.id.manual_input_container);
        etManualAddress = view.findViewById(R.id.etManualAddress);
        btnManualLocation = view.findViewById(R.id.btnManualLocation);
        btnConfirmManual = view.findViewById(R.id.btnConfirmManual);
        btnUseCurrent = view.findViewById(R.id.btnUseCurrent);
        tvManualMode = view.findViewById(R.id.tv_manual_mode);
        locationBox = view.findViewById(R.id.location_box);
        tvAddress = view.findViewById(R.id.tv_current_address);
        locationService = new LocationService(requireContext());

        lvAddressSuggestions = view.findViewById(R.id.lvAddressSuggestions);
        suggestionsAdapter = new ArrayAdapter<>(requireContext(), R.layout.address_suggestion_item, suggestionList);
        lvAddressSuggestions.setAdapter(suggestionsAdapter);
        lvAddressSuggestions.setOnItemClickListener((parent, v1, position, id) -> {
            String address = suggestionList.get(position);
            String placeId = suggestionIdMap.get(address);
            handleSuggestionSelection(address, placeId);
        });

        AutocompleteHelper.initPlaces(requireContext());
        if (Places.isInitialized()) {
            placesClient = Places.createClient(requireContext());
            sessionToken = AutocompleteSessionToken.newInstance();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnManualLocation.setOnClickListener(v -> {
            if (manualInputContainer.getVisibility() == View.GONE) {
                manualInputContainer.setVisibility(View.VISIBLE);
            } else {
                manualInputContainer.setVisibility(View.GONE);
                lvAddressSuggestions.setVisibility(View.GONE);
            }
        });

        btnConfirmManual.setOnClickListener(v -> handleManualAddress());

        btnUseCurrent.setOnClickListener(v -> {
            manualMode = false;
            manualLatLng = null;
            tvManualMode.setVisibility(View.GONE);
            manualInputContainer.setVisibility(View.GONE);
            lvAddressSuggestions.setVisibility(View.GONE);
            startLocationUpdates();
        });

        etManualAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchAddressSuggestions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        etManualAddress.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleManualAddress();
                return true;
            }
            return false;
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        applyCustomStyle();
        mMap.setOnMapClickListener(this::handleMapSelection);
        mMap.setOnMapLongClickListener(this::handleMapSelection);
        checkPermission();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            showMap();
        } else {
            showPermissionBox();
        }
    }

    private void showMap() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.VISIBLE);
        locationBox.setVisibility(View.GONE);
        if (!manualMode) {
            manualInputContainer.setVisibility(View.GONE);
            tvManualMode.setVisibility(View.GONE);
            lvAddressSuggestions.setVisibility(View.GONE);
            startLocationUpdates();
        }
    }

    private void showPermissionBox() {
        View mapView = requireView().findViewById(R.id.map);
        mapView.setVisibility(View.GONE);
        locationBox.setVisibility(View.VISIBLE);
        tvAddress.setText(getString(R.string.address_not_found));
        lvAddressSuggestions.setVisibility(View.GONE);
    }

    private void startLocationUpdates() {
        locationService.getCurrentLocation(new LocationService.SimpleLocationListener() {
            @Override
            public void onLocation(android.location.Location location) {
                if (!manualMode) {
                    updateLiveLocation(location);
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });

        locationService.startLocationUpdates(new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult result) {
                android.location.Location location = result.getLastLocation();
                if (location != null && !manualMode) {
                    updateLiveLocation(location);
                }
            }
        });
    }

    private void stopLocationUpdates() {
        locationService.stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void updateLiveLocation(android.location.Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        if (locationMarker == null) {
            locationMarker = MapHelper.addMarker(mMap, pos, getString(R.string.your_location));
        } else {
            locationMarker.setPosition(pos);
        }
        MapHelper.moveCamera(mMap, pos, 15f);

        viewModel.setEventLocation(new com.google.firebase.firestore.GeoPoint(pos.latitude, pos.longitude));

        String address = AddressHelper.getAddressFromLatLng(requireContext(), pos.latitude, pos.longitude);
        if (address == null) address = getString(R.string.address_not_found);
        tvAddress.setText(address);
    }

    private void updateManualLocation(LatLng pos, String address) {
        manualLatLng = pos;
        manualMode = true;
        stopLocationUpdates();
        if (locationMarker == null) {
            locationMarker = MapHelper.addMarker(mMap, pos, getString(R.string.your_location));
        } else {
            locationMarker.setPosition(pos);
        }
        MapHelper.moveCamera(mMap, pos, 15f);
        tvAddress.setText(address);
        tvManualMode.setVisibility(View.VISIBLE);
        manualInputContainer.setVisibility(View.GONE);
        etManualAddress.setText(address);
        viewModel.setEventLocation(new com.google.firebase.firestore.GeoPoint(pos.latitude, pos.longitude));
    }

    private void handleManualAddress() {
        String input = etManualAddress.getText() != null ? etManualAddress.getText().toString().trim() : "";
        if (input.isEmpty()) return;

        android.location.Geocoder geocoder = new android.location.Geocoder(requireContext(), new Locale("he"));
        try {
            java.util.List<android.location.Address> list = geocoder.getFromLocationName(input, 1);
            if (list != null && !list.isEmpty()) {
                android.location.Address addr = list.get(0);
                if ("IL".equalsIgnoreCase(addr.getCountryCode())) {
                    LatLng pos = new LatLng(addr.getLatitude(), addr.getLongitude());
                    updateManualLocation(pos, addr.getAddressLine(0));
                    Toast.makeText(requireContext(), R.string.location_updated, Toast.LENGTH_SHORT).show();
                    manualInputContainer.setVisibility(View.GONE);
                    lvAddressSuggestions.setVisibility(View.GONE);
                    return;
                } else {
                    Toast.makeText(requireContext(), R.string.invalid_israel_address, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(requireContext(), R.string.address_not_found, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(requireContext(), R.string.address_lookup_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAddressSuggestions(String query) {
        if (placesClient == null || query.length() < 3) {
            suggestionList.clear();
            suggestionIdMap.clear();
            suggestionsAdapter.notifyDataSetChanged();
            lvAddressSuggestions.setVisibility(View.GONE);
            return;
        }

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setTypeFilter(TypeFilter.ADDRESS)
                .setCountries(java.util.Collections.singletonList("IL"))
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    suggestionList.clear();
                    suggestionIdMap.clear();
                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        String text = prediction.getFullText(null).toString();
                        suggestionList.add(text);
                        suggestionIdMap.put(text, prediction.getPlaceId());
                    }
                    suggestionsAdapter.notifyDataSetChanged();
                    lvAddressSuggestions.setVisibility(suggestionList.isEmpty() ? View.GONE : View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    lvAddressSuggestions.setVisibility(View.GONE);
                });
    }

    private void handleSuggestionSelection(String address, String placeId) {
        etManualAddress.setText(address);
        lvAddressSuggestions.setVisibility(View.GONE);
        if (placesClient == null || placeId == null) {
            handleManualAddress();
            return;
        }

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, java.util.Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS))
                .setSessionToken(sessionToken)
                .build();
        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    Place place = response.getPlace();
                    LatLng pos = place.getLatLng();
                    if (pos != null) {
                        updateManualLocation(pos, place.getAddress());
                        Toast.makeText(requireContext(), R.string.location_updated, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> handleManualAddress());
    }

    private void handleMapSelection(LatLng pos) {
        String address = AddressHelper.getAddressFromLatLng(requireContext(), pos.latitude, pos.longitude);
        if (address == null) address = getString(R.string.address_not_found);
        updateManualLocation(pos, address);
    }

    private void applyCustomStyle() {
        MapStyleHelper.applyStyle(mMap, requireContext(), R.raw.map_style);
    }
}
