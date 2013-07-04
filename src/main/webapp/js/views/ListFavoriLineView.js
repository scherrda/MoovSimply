var ListFavoriLineView = Backbone.View.extend({

    tagName: 'li',
    className: 'line favori-line',

    events: {
        'click .extend-arrow': 'toggle',
        'click .localize': 'showOnMap',
        'click .remove': 'removeFavori'
    },

    initialize: function () {
        this.appState = this.options.appState;
        this.template = Handlebars.compile($('#list-favoris-tmpl').html());
        this.templateExtended = Handlebars.compile($('#list-line-extended-tmpl').html());
    },

    render: function () {
        this.$el.html(this.template(this.model.attributes));
        return this;
    },

    toggle: function () {
        this.$('.line-extended').slideToggle(300).toggleClass('extended');
        this.$('.extend-arrow').toggleClass('extended');
    },

    showOnMap: function () {
        var searchStation = allStations.findWhere({stationId: this.model.get('stationId')});
        if (searchStation) {
            this.updateCurrentStationAndCenter(searchStation);
        }
    },

    updateCurrentStationAndCenter: function (station) {
        var newCenter = station.get('coordinates');
        Backbone.history.navigate('#map', {trigger: true});
        this.appState.set('mapCenter', {
            pos: new google.maps.LatLng(newCenter.latitude, newCenter.longitude),
            station: station
        });
    },

    removeFavori: function () {
        favoris.remove(this.model).save();
    },

    close: function () {
        this.remove();
        this.unbind();
    }

});