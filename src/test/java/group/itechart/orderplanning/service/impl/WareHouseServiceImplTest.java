package group.itechart.orderplanning.service.impl;

import static group.itechart.orderplanning.utils.GeoHashUtils.getSymbolPosition;
import static group.itechart.orderplanning.utils.GeoHashUtils.getGeoHashesSymbolsBetween;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import ch.hsr.geohash.GeoHash;


class WareHouseServiceImplTest {

	@Test
	public void test() {

		//		double lat1 = 53.893009;
		//		double lat2 = 53.669353;
		//		double lat3 = 55.1904;
		//		double lat4 = 52.097622;
		//
		//		double long1 = 27.567444;
		//		double long2 = 23.813131;
		//		double long3 = 30.2049;
		//		double long4 = 23.734051;

		double lat1 = 54.07278224248743;
		double lat2 = 54.08043652509298;
		double lat3 = 53.905431793981386;
		double lat4 = 54.221987038416046;
		double lat5 = 53.908162190614625;

		double long1 =  26.680091901650176;
		double long2 = 26.795448346962676;
		double long3 = 27.02135471903299;
		double long4 = 27.02135471903299;
		double long5 = 27.578313831204888;

		String geoHash1 = GeoHash.geoHashStringWithCharacterPrecision(lat1, long1, 12);
		String geoHash2 = GeoHash.geoHashStringWithCharacterPrecision(lat2, long2, 12);
		String geoHash3 = GeoHash.geoHashStringWithCharacterPrecision(lat3, long3, 12);
		String geoHash4 = GeoHash.geoHashStringWithCharacterPrecision(lat4, long4, 12);
		String geoHash5 = GeoHash.geoHashStringWithCharacterPrecision(lat5, long5, 12);

		TreeMap<String, Long> tree = new TreeMap<>();

		//		tree.put(geoHash1, 1L);
		//		tree.put(geoHash2, 2L);
		//		tree.put(geoHash3, 3L);
		//		tree.put(geoHash5, 5L);

		tree.put("u9e5r6x6", 1L); //shop1
		tree.put("u9e5pe0k", 2L); //shop2
		tree.put("u9dgxs4g", 3L); //shop3
		//		tree.put(geoHash5, 5L);

		geoHash4 = "u9e59kqrq"; // client

		final SortedMap<String, Long> head = tree.headMap(geoHash4);
		final SortedMap<String, Long> tail = tree.tailMap(geoHash4);

		double clientLat = 54.07399090725811;
		double clientLong = 26.797508283486113;

//		final String commonGeoHashForCircle = getCommonGeoHashForCircle(clientLat, clientLong, 5);

//		final int[] es = getSymbolPosition("e");

//		final List<String> symbolsBetween = getGeoHashesSymbolsBetween("c", "d");

		assertEquals("123", "11");
	}


}