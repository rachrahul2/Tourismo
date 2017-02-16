package tourist.android.com.newproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import tourist.android.com.newproject.sql.Place;
import tourist.android.com.newproject.sql.PlaceDataSource;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView txtName, txtType, txtDescription;
    LinearLayout panelImage;
    Button btnLocation;
    Place place;
    GoogleMap map;
    private PlaceDataSource placeDataSource;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        // Init datasource
        placeDataSource = new PlaceDataSource(this);
        placeDataSource.open();

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        txtName = (TextView) findViewById(R.id.txtName);
        txtType = (TextView) findViewById(R.id.txtType);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        panelImage = (LinearLayout)findViewById(R.id.panelImage);

        btnLocation = (Button) findViewById(R.id.btnLocation);
        btnLocation.setTypeface(font);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Criteria criteria = new Criteria();
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;

                if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

                    criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setAltitudeRequired(true);
                    criteria.setBearingRequired(true);
                    criteria.setSpeedRequired(true);

                }
                String provider = locationManager.getBestProvider(criteria, true);
                Location location = locationManager.getLastKnownLocation(provider);
                redrawMap(location.getLatitude(), location.getLongitude());
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 10);
                return;
            } else {
                configLocation();
            }
        } else {
            configLocation();
        }

        // Get data from intent
        Intent intent = getIntent();
        place = (Place) intent.getSerializableExtra("placeId");

        if (place != null) {
            txtName.setText(place.getPlaceName());
            txtType.setText(place.getPlaceType());
            txtDescription.setText(place.getDescription());
            if(place.getImages() != null) {
                String images[] = place.getImages().split(";");
                for(int i = 0; i < images.length; i++) {
                    ImageView imageView = new ImageView(this);
                    imageView.setId(i + 1);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    panelImage.addView(imageView, layoutParams);
                    Picasso.with(this)
                            .load(images[i])
                            .placeholder(R.drawable.common_google_signin_btn_icon_dark_focused)
                            .error(R.drawable.common_google_signin_btn_icon_dark_focused)
                            .resize(convertDipToPixcel(100), convertDipToPixcel(100))
//                            .centerInside()
                            .into(imageView);
                }
            }
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void configLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, new MyLocationListener(PlaceDetailActivity.this, new MyLocationListener.LocationCallbackChanged() {
            @Override
            public void onChanged(double lat, double lng) {
//                Toast.makeText(PlaceDetailActivity.this, "Your location now is: " + lat + " - " + lng, Toast.LENGTH_LONG).show();
                redrawMap(lat, lng);
            }

        }));
    }

    private int convertDipToPixcel(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    private void redrawMap(double lat, double lng) {
        map.clear();
        LatLng placeLocation = new LatLng(place.getLat(), place.getLng());
        LatLng yLocation = new LatLng(lat, lng);
        map.addMarker(new MarkerOptions().position(placeLocation).title(place.getPlaceName()));
        map.addMarker(new MarkerOptions().position(yLocation).title("Your location!"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(yLocation, 8));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configLocation();
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        super.onPause();
        placeDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        placeDataSource.open();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(place != null) {
            LatLng placeLocation = new LatLng(place.getLat(), place.getLng());
            map.addMarker(new MarkerOptions().position(placeLocation).title(place.getPlaceName()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 8));
        }
    }
}
