var GMapView = Backbone.View.extend({

    el: '#map-canvas',
    currentMarkers: [],

    initialize: function () {
        this.listenTo(stations, 'sync', this.showMarkers);

        this.meMarkerImage = new google.maps.MarkerImage(
            './img/dude-pink.png',
            new google.maps.Size(29, 25), // taille
            new google.maps.Point(0, 0), // The origin for this image
            new google.maps.Point(12, 23) // The anchor for this image
        );
    },

    render: function () {
        var mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(48.87525, 2.31110),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        this.map = new google.maps.Map(this.el, mapOptions);
        this.geolocalize();

        return this;
    },

    geolocalize: function () {
        if (navigator.geolocation) {
            this.watchId = navigator.geolocation.watchPosition(this.addMyMarkerAndCenter.bind(this), this.errorGeoloc.bind(this), {enableHighAccuracy: true, timeout: 10000, maximumAge: 600000});
        }
        else {
            console.log("pas de géolocalisation HTML5 possible avec ce navigateur");
        }
    },

    addMyMarkerAndCenter: function (position) {
        stations.reset().setCoordinates(position.coords.latitude, position.coords.longitude).fetch();
        console.log("Votre position - Latitude : " + position.coords.latitude + ", longitude : " + position.coords.longitude);
        var myMarker = new google.maps.Marker({
            position: new google.maps.LatLng(position.coords.latitude, position.coords.longitude),
            title: 'You',
            map: this.map,
            icon: this.meMarkerImage
        });
        this.map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
    },

    errorGeoloc: function (error) {
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
    },

    stopWatch: function () {
        navigator.geolocation.clearWatch(this.watchId);
    },

    showMarkers: function () {
        _.each(this.currentMarkers, function (marker) {
            marker.setMap(null);
        });
        this.currentMarkers = [];
        stations.each(function (station) {
            var stationMarker = new google.maps.Marker({
                position: new google.maps.LatLng(station.get('coordinates').latitude, station.get('coordinates').longitude),
                title: station.get('type') + ' ' + station.get('lineNumber'),
                map: this.map,
                icon: this.getMarkerImage(station.get('type'))
            });
            this.currentMarkers.push(stationMarker);
        }.bind(this));
    },

    getMarkerImage: function (type) {
        var stationMarkerImage = new google.maps.MarkerImage(
            './img/markers/' + type.toLowerCase() + '.png',
            new google.maps.Size(31, 40), // taille
            new google.maps.Point(0, 0), // The origin for this image
            new google.maps.Point(15, 40) // The anchor for this image
        );
        return stationMarkerImage;
    }
});