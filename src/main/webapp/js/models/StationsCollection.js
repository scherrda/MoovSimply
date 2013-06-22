var StationsCollection = Backbone.Collection.extend({

    url: function () {
        return '/rest/moovin/around?LAT=' + this.lat + '&LNG=' + this.lng;
        //return '../stations.json?LAT=' + this.lat + '&LNG=' + this.lng;
    },

    comparator: function (station) {
        return station.get('distance');
    },

    setCoordinates: function (lat, lng) {
        this.lat = lat == null ? 0 : lat;
        this.lng = lng == null ? 0 : lng;
        return this;
    }

});

var stations = new StationsCollection();