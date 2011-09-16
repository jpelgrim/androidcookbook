package nl.codestone.cookbook.mylocationoverlayexample;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MainActivity extends MapActivity {
    
    private MyLocationOverlayExtension mMyLocationOverlay;
    private MapController mMapController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);

        mMyLocationOverlay = new MyLocationOverlayExtension(this, mapView);

        mapView.getOverlays().add(mMyLocationOverlay);
        mapView.postInvalidate();

        mMapController = mapView.getController();
        mMapController.setZoom(19);
        mMyLocationOverlay.runOnFirstFix(new Runnable() {
            @Override
            public void run() {
                mMapController.animateTo(mMyLocationOverlay.getMyLocation());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMyLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMyLocationOverlay.disableMyLocation();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    private class MyLocationOverlayExtension extends MyLocationOverlay {

        public MyLocationOverlayExtension(Context context, MapView mapView) {
            super(context, mapView);
        }
        
        @Override
        public synchronized void onLocationChanged(Location location) {
            super.onLocationChanged(location);
            GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
            mMapController.animateTo(point);
        }
        
    }
    
}