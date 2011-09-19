package org.swixml.contrib.gmap;


//import Task.Support.CoreSupport.*;

/**
 * MapLookup
 * <p/>
 * http://code.google.com/apis/maps/documentation/staticmaps/index.html
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Apr 16, 2008, 10:55:50 PM
 */
public class MapLookup {

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// constants
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
public static final String GMAP_STATIC_URI = "http://maps.google.com/staticmap";
public static final String GMAP_STATIC_URI_V2 = "https://maps.googleapis.com/maps/api/staticmap";


public static final String GMAP_LICENSE_KEY = "key";

public static final String CENTER_KEY = "center";

public static final String ZOOM_KEY = "zoom";
public static final int ZOOM_DEFAULT = 10;

public static final String SIZE_KEY = "size";
public static final String SIZE_SEPARATOR = "x";
public static final int SIZE_DEFAULT = 512;

public static final String MARKER_SEPARATOR = "|";
public static final String MARKERS_KEY = "markers";

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// data
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
private String GmapLicense = "";

private int SizeMin = 10;
private int SizeMax = SIZE_DEFAULT;

private int ZoomMin = 0;
private int ZoomMax = 19;

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// set the license key
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public String getLicenseKey() {
	return GmapLicense;
}
public void setLicenseKey(String lic) {
  GmapLicense = lic;
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// methods
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

public String getMap(double lat, double lon, int sizeW, int sizeH, int zoom, MapMarker... markers) {
  //return getURI(lat, lon, sizeW, sizeH, zoom, markers);
  return getURI(lat, lon, sizeW, sizeH, zoom, markers);
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// param handling and uri generation
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/*
public String getURI(double lat, double lon, int sizeW, int sizeH, MapMarker... markers) {
	  _validateParams(sizeW, sizeH, ZOOM_DEFAULT);

	  // generate the URI
	  StringBuilder sb = new StringBuilder();
	  sb.append(GMAP_STATIC_URI);

	  // size key
	  sb.
	      append("?").
	      append(SIZE_KEY).append("=").append(sizeW).append(SIZE_SEPARATOR).append(sizeH);

	  // markers key
	  sb.
	      append("&").
	      append(MarkerUtils.toString(markers));

	  // maps key
	  sb.
	      append("&").
	      append(GMAP_LICENSE_KEY).append("=").append(GmapLicense);
	  // maps type
	  sb.
	  append("&").
	  append("sensor").append("=").append("false");


	  return sb.toString();
	}
*/
	public String getURI(double lat, double lon, int sizeW, int sizeH, int zoom, MapMarker... markers) {
	  _validateParams(sizeW, sizeH, zoom);

	  // generate the URI
	  StringBuilder sb = new StringBuilder();
	  sb.append(GMAP_STATIC_URI_V2);

	  // center key
	  sb.
	      append("?").
	      append(CENTER_KEY).append("=").append(lat).append(",").append(lon);

	  // zoom key
	  sb.
	      append("&").
	      append(ZOOM_KEY).append("=").append(zoom);

	  // size key
	  sb.
	      append("&").
	      append(SIZE_KEY).append("=").append(sizeW).append(SIZE_SEPARATOR).append(sizeH);

	  // markers key
	  sb.
	      append("&").
	      append(MarkerUtils.toString(new MapMarker(lat, lon)));

	  // maps key
	  sb.
	      append("&").
	      append(GMAP_LICENSE_KEY).append("=").append(GmapLicense);

	  // maps type
	  /*
	  sb.
	      append("&").
	      append("maptype").append("=").append("hybrid");
	  */
	  sb.
	  append("&").
	  append("sensor").append("=").append("false");
	  
	  return sb.toString();
	}


private void _validateParams(int sizeW, int sizeH, int zoom) {
  /*	
  if (zoom < ZoomMin || zoom > ZoomMax)
    throw new IllegalArgumentException("zoom value is out of range [" + ZoomMin + "-" + ZoomMax + "]");

  if (sizeW < SizeMin || sizeW > SizeMax)
    throw new IllegalArgumentException("width is out of range [" + SizeMin + "-" + SizeMax + "]");

  if (sizeH < SizeMin || sizeH > SizeMax)
    throw new IllegalArgumentException("height is out of range [" + SizeMin + "-" + SizeMax + "]");
  */  
}

//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// actually get the map from Google
//XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
/** use httpclient to get the data */
/*
public static ByteBuffer getDataFromURI(String uri) throws IOException {

  GetMethod get = new GetMethod(uri);

  try {
    new HttpClient().executeMethod(get);
    return new ByteBuffer(get.getResponseBodyAsStream());
  }
  finally {
    get.releaseConnection();
  }

}
*/

/** markers=40.702147,-74.015794,blues|40.711614,-74.012318,greeng&key=MAPS_API_KEY */
public static class MarkerUtils {
  public static String toString(MapMarker... markers) {
    if (markers.length > 0) {
      StringBuilder sb = new StringBuilder();

      sb.append(MARKERS_KEY).append("=");

      for (int i = 0; i < markers.length; i++) {
        sb.append(markers[i].toString());
        if (i != markers.length - 1) sb.append(MARKER_SEPARATOR);
      }

      return sb.toString();
    }
    else {
      return "";
    }
  }
}// class MarkerUtils


}//end class MapLookup
