package nl.codestone.cookbook.locationonmap;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MainActivity extends MapActivity {

    GeoPoint myLocationGeoPoint = new GeoPoint(52334822, 4668907);

    class MyOverlay extends com.google.android.maps.Overlay {

        @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            super.draw(canvas, mapView, shadow);

            Point myLocationPoint = new Point();
            mapView.getProjection().toPixels(myLocationGeoPoint, myLocationPoint);

            // ---add the marker---
            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker_default);
            int x = myLocationPoint.x - bmp.getWidth() / 2;
            int y = myLocationPoint.y - bmp.getHeight();
            canvas.drawBitmap(bmp, x, y, null);

            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setSatellite(true);
        mapView.setBuiltInZoomControls(true);
        mapView.displayZoomControls(true);

        MapController mc = mapView.getController();
        mc.setZoom(18);
        mc.animateTo(myLocationGeoPoint);

        List<Overlay> overlays = mapView.getOverlays();
        overlays.clear();
        overlays.add(new MyOverlay());

        mapView.invalidate();

    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}