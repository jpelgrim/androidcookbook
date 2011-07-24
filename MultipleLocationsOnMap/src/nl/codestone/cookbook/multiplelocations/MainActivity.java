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
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);

        Drawable makerDefault = this.getResources().getDrawable(R.drawable.marker_default);
        MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(makerDefault);

        Drawable windmill = getResources().getDrawable(R.drawable.windmill);
        Drawable bigBen = getResources().getDrawable(R.drawable.big_ben);
        Drawable eiffelTower = getResources().getDrawable(R.drawable.eiffel_tower);
        
        itemizedOverlay.addOverlayItem(52372991, 4892655, "Amsterdam", windmill);
        itemizedOverlay.addOverlayItem(51501851, -140623, "London", bigBen);
        itemizedOverlay.addOverlayItem(48857522, 2294496, "Paris", eiffelTower);

        mapView.getOverlays().add(itemizedOverlay);

        MapController mc = mapView.getController();
        mc.setCenter(new GeoPoint(51035349, 2370987)); // Dunkerque, Belgium
        mc.zoomToSpan(itemizedOverlay.getLatSpanE6(), itemizedOverlay.getLonSpanE6());

    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {

        private List<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

        public MyItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
        }

        public void addOverlayItem(int lat, int lon, String title, Drawable altMarker) {
            GeoPoint point = new GeoPoint(lat, lon);
            OverlayItem overlayItem = new OverlayItem(point, title, null);
            addOverlayItem(overlayItem, altMarker);
        }

        public void addOverlayItem(OverlayItem overlayItem) {
            mOverlays.add(overlayItem);
            populate();
        }

        public void addOverlayItem(OverlayItem overlayItem, Drawable altMarker) {
            overlayItem.setMarker(boundCenterBottom(altMarker));
            addOverlayItem(overlayItem);
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
            return true;
        }

    }

}