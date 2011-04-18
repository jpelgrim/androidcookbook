package nl.codestone.cookbook.maptest;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class MapTest extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MapView mapview = (MapView) findViewById(R.id.mapview);
        mapview.setBuiltInZoomControls(true); 
    
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}