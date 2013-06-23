var MapView = Backbone.View.extend({

    className: 'withTopBar',

    initialize: function () {
        this.appState = new AppState();

        this.gmap = new GMapView({appState:this.appState});
        this.listenTo(this.appState, "change:currentStation", this.showDetail)
        this.listenTo(this.gmap, 'details:hide', this.hideDetail);
        this.listenTo(this.appState, "change:mapCenter", this.fetchStations);
        //this.listenTo(this.gmap, 'localized', this.fetchStations);
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

    showDetail: function () {
        var station = this.appState.get("currentStation");

        if (this.detailView) this.detailView.close();
        this.detailView = new ListLineView({tagName: 'div', className: 'line overflow', model: station});
        this.$el.append(this.detailView.render().el);
        this.detailView.extend();
    },

    hideDetail: function () {
        if (this.detailView) this.detailView.close();
    },

    showDetailAndCenterMap: function (station) {
        this.showDetail(station);
        this.gmap.centerMap(station.get('coordinates').latitude, station.get('coordinates').longitude);
    },

    fetchStations: function () {
        var mapCenter = this.appState.get("mapCenter");
        stations.reset().setCoordinates(mapCenter.lat(), mapCenter.lng()).fetch();
    },

    displayStations: function () {
        this.gmap.showStationsMarkers(stations);
    }

});