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
        if (this.get("serviceType") === 'RATP') {
            if (this.get("stationId") === '69-11349574') { //BUS 69 Breguet-Sabin
                return 'data/ratp-75-11347215.json';
            }
            if (this.get("stationId") === '5-1933') { // Métro 5 Breguet-Sabin
                return 'data/ratp-7-1712.json';
            }
        }
        return '/rest/moovin/' + this.get("serviceType").toLowerCase() + '/' + this.get("stationId");
    },
    updateDistance : function(point){
    	if (point != null){
    		this.set("distance", 1000*this.calculateKmDistance(point.lat(), point.lng()));
    	}else{
    		//position de l'utilisateur inconnue. l'info distance n'a aucun sens !
    		this.set("distance", null);
    	}
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
    },

    parse: function(json) {
        if (json.directions) {
            _.each(json.directions, function(directionTime, index) {
                if (directionTime.time === 'todo') {
                    var now = new Date().getTime();
                    var nextTimes = [2, 5, 6, 9, 14];
                    directionTime.time = new Date(now + nextTimes[index] * 1000 * 60);
                }
            });
        }
        return json;
    }
});
