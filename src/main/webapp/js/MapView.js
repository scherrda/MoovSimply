var MapView = Backbone.View.extend({

    className: 'withTopBar',

    initialize: function () {
        this.gmap = new GMapView();
        this.listenTo(this.gmap, 'details:show', this.showDetail);
        this.listenTo(this.gmap, 'details:hide', this.hideDetail);
        this.listenTo(stations, 'sync', this.displayStations);
    },

    reBind: function () {
        if (this.detailView) this.detailView.delegateEvents();
        return this;
    },

    render: function () {
        this.$el.append(this.gmap.render().geolocalize().el);
        return this;
    },

    showDetail: function (station) {
        this.hideDetail();
        this.detailView = new ListLineView({tagName: 'div', className: 'line overflow', model: station});
        this.$el.append(this.detailView.render().el);
    },

    hideDetail: function () {
        if (this.detailView) this.detailView.close();
    },

    displayStations: function () {
        this.gmap.showStationsMarkers();
    }

});