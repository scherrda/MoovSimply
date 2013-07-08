var User = Backbone.Model.extend({
    defaults : {
        centerOnMe : true,
        pos : null,
        lastWatchId : null,
    },


    geolocalize: function () {
        if (navigator.geolocation && navigator.geolocation.watchPosition) {
            this.watchId = navigator.geolocation.watchPosition(_.bind(this.updatePosition, this), _.bind(this.errorGeoloc, this), {enableHighAccuracy: true, timeout: 10000, maximumAge: 600000});
            this.set("lastWatchId", this.watchId);
        } else {
            alert('La géolocalisation n’est pas possible avec ce navigateur');
        }
        return this;
    },

    updatePosition: function (position) {
        var myLat = position.coords.latitude, // 48.87525
            myLng = position.coords.longitude; // 2.31110

        this.set("pos", new google.maps.LatLng(myLat, myLng));
        //console.log('Votre position - Latitude : ' + myLat + ', longitude : ' + myLng);
    },


    errorGeoloc: function (error) {
    		switch (error.code) {
	            case error.PERMISSION_DENIED:
	            	alert('Vous n’avez pas autorisé l’accès à votre position');
	                break;
	            case error.POSITION_UNAVAILABLE:
	            	alert('Votre position n’a pu être déterminée. Utilisez la recherche pour trouver une station');
	                break;
	            case error.TIMEOUT:
	            	alert('Timeout geolocalisation. Utilisez la recherche pour trouver une station.');	            	
	                break;
    		}
    		navigator.geolocation.clearWatch(this.get("lastWatchId"));
        
    },

    stopWatch: function () {
        navigator.geolocation.clearWatch(this.watchId);
    },

    forceGeolocalize: function () {
        this.set("centerOnMe", true);
 //       if (this.get("centerOnMe") == true) {
            this.stopWatch();
            this.geolocalize();
 //       }
    }

});
var user = new User();
