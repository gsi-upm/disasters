
package gsi.twitter;

/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class implements methods to get street address from lat-lon using
 * reverse geocoding API through HTTP.
 *
 * @author chaitanyag@google.com (Chaitanya Gharpure)
 * @author al.lopezf (modify original Chaitanya code for our interests)
 */
public class StreetLocator {



  private static final String ENCODING = "UTF-8";

  // URL for obtaining navigation directions
  private static final String URL_NAV_STRING = "http://maps.google.com/maps/nav?";
  // URL for obtaining reverse geocoded location
  private static final String URL_GEO_STRING = "http://maps.google.com/maps/geo?";

  /**
   * Queries the map server and obtains the reverse geocoded address of the
   * specified location.
   *
   * @param lat The latitude in degrees
   * @param lon The longitude in degrees
   * @return Returns the reverse geocoded address
   */
  public String getAddress(double lat, double lon) {
    try {
      String resp = getResult(makeGeoURL(lat, lon));
      JSONObject jsonObj = new JSONObject(resp);
      int code = jsonObj.getJSONObject("Status").getInt("code");
      if (code == 200) {
        return extendShorts(jsonObj.getJSONArray("Placemark").getJSONObject(0).getString("address"));
      }
    } catch (MalformedURLException mue) {
      //Log.d("Locator", "Malformed URL: " + mue.getMessage());
    } catch (IOException e) {
      //Log.d("Locator", "Error reading from Map server: " + e.toString());
    } catch (JSONException e) {
     // Log.d("Locator", "Error in JSON response: " + e.getMessage());
    }
    return null;
  }

  /**
   * Parses the JSON response to extract the street name.
   *
   * @param resp The String representation of the JSON response
   * @return Returns the street name
   * @throws JSONException
   */
  private String parseStreetName(String resp) throws JSONException {
    JSONObject jsonObj = new JSONObject(resp);
    int code = jsonObj.getJSONObject("Status").getInt("code");
    if (code == 200) {
      return extendShorts(jsonObj.getJSONArray("Placemark").getJSONObject(0).getString("address"));
    }
    return null;
  }

  /**
   * Sends a request to the specified URL and obtains the result from the sever.
   *
   * @param url The URL to connect to
   * @return the server response
   * @throws IOException
   */
  private String getResult(URL url) throws IOException {
   // Log.d("Locator", url.toString());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoInput(true);
    conn.setDoOutput(true);
    InputStream is = conn.getInputStream();
    String result = toString(is);
    return result;
  }

  /**
   * Prepares the URL to connect to navigation server, from the specified start
   * and end location coordinates
   *
   * @param lat1 Start location latitude in degrees
   * @param lon1 Start location longitude in degrees
   * @param lat2 End location latitude in degrees
   * @param lon2 End location longitude in degrees
   * @return a well-formed URL
   * @throws MalformedURLException
   */
  private URL makeNavURL(double lat1, double lon1, double lat2, double lon2)
      throws MalformedURLException {
    StringBuilder url = new StringBuilder();
    url.append(URL_NAV_STRING).append("hl=EN&gl=EN&output=js&oe=utf8&q=from%3A").append(lat1)
        .append(",").append(lon1).append("+to%3A").append(lat2).append(",").append(lon2);
    return new URL(url.toString());
  }

  /**
   * Prepares the URL to connect to the reverse geocoding server from the
   * specified location coordinates.
   *
   * @param lat latitude in degrees of the location to reverse geocode
   * @param lon longitude in degrees of the location to reverse geocode
   * @return URL The Geo URL created based on the given lat/lon
   * @throws MalformedURLException
   */
  private URL makeGeoURL(double lat, double lon) throws MalformedURLException {
    StringBuilder url = new StringBuilder();
    url.append(URL_GEO_STRING).append("q=").append(lat).append(",").append(lon);
    return new URL(url.toString());
  }

  /**
   * Reads an InputStream and returns its contents as a String.
   *
   * @param inputStream The InputStream to read from.
   * @return The contents of the InputStream as a String.
   */
  private static String toString(InputStream inputStream) throws IOException {
    StringBuilder outputBuilder = new StringBuilder();
    String string;
    if (inputStream != null) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING));
      while (null != (string = reader.readLine())) {
        outputBuilder.append(string).append('\n');
      }
    }
    return outputBuilder.toString();
  }

  /**
   * Replaces the short forms in the address by their longer forms, so that TTS
   * speaks the addresses properly
   *
   * @param addr The address from which to replace short forms
   * @return the modified address string
   */
  private String extendShorts(String addr) {
    addr = addr.replace("St,", "Street");
    addr = addr.replace("St.", "Street");
    addr = addr.replace("Rd", "Road");
    addr = addr.replace("Fwy", "Freeway");
    addr = addr.replace("Pkwy", "Parkway");
    addr = addr.replace("Blvd", "Boulevard");
    addr = addr.replace("Expy", "Expressway");
    addr = addr.replace("Ave", "Avenue");
    addr = addr.replace("Dr", "Drive");
    return addr;
  }

}
