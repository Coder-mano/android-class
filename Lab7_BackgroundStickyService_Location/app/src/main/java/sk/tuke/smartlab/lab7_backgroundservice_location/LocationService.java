package sk.tuke.smartlab.lab7_backgroundservice_location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

public class LocationService extends Service {

    Intent broadcastIntent;
    private LocationManager lm;
    private LocationListener ll;
    public static final String BROADCAST_ACTION_NAME = "location";
    public static final String BROADCAST_INTENT_LAT = "latitude";
    public static final String BROADCAST_INTENT_LON = "longitude";
    public static boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastIntent = new Intent();
        ll = new LocListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 15, ll);
        }
        isRunning = true;
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class LocListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            broadcastIntent.putExtra(BROADCAST_INTENT_LAT,location.getLatitude());
            broadcastIntent.putExtra(BROADCAST_INTENT_LON,location.getLongitude());
            broadcastIntent.setAction(BROADCAST_ACTION_NAME);
            LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(broadcastIntent);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(ll);
        isRunning = false;
    }
}
