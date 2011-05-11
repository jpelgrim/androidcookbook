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

    GeoPoint geoPoint = new GeoPoint((int) (52.334822 * 1E6), (int) (4.668907 * 1E6));

    private class MyOverlay extends com.google.android.maps.Overlay {

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            super.draw(canvas, mapView, shadow);

            if (!shadow) {

                Point point = new Point();
                mapView.getProjection().toPixels(geoPoint, point);

                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker_default);

                /** 
                 * Shift it left so the center of the image is aligned with the x-coordinate of the geo point
                 */
                int x = point.x - bmp.getWidth() / 2;

                /** 
                 * Shift it upward so the bottom of the image is aligned with the y-coordinate of the geo point
                 */
                int y = point.y - bmp.getHeight();

                canvas.drawBitmap(bmp, x, y, null);

            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);

        MapController mc = mapView.getController();
        mc.setZoom(18);
        mc.animateTo(geoPoint);

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