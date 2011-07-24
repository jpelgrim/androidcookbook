package nl.codestone.cookbook.mylocationoverlayexample;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class MainActivity extends MapActivity {
    
    private MyLocationOverlay mMyLocationOverlay;
    private MapController mMapController;
    private MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setSatellite(false);

        mMyLocationOverlay = new MyLocationOverlay(this, mMapView);

        mMapView.getOverlays().add(mMyLocationOverlay);
        mMapView.postInvalidate();

        mMapController = mMapView.getController();
        mMapController.setZoom(17);
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
    
}