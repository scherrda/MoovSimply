var StationsCollection = Backbone.Collection.extend({

    url: function () {
        // return '/around?LAT=&LNG=';
        return '../stations.json?LAT=' + this.lat + '&LNG=' + this.lng;
    },

    setCoordinates: function (lat, lng) {
        this.lat = lat;
        this.lng = lng;
        return this;
    }

});

var stations = new StationsCollection();