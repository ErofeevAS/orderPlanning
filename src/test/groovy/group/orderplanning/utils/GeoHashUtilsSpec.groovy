package group.orderplanning.utils

import group.itechart.orderplanning.utils.GeoHashUtils
import spock.lang.Specification

class GeoHashUtilsSpec extends Specification {


    def "should return values between south-west and north-east symbols"(String s1, String s2, boolean isOdd, List<String> result) {

        expect:
        GeoHashUtils.getGeoHashesSymbolsBetween(s1, s2, isOdd) == result

        where:
        s1  | s2  | isOdd | result
        "s" | "7" | false | ["7", "s", "e", "k"].sort()
        "f" | "9" | false | ["9", "d", "c", "f"].sort()
        "d" | "8" | false | ["d", "9", "8"].sort()
        "z" | "0" | false | ["0", "1", "4", "5", "h", "j", "n", "p",
                             "2", "3", "6", "7", "k", "m", "q", "r", "8", "9", "d", "e", "s", "t", "w", "x",
                             "b", "c", "f", "g", "u", "v", "y", "z"].sort()
        "t" | "7" | true  | ["t", "m", "s", "k", "e", "7"].sort()

    }


    def "should return position of symbol"(String symbol, int[] result, boolean isOdd) {
        expect:
        GeoHashUtils.getSymbolPosition(symbol, isOdd) == result

        where:
        symbol | isOdd | result
        "u"    | false | [4, 3]
        "9"    | false | [1, 2]
        "0"    | false | [0, 0]
        "z"    | false | [7, 3]
        "t"    | true  | [2, 5]
        "s"    | false | [4, 2]
        "s"    | true  | [2, 4]

    }

    def "should return geoHashes in radius"(double lat, double lon, double radius) {

        expect:
        GeoHashUtils.getCommonGeoHashForCircle(lat, lon, radius) != result

        where:
        lat                | lon                | radius             | result
        54.076235825729185 | 26.783177389068793 | 0.2744             | Collections.emptyList()
        54.076235825729185 | 26.783177389068793 | 2.8925465497599983 | Collections.emptyList()

    }
}