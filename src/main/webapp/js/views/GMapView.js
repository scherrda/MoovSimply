var alreadyLocalized = false;

var GMapView = Backbone.View.extend({

    id: 'map-canvas',
    currentMarkers: [],

    initialize: function () {
        this.appState  = this.options.appState;
        this.meMarkerImage = new google.maps.MarkerImage(
            './img/dude-pink.png',
            new google.maps.Size(29, 25), // taille
            new google.maps.Point(0, 0), // The origin for this image
            new google.maps.Point(12, 23) // The anchor for this image
        );

        this.center = new google.maps.LatLng(48.87525, 2.31110); //initial Center


        this.meMarker = new google.maps.Marker({
            title: 'You',
            icon: this.meMarkerImage
        });
        this.geolocalize();
    },

    render: function () {
        var mapOptions = {
            zoom: 16,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            center : this.center,
            styles: [
                {
                    "featureType": "road.arterial",
                    "elementType": "geometry.fill",
                    "stylers": [
                        { "color": "#eeeeee" }
                    ]
                },
                {
                    "featureType": "road.arterial",
                    "elementType": "labels.text.stroke",
                    "stylers": [
                        { "color": "#eeeeee" }
                    ]
                },
                {
                    "featureType": "road.highway",
                    "elementType": "geometry.fill",
                    "stylers": [
                        { "color": "#dddddd" }
                    ]
                },
                {
                    "featureType": "road.highway",
                    "elementType": "labels.text.stroke",
                    "stylers": [
                        { "color": "#dddddd" }
                    ]
                },
                {
                    "stylers": [
                        { "hue": "#00ccff" },
                        { "saturation": -30 },
                        { "lightness": 20 }
                    ]
                }
            ]
        };
        this.map = new google.maps.Map(this.el, mapOptions);

        return this;
    },

    geolocalize: function () {
        if (navigator.geolocation && navigator.geolocation.watchPosition) {
            this.watchId = navigator.geolocation.watchPosition(_.bind(this.refreshMyMarkerAndCenter, this), _.bind(this.errorGeoloc, this), {enableHighAccuracy: true, timeout: 10000, maximumAge: 600000});
        } else {
            alert('La géolocalisation n’est pas possible avec ce navigateur');
        }
        return this;
    },

    refreshMyMarkerAndCenter: function (position) {
        var myLat = position.coords.latitude, // 48.87525
            myLng = position.coords.longitude; // 2.31110

        console.log("Votre position - Latitude : " + myLat + ", longitude : " + myLng);
        this.meMarker.setPosition(new google.maps.LatLng(myLat, myLng));
        this.meMarker.setMap(this.map);
        if(!alreadyLocalized){
            this.centerMap(myLat, myLng);
        }
        alreadyLocalized = true;

        this.trigger('localized', myLat, myLng);
    },

    errorGeoloc: function (error) {
        switch (error.code) {
            case error.PERMISSION_DENIED:
                alert('Vous n’avez pas autorisé l’accès à votre position');
                break;
            case error.POSITION_UNAVAILABLE:
                alert('Votre position n’a pas pu être déterminée');
                break;
            case error.TIMEOUT:
                console.log('Timeout geo html5');
                break;
        }
    },

    stopWatch: function () {
        navigator.geolocation.clearWatch(this.watchId);
    },

    centerMap: function (lat, lng) {
        this.center = new google.maps.LatLng(lat, lng);
        this.map.setCenter(this.center);
    },

   createMarker : function (coordinates, info, icon){
        var marker = new google.maps.Marker({
            position: new google.maps.LatLng(coordinates.latitude, coordinates.longitude),
            map   : this.map,
            title : info,
            icon :  icon
        });
        return marker; //TODO why ?
    },

    showStationsMarkers: function (stations) {
        _.each(this.currentMarkers, function (marker) {
            marker.setMap(null);
        });
        this.currentMarkers = [];

        stations.each(_.bind(function (station) {
             var info = station.get('type') + ' ' + station.get('lineNumber');
            var stationMarker = this.createMarker(station.get('coordinates'), info, this.getStationMarkerImage(station.get('type')));
            stationMarker.type = station.get('type');
            stationMarker.active = false;
            this.currentMarkers.push(stationMarker);


            google.maps.event.addListener(stationMarker, 'click', _.bind(function () {
                  this.appState.set("currentStation", station);
                  this.turnOnMarker(stationMarker);
            }, this));

        }, this));


        google.maps.event.addListener(this.map, 'click', _.bind(function () {
            this.trigger('details:hide');
        }, this));


    },

    turnOnMarker: function (stationMarker) {
        this.turnOffActiveMarker();
        stationMarker.setIcon(this.getStationMarkerImage(stationMarker.type, true));
        stationMarker.active = true;
    },

    turnOffMarker: function (stationMarker) {
        stationMarker.setIcon(this.getStationMarkerImage(stationMarker.type));
        stationMarker.active = false;
    },

    turnOffActiveMarker: function () {
        var turnedOnMarker = _.findWhere(this.currentMarkers, {active: true});
        if (turnedOnMarker) this.turnOffMarker(turnedOnMarker);
    },

    getStationMarkerImage: function (type, active) {
        active = active || false;
        return new google.maps.MarkerImage(
            './img/markers/' + type.toLowerCase() + (active ? '-active' : '') + '.png',
            new google.maps.Size(31, 40), // taille
            new google.maps.Point(0, 0), // The origin for this image
            new google.maps.Point(15, 40) // The anchor for this image
        );
    }

});