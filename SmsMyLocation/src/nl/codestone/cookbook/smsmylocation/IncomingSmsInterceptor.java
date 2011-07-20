package nl.codestone.cookbook.smsmylocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class IncomingSmsInterceptor extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return; // nothing to process
        }

        /**
         * The PDU mode offers to send binary information in 7 bit or 8 bit
         * format. That is helpful if you have to send compressed data, binary
         * data or you you like to build your own encoding of the characters in
         * the binary bit stream
         */
        Object[] pdus = (Object[]) extras.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage incomingMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String requestor = incomingMessage.getOriginatingAddress();

            Toast.makeText(context, "Got a message from " + requestor + ": \"" + incomingMessage.getMessageBody() + "\"",
                    Toast.LENGTH_LONG).show();

            if ("Send my location".equalsIgnoreCase(incomingMessage.getMessageBody().trim())) {
                Location lastKnownLocation = getLastKnownLocation(context);
                String lastKnownAddress = getAddressFromLocation(context, lastKnownLocation);
                if (lastKnownLocation != null) {
                    String outgoingMessage = String.format("Your phone was last seen at: %1$s (lat:%2$2.6f, long:%3$2.6f)", lastKnownAddress, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    sendSmsMessage(context, requestor, outgoingMessage);
                } else {
                    sendSmsMessage(context, requestor, "Could not get a location, sorry!");
                }
            }

        }
    }

    public Location getLastKnownLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location == null) {
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            bestProvider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(bestProvider);
        }
        return location;
    }
    
    private String getAddressFromLocation(Context context, Location lastKnownLocation) {
        if (lastKnownLocation == null) return null;
        String address = "";
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geoCoder.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);
            if (addresses.size() > 0) {
                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                    String addressLine = addresses.get(0).getAddressLine(i);
                    if (addressLine != null) {
                        address += addresses.get(0).getAddressLine(i) + "\n";
                    }
                }
            }
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return address;

    }

    

    private void sendSmsMessage(Context context, String requestor, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(requestor, null, message, null, null);
        Toast.makeText(context, "Sending message to " + requestor + ": \"" + message + "\"", Toast.LENGTH_LONG).show();
    }

}