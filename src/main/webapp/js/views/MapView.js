var MapView = Backbone.View.extend({

    className: 'withTopBar',

    initialize: function () {
        this.appState = this.options.appState;
        this.gmap = new GMapView({appState: this.appState});
        this.listenTo(this.appState, 'change:currentStation', this.showDetail);
        this.listenTo(this.appState, 'change:mapCenter', this.fetchStations);
        this.listenTo(stations, 'reset', this.displayStations);
        this.listenTo(this.appState, 'change:transportTypes', this.displayStations);
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
        var station = this.appState.get('currentStation');
        if(!station){
            this.hideDetail();
            return;
        }
        if (this.detailView) this.detailView.close();
        station.updateDistance(user.get("pos"));
        this.detailView = new ListLineView({tagName: 'div', className: 'line overflow', model: station, lineTemplate: "#list-line-tmpl", extendedLineTemplate: "#list-line-extended-tmpl"});
        this.$el.append(this.detailView.render().el);
        this.detailView.toggle();
    },

    hideDetail: function () {
        if (this.detailView) this.detailView.close();
    },

    showDetailAndCenterMap: function (station) {
        this.showDetail(station);
        this.gmap.centerMap(station.get('coordinates').latitude, station.get('coordinates').longitude);
    },

    fetchStations: function () {
        var mapCenter = this.appState.get('mapCenter').pos;
        var station = this.appState.get('mapCenter').station;
        var self = this;
        stations.setCoordinates(mapCenter.lat(), mapCenter.lng())
            .fetch({
                reset: true,
                success: function () {
                    if (station) {
                        self.appState.set('currentStation', station);
                    }
                }});
    },

    displayStations: function () {
        this.gmap.showStationsMarkers(stations.filterByTypes(this.appState.get('transportTypes')));
    }

});