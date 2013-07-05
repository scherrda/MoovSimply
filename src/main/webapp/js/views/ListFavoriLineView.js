var ListFavoriLineView = Backbone.View.extend({

    tagName: 'li',
    className: 'line favori-line',

    events: {
        'click .extend-arrow': 'toggle',
        'click .localize': 'showOnMap',
        'click .remove': 'removeFavori',
        'click .rename': 'renameFavori'
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
        this.$('.line-extended').slideToggle(300);
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
        this.model.destroy();
    },

    renameFavori: function () {
        var $input = $('<input type="text" class="name" value="' + this.model.get('customName') + '">');
        this.$('.name').replaceWith($input);
        this.$('.rename').addClass('active');

        $input.bind('keypress', _.bind(function (e) {
            if (e.charCode === 13) this.saveFavori();
        }, this));
    },

    saveFavori: function () {
        var newName = this.$('input.name').val();
        if (newName === '') newName = this.model.get('name');
        this.model.set({customName: newName}, {silent: true}).trigger('change');
    },

    close: function () {
        this.remove();
        this.unbind();
    }

});