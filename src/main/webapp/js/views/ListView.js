var ListView = Backbone.View.extend({

    tagName: 'ul',
    className: 'withTopBar',

    initialize: function () {
        appState = this.options.appState;
        this.listenTo(stations, 'reset', this.showLines);
        this.listenTo(appState, 'change:transportTypes', this.showLines);

        this.listenTo(stations, 'show', this.updateCurrentStationAndCenter, this);

        this.showLines();
    },


    reBind: function () {
        _.each(this.lines, function (lineView) {
            lineView.delegateEvents();
        });
        return this;
    },

    updateCurrentStationAndCenter: function (station) {
        //       appState.set('currentStation', station);
        var newCenter = station.get('coordinates');
        appState.set('mapCenter', {
                pos: new google.maps.LatLng(newCenter.latitude, newCenter.longitude),
                station: station}
        );
    },

    showLines: function () {
        this.$el.empty();
        this.lines = [];

        if (user.get('centerOnMe')) {
            this.$el.append('<div class="no-content">Basculez vers le mode "Carte" pour vous géolocaliser</div>');
            return;
        }
        if (stations.isEmpty()) {
            this.$el.append('<div class="no-content">Aucune station à moins de 1500m</div>');
            return;
        }
        var stationsFilteredByType = stations.filterByTypes(appState.get('transportTypes'));
        stationsFilteredByType.sort();
        stationsFilteredByType.each(_.bind(function (station) {
            var lineView = new ListLineView({model: station}).render();
            this.$el.append(lineView.el);
            this.lines.push(lineView);
        }, this));
    }

});