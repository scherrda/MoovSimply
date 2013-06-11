var GMapView = Backbone.View.extend({

    el: '#map-canvas',
    currentMarkers: [],

    render: function () {
        this.geolocalize();
        var mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(48.87525, 2.31110),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        this.map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

        return this;
    },

    geolocalize: function () {
        if (navigator.geolocation) {
            var watchId = navigator.geolocation.watchPosition(successCallback, errorCallback, {enableHighAccuracy: true, timeout: 10000, maximumAge: 600000});
        }
        else {
            console.log("pas de géolocalisation HTML5 possible avec ce navigateur");
        }

        function successCallback(position) {
            console.log("Votre position - Latitude : " + position.coords.latitude + ", longitude : " + position.coords.longitude);
        }

        function errorCallback(error) {
            switch (error.code) {
                case error.PERMISSION_DENIED:
                    console.log("L'utilisateur n'a pas autorisé l'accès à sa position");
                    break;
                case error.POSITION_UNAVAILABLE:
                    console.log("Votre position n'a pas pu être déterminée");
                    break;
                case error.TIMEOUT:
                    console.log("timeout geo html5");
                    break;
            }
        }
    },

    stopWatch: function () {
        navigator.geolocation.clearWatch(watchId);
    }
});