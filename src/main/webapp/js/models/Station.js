/** Converts numeric degrees to radians */
if (typeof(Number.prototype.toRad) === "undefined") {
    Number.prototype.toRad = function () {
        return this * Math.PI / 180;
    }
}

var Station = Backbone.Model.extend({

    defaults: {
    },

    url: function () {
        return '/rest/moovin/' + this.get("serviceType").toLowerCase() + '/' + this.get("stationId");
    },
    updateDistance : function(point){
        this.set("distance", 1000*this.calculateKmDistance(point.lat(), point.lng()));
    },

    calculateKmDistance: function (lat, lng) {
        var R = 6371;
        var dLat = (lat - this.get("latitude")).toRad();
        var dLon = (lng - this.get("longitude")).toRad();
        var lat1 = lat.toRad();
        var lat2 = this.get("latitude").toRad();

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        var d = R * c;
        return d;
    }
});
