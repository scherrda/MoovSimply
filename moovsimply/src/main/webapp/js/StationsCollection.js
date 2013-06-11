/** Converts numeric degrees to radians */
if (typeof(Number.prototype.toRad) === "undefined") {
    Number.prototype.toRad = function () {
        return this * Math.PI / 180;
    }
}

var StationsCollection = Backbone.Collection.extend({

    url: function () {
        return '/rest/moovin/around?LAT=' + this.lat + '&LNG=' + this.lng;
        //return '../stations.json?LAT=' + this.lat + '&LNG=' + this.lng;
    },

    comparator: function (station) {
        return station.get('distance');
    },

    setCoordinates: function (lat, lng) {
        this.lat = lat;
        this.lng = lng;
        return this;
    },

    parse: function (json) {
        _.each(json, function (station) {
            station.distance = Math.round(1000 * this.calculateKmDistanceBetween(
                station.coordinates.latitude,
                station.coordinates.longitude,
                this.lat,
                this.lng
            ));
        }.bind(this));
        return json;
    },

    calculateKmDistanceBetween: function (lat1, lng1, lat2, lng2) {
        var R = 6371;
        var dLat = (lat2 - lat1).toRad();
        var dLon = (lng2 - lng1).toRad();
        var lat1 = lat1.toRad();
        var lat2 = lat2.toRad();

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c;
        return d;
    }

});

var stations = new StationsCollection();