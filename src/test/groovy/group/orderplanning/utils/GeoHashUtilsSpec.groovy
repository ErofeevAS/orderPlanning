package group.orderplanning.utils

import group.itechart.orderplanning.utils.GeoHashUtils
import spock.lang.Specification

class GeoHashUtilsSpec extends Specification {

    def "should return geoHashes in radius"(double lat, double lon, double radius) {

        expect:
        GeoHashUtils.getCommonGeoHashForCircle(lat, lon, radius,9).size() > result

        where:
        lat                | lon                | radius | result
        54.076235825729185 | 26.783177389068793 | 1      | 0
        54.076235825729185 | 26.783177389068793 | 50     | 0

    }

    def "distance between shops and client"(String geo1, String geo2) {
        expect:
        Math.round(GeoHashUtils.calculateDistance(geo1, geo2)) == (result)

        where:
        geo1        | geo2        | result
        'u9dgxs4gu' | 'u9e59kqbv' | 5
        'u9e5r3ts'  | 'u9e59kqbv' | 18
        'u9e5pe7jx' | 'u9e59kqbv' | 20
    }
}