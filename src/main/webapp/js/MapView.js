var MapView = Backbone.View.extend({

    className: 'withTopBar',

    initialize: function () {
        this.gmap = new GMapView();
        this.listenTo(this.gmap, 'details:show', this.showDetail);
        this.listenTo(this.gmap, 'details:hide', this.hideDetail);
        this.listenTo(this.gmap, 'localized', this.fetchStations);
        this.listenTo(stations, 'sync', this.displayStations);
       // this.listenTo(stations, 'show', this.showDetailAndCenterMap, this); //unused
    },

    reBind: function () {
        if (this.detailView) this.detailView.delegateEvents();
        return this;
    },

    render: function () {
        this.$el.append(this.gmap.render().el);
        return this;
    },

    showDetail: function (station) {
        this.hideDetail();

        this.detailView = new ListLineView({tagName: 'div', className: 'line overflow', model: station});
        this.$el.append(this.detailView.render().el);
        this.detailView.extend();
        this.gmap.turnOnMarker(station.get('marker'));
    },

    hideDetail: function () {
        if (this.detailView) this.detailView.close();
        this.gmap.turnOffActiveMarker();
    },

    showDetailAndCenterMap: function (station) {
        this.showDetail(station);
        this.gmap.centerMap(station.get('coordinates').latitude, station.get('coordinates').longitude);
    },

    fetchStations: function (myLat, myLng) {
        stations.reset().setCoordinates(myLat, myLng).fetch();
    },

    displayStations: function () {
        this.gmap.showStationsMarkers(stations);
    }

});