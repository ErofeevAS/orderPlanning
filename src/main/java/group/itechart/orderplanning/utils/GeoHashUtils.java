package group.itechart.orderplanning.utils;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.geohash.GeoHash;


public class GeoHashUtils {

	private GeoHashUtils() {
		throw new UnsupportedOperationException();
	}

	private static final Integer accuracy = 12;

	private static final String[][] zOrder = { { "0", "1", "4", "5", "h", "j", "n", "p" },
			{ "2", "3", "6", "7", "k", "m", "q", "r" }, { "8", "9", "d", "e", "s", "t", "w", "x" },
			{ "b", "c", "f", "g", "u", "v", "y", "z" } };

	public static int[] getSymbolPosition(String str) {
		int[] array = new int[2];
		for (int i = 0; i < zOrder.length; i++) {
			for (int j = 0; j < zOrder[0].length; j++) {
				if (str.equals(zOrder[i][j])) {
					array[0] = j;
					array[1] = i ;
					return array;
				}
			}
		}
		return array;
	}

	public static List<String> getSymbolsBetween(String s1, String s2) {
		final int[] symbolPosition1 = getSymbolPosition(s1);
		int x1 = symbolPosition1[0];
		int y1 = symbolPosition1[1];
		final int[] symbolPosition2 = getSymbolPosition(s2);
		int x2 = symbolPosition2[0];
		int y2 = symbolPosition2[1];

		List<String> symbols = new ArrayList<>();

		for (int i = 0; i < zOrder.length; i++) {
			for (int j = 0; j < zOrder[0].length; j++) {
				if (i >= x1 && i <= x2 && j >= y2 && j <= y1) {
					symbols.add(zOrder[i][j]);
				}
			}
		}

		return symbols;
	}

	public static String getCommonGeoHashForCircle(double lat, double longitude, double radius) {

		double maxLongitude = getLongitude(longitude, radius, lat);
		double minLongitude = getLongitude(longitude, -radius, lat);
		double maxLatitude = getLatitude(lat, radius);
		double minLatitude = getLatitude(lat, -radius);

		String geoHashForNorthEastPoint = GeoHash.geoHashStringWithCharacterPrecision(maxLatitude, maxLongitude, accuracy);
		String geoHashForNorthWestPoint = GeoHash.geoHashStringWithCharacterPrecision(minLatitude, maxLongitude, accuracy);
		String geoHashForSouthWestPoint = GeoHash.geoHashStringWithCharacterPrecision(minLatitude, minLongitude, accuracy);
		String geoHashForSouthEastPoint = GeoHash.geoHashStringWithCharacterPrecision(maxLatitude, minLongitude, accuracy);

		System.out.println(geoHashForNorthEastPoint);
		System.out.println(geoHashForNorthWestPoint);
		System.out.println(geoHashForSouthWestPoint);
		System.out.println(geoHashForSouthEastPoint);

		StringBuilder commonGeoHash = new StringBuilder();

		final char[] ne = geoHashForNorthEastPoint.toCharArray();
		for (int i = 0; i < ne.length; i++) {
			final char nw = geoHashForNorthWestPoint.charAt(i);
			final char sw = geoHashForSouthWestPoint.charAt(i);
			final char se = geoHashForSouthEastPoint.charAt(i);

			if (ne[i] == nw && sw == se && nw == se) {
				commonGeoHash.append(ne[i]);
			}
			else {
				return commonGeoHash.toString();
			}
		}
		return commonGeoHash.toString();

	}

	private static double getLatitude(double lat1, double distance) {
		double radius = 6371;
		return Math.toDegrees(distance / radius) + lat1;

	}

	private static double getLongitude(double longitude, double distance, double lat) {
		double radius = 6371;
		return Math.toDegrees(distance / radius / Math.cos(Math.toRadians(lat))) + longitude;
	}

	public static double calculateDistance(String geohash1, String geohash2) {
		final GeoHash geoHash1 = GeoHash.fromGeohashString(geohash1);
		final GeoHash geoHash2 = GeoHash.fromGeohashString(geohash2);
		final double latitude1 = geoHash1.getOriginatingPoint().getLatitude();
		final double longitude1 = geoHash1.getOriginatingPoint().getLongitude();
		final double latitude2 = geoHash2.getOriginatingPoint().getLatitude();
		final double longitude2 = geoHash2.getOriginatingPoint().getLongitude();
		return distance(latitude1, longitude1, latitude2, longitude2);
	}

	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		lon1 = Math.toRadians(lon1);
		lon2 = Math.toRadians(lon2);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		// Haversine formula
		double dlon = lon2 - lon1;
		double dlat = lat2 - lat1;
		double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double r = 6371;
		return (c * r);
	}

}
