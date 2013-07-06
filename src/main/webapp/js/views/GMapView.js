var GMapView = Backbone.View.extend({

    id: 'map-canvas',
    currentMarkers: [],

    initialize: function () {
        this.appState = this.options.appState;
        this.listenTo(this.appState, 'change:mapCenter', this.onChangeMapCenter);
        this.listenTo(this.appState, 'change:currentStation', this.onChangeCurrentStation);
        this.meMarker = this.createMarker(this.appState.getCenterCoordinates(), 'You', this.getMeMarkerImage());
        this.listenTo(user, "change:pos", this.refreshPosition);
        user.geolocalize();
    },

    render: function () {
        console.log("rendering Gmap");
        //Why the map creation is in render ?
        var mapOptions = {
            zoom: 16,
            mapTypeId: google.maps.MapTypeId.ROADMAP,
            center: this.appState.get('mapCenter').pos,
            styles: MAP_STYLES
        };
        this.map = new google.maps.Map(this.el, mapOptions);

        return this;
    },

    refreshPosition: function (position) {
        this.meMarker.setPosition(user.get("pos"));
        this.meMarker.setMap(this.map);
        if (user.get('centerOnMe')) {
            this.appState.set('mapCenter', {pos: user.get("pos")});
            user.set('centerOnMe', false);
        }

    },


    onChangeMapCenter: function () {
        this.map.setCenter(this.appState.get('mapCenter').pos);
    },

    createMarker: function (coordinates, title, icon) {
        return new google.maps.Marker({
            position: new google.maps.LatLng(coordinates.latitude, coordinates.longitude),
            map: this.map,
            title: title,
            icon: icon
        });
    },

    showStationsMarkers: function (stations) {
//        if (this.markerCluster) this.markerCluster.removeMarkers(this.currentMarkers);
        _.each(this.currentMarkers, function (marker) {
            marker.setMap(null);
        });
        this.currentMarkers = [];

        stations.each(_.bind(function (station) {
            var info = station.get('type') + (station.get('lineNumber') ? ' ' + station.get('lineNumber') : '');
            var stationMarker = this.createMarker(station.get('coordinates'), info, this.getStationMarkerImage(station.get('type')));
            stationMarker.type = station.get('type');
            stationMarker.id = station.get('stationId');
            stationMarker.active = false;
            this.currentMarkers.push(stationMarker);

            google.maps.event.addListener(stationMarker, 'click', _.bind(function () {
                this.appState.set('currentStation', station);
            }, this));
        }, this));

//        this.markerCluster = new MarkerClusterer(this.map, this.currentMarkers, MARKER_CLUSTER_OPTIONS);

        google.maps.event.addListener(this.map, 'click', _.bind(function () {
            this.appState.set('currentStation', null);
        }, this));
    },

    onChangeCurrentStation: function () {
        this.turnOffActiveMarker();
        var newStation = this.appState.get('currentStation');
        if(newStation){
            var newStationMarker = _.findWhere(this.currentMarkers, {id: newStation.get('stationId'), type: newStation.get('type')});
            this.turnOnMarker(newStationMarker);
        }
    },

    turnOnMarker: function (stationMarker) {
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

    getMeMarkerImage: function () {
        return new google.maps.MarkerImage(
            './img/dude-pink.png',
            new google.maps.Size(29, 25), // taille
            new google.maps.Point(0, 0), // The origin for this image
            new google.maps.Point(12, 23) // The anchor for this image
        );
    },

    getStationMarkerImage: function (type, active) {
        active = active || false;

        var xSprite = active ? 31 : 0,
            ySprite = MARKERS_SPRITE_MAP[type];

        return new google.maps.MarkerImage(
            './img/markers/markers-sprite.png',
            new google.maps.Size(31, 40), // taille
            new google.maps.Point(xSprite, ySprite), // The origin for this image
            new google.maps.Point(15, 40) // The anchor for this image
        );
    }

});

var MAP_STYLES = [
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
];

/*
var MARKER_CLUSTER_OPTIONS = {
    styles: [
        {
            width: 60,
            height: 60,
            url: "img/markers/marker-cluster.png",
            anchorIcon: [30, 30],
            fontWeight: "normal",
            textColor: '#ffffff',
            textSize: 11
        }
    ]
};
*/

var MARKERS_SPRITE_MAP = {
    TRAIN: 0,
    METRO: 40,
    RER: 80,
    TRAM: 120,
    BUS: 160,
    AUTOLIB: 200,
    VELIB: 240
};
