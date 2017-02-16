package tourist.android.com.newproject;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;

public class MyLocationListener implements LocationListener {

    private LocationCallbackChanged changed;
    private Activity context;

    public MyLocationListener(Activity context, LocationCallbackChanged callbackChanged) {
        this.changed = callbackChanged;
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location loc) {
        if(changed != null) {
            changed.onChanged(loc.getLatitude(), loc.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public interface LocationCallbackChanged {
        public void onChanged(double lat, double lng);
    }
}
