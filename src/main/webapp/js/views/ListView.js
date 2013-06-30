var ListView = Backbone.View.extend({

    tagName: 'ul',
    className: 'withTopBar',

    initialize: function () {
        this.appState = this.options.appState;
        this.listenTo(stations, 'reset', this.showLines);
        this.listenTo(stations, 'show', this.updateCurrentStationAndCenter, this);

        this.showLines();
    },


    reBind: function () {
        _.each(this.lines, function (lineView) {
            lineView.delegateEvents();
        });
        return this;
    },

    updateCurrentStationAndCenter : function (station) {
 //       this.appState.set('currentStation', station);
        var newCenter = station.get('coordinates');
        this.appState.set('mapCenter', {
            pos :new google.maps.LatLng(newCenter.latitude, newCenter.longitude),
            station : station}
            );
    },

    showLines: function () {
        this.$el.empty();
        this.lines = [];

        if (this.appState.get('nextGeolocCenterOnUser')) {
            this.$el.append('<div class="no-station">Basculez vers le mode "Carte" pour vous géolocaliser.</div>');
            return;
        }
        if (stations.isEmpty()) {
            this.$el.append('<div class="no-station">Aucune station à moins de 500m.</div>');
            return;
        }

        stations.sort();
        stations.each(_.bind(function (station) {
            var lineView = new ListLineView({model: station}).render();
            this.$el.append(lineView.el);
            this.lines.push(lineView);
        }, this));
    }

});