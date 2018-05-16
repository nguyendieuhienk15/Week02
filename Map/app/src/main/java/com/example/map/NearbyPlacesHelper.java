package com.example.map;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class NearbyPlacesHelper {
    public static String getRequestString(LatLng location, int radius, String type) {
        String str_location = "location=" + location.latitude + "," + location.longitude;
        String str_radius = "radius=" + radius;
        String str_key = "key=AIzaSyACo6WccsfmrskqWAVFMBnkNKiNNeQhC58";
        String parameters = str_location + "&" + str_radius + "&" + str_key;
        if (type != null) {
            String str_type = "type=" + type;
            parameters += "&"+str_type;
        }
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/" + output + "?" + parameters;
        return url;
    }

    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
