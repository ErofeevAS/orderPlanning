package group.itechart.orderplanning.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ch.hsr.geohash.GeoHash;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GeoHashUtils {

	private GeoHashUtils() {
		throw new UnsupportedOperationException();
	}

	private static final Integer accuracy = 12;

	private static final String[][] zOrder = {
			{ "0", "1", "4", "5", "h", "j", "n", "p" },
			{ "2", "3", "6", "7", "k", "m", "q", "r" },
			{ "8", "9", "d", "e", "s", "t", "w", "x" },
			{ "b", "c", "f", "g", "u", "v", "y", "z" }
	};

	private static final String[][] zOrder2 = {
			{ "0", "2", "8", "b" },
			{ "1", "3", "9", "c" },
			{ "4", "6", "d", "f" },
			{ "5", "7", "e", "g" },
			{ "h", "k", "s", "u" },
			{ "j", "m", "t", "v" },
			{ "n", "q", "w", "y" },
			{ "p", "r", "x", "z" }
	};

	private static String[][] matrix = null;


	public static int[] getSymbolPosition(String str, boolean isOdd) {
		int[] array = new int[2];

		if (!isOdd) {
			matrix = zOrder;
		}
		else {
			matrix = zOrder2;
		}
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (str.equals(matrix[i][j])) {
					array[0] = j;
					array[1] = i;
					return array;

				}
			}
		}
		return array;
	}

	public static List<String> getGeoHashesSymbolsBetween(String s1, String s2, boolean isOdd) {
		String temp;
		if (!isOdd) {
			temp = s1;
			s1 = s2;
			s2 = temp;
		}
		final int[] symbolPosition1 = getSymbolPosition(s1, isOdd);
		int x1 = symbolPosition1[0];
		int y1 = symbolPosition1[1];
		final int[] symbolPosition2 = getSymbolPosition(s2, isOdd);
		int x2 = symbolPosition2[0];
		int y2 = symbolPosition2[1];

		List<String> symbols = new ArrayList<>();

		if (!isOdd) {
			matrix = zOrder;
		}
		else {
			matrix = zOrder2;
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				if (!isOdd) {
					if (j >= x1 && j <= x2 && i <= y2 && i >= y1) {
						symbols.add(matrix[i][j]);
					}
				}
				else {
					if (j >= x2 && j <= x1 && i <= y1 && i >= y2) {
						symbols.add(matrix[i][j]);
					}
				}
			}
		}

		Collections.sort(symbols);
		return symbols;
	}

	public static List<String> getCommonGeoHashForCircle(double lat, double longitude, double radius) {

		double maxLongitude = getLongitude(longitude, radius, lat);
		double minLongitude = getLongitude(longitude, -radius, lat);
		double maxLatitude = getLatitude(lat, radius);
		double minLatitude = getLatitude(lat, -radius);

		String geoHashForNorthEastPoint = GeoHash.geoHashStringWithCharacterPrecision(maxLatitude, maxLongitude, accuracy);
		String geoHashForNorthWestPoint = GeoHash.geoHashStringWithCharacterPrecision(minLatitude, maxLongitude, accuracy);
		String geoHashForSouthWestPoint = GeoHash.geoHashStringWithCharacterPrecision(minLatitude, minLongitude, accuracy);
		String geoHashForSouthEastPoint = GeoHash.geoHashStringWithCharacterPrecision(maxLatitude, minLongitude, accuracy);

		log.info(geoHashForNorthEastPoint);
		log.info(geoHashForNorthWestPoint);
		log.info(geoHashForSouthWestPoint);
		log.info(geoHashForSouthEastPoint);

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

				final String additionalSymbolForNE = geoHashForNorthEastPoint.substring(commonGeoHash.length(),
						commonGeoHash.length() + 1);
				final String additionalSymbolForSW = geoHashForSouthWestPoint.substring(commonGeoHash.length(),
						commonGeoHash.length() + 1);

				boolean isOdd = (commonGeoHash.length()+1) % 2 != 0;
				final List<String> additionalSymbols = getGeoHashesSymbolsBetween(additionalSymbolForNE, additionalSymbolForSW,
						isOdd);

				final List<String> geoHashesSymbolsBetween = additionalSymbols.stream().map(x -> commonGeoHash + x)
						.collect(Collectors.toList());

				return geoHashesSymbolsBetween;
			}
		}
		return Collections.emptyList();

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
