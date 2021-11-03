package contracts.warehouses.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description "should return warehouses when lat, lon and radius are non blank"

    request {
        method GET()
        url("/api/v1/warehouses/nearest") {
            queryParameters {
                parameter("lat", value(regex(nonBlank())))
                parameter("lon", value(regex(nonBlank())))
                parameter("radius", value(regex(nonBlank())))
            }
        }
    }
    response {
        body('''[
    {
        "id": "6181241031168e7425b7dfce",
        "name": "test1",
        "geometry": {
            "x": 53.90734927644237,
            "y": 26.716030191307375,
            "type": "Point",
            "coordinates": [
                53.90734927644237,
                26.716030191307375
            ]
        }
    },
    {
        "id": "6181241031168e7425b7dfcf",
        "name": "test2",
        "geometry": {
            "x": 53.88139327767924,
            "y": 26.73376971980169,
            "type": "Point",
            "coordinates": [
                53.88139327767924,
                26.73376971980169
            ]
        }
    },
    {
        "id": "6181241031168e7425b7dfd0",
        "name": "test3",
        "geometry": {
            "x": 53.87921769422419,
            "y": 26.73376971980169,
            "type": "Point",
            "coordinates": [
                53.87921769422419,
                26.73376971980169
            ]
        }
    }
]''')
        status(200)
        headers {
            contentType('application/json')
        }
    }

}