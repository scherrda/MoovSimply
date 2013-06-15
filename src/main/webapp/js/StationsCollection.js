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
    }

});

var stations = new StationsCollection();