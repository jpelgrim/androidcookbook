package nl.codestone.cookbook.multiplelocations;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
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
        
        addOverlayItem(52372991, 4892655, "Amsterdam");
        addOverlayItem(51924216, 4481776, "Rotterdam");
        addOverlayItem(52090142, 5109665, "Utrecht");
        
        mapOverlays.add(itemizedOverlay);
        
        MapController mc = mapView.getController();
        mc.setZoom(8);
        mc.animateTo(new GeoPoint(52090142, 5109665));
        
    }

    private void addOverlayItem(int lat, int lon, String title) {
        GeoPoint point = new GeoPoint(lat, lon);
        OverlayItem overlayitem = new OverlayItem(point, title, "");

        itemizedOverlay.addOverlay(overlayitem);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}