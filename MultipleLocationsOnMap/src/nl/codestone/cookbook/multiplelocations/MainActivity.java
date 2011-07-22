package nl.codestone.cookbook.multiplelocations;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

    List<Overlay> mapOverlays;
    Drawable drawable;
    MyItemizedOverlay itemizedOverlay;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);

        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.marker_default);
        itemizedOverlay = new MyItemizedOverlay(drawable);        
        
        addOverlayItem(52372991, 4892655, "Windmill", "Amsterdam", R.drawable.windmill);
        addOverlayItem(51501851, -140623, "Big Ben", "London", R.drawable.big_ben);
        addOverlayItem(48857522, 2294496, "Eiffel Tower", "Paris", R.drawable.eiffel_tower);
        
        mapOverlays.add(itemizedOverlay);
        
        MapController mc = mapView.getController();
        mc.setCenter(new GeoPoint(51035349,2370987));
        mc.zoomToSpan(itemizedOverlay.getLatSpanE6(), itemizedOverlay.getLonSpanE6());
        
    }

    private void addOverlayItem(int lat, int lon, String snippet, String title, Integer altMarker) {
        GeoPoint point = new GeoPoint(lat, lon);
        OverlayItem overlayitem = new OverlayItem(point, title, snippet);
        if (altMarker != null) {
            Drawable marker = getResources().getDrawable(altMarker);
            itemizedOverlay.addOverlay(overlayitem, marker);
        } else {
            itemizedOverlay.addOverlay(overlayitem);
        }
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

        private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

        public MyItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
        }

        public void addOverlay(OverlayItem overlay) {
            mOverlays.add(overlay);
            populate();
        }

        public void addOverlay(OverlayItem overlay, Drawable altMarker) {
            overlay.setMarker(boundCenterBottom(altMarker));
            addOverlay(overlay);
        }
        
        @Override
        protected OverlayItem createItem(int i) {
            return mOverlays.get(i);
        }

        @Override
        public int size() {
            return mOverlays.size();
        }
        
        @Override
        protected boolean onTap(int index) {
            Toast.makeText(MainActivity.this, getItem(index).getTitle(), Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this, getItem(index).getSnippet(), Toast.LENGTH_LONG).show();
            return true;
        }

    }
    
}