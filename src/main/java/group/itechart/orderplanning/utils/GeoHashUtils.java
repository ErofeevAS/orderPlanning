package group.itechart.orderplanning.utils;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import java.util.List;
import java.util.stream.Collectors;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import ch.hsr.geohash.queries.GeoHashCircleQuery;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GeoHashUtils {

	private GeoHashUtils() {
		throw new UnsupportedOperationException();
	}

	public static List<String> getCommonGeoHashForCircle(double lat, double longitude, double radius, int accuracy) {
		final WGS84Point center = new WGS84Point(lat, longitude);
		GeoHashCircleQuery circleQuery = new GeoHashCircleQuery(center, 1000 * radius);
		final List<GeoHash> searchHashes = circleQuery.getSearchHashes();
		return emptyIfNull(searchHashes).stream().map(hash -> convertWGS84PointToGeoHash(hash.getOriginatingPoint(), accuracy))
				.collect(Collectors.toList());
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

	private static String convertWGS84PointToGeoHash(WGS84Point point, int accuracy) {
		return GeoHash.geoHashStringWithCharacterPrecision(point.getLatitude(), point.getLongitude(), accuracy);
	}

}
