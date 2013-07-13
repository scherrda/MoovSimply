var ListView = Backbone.View.extend({

    tagName: 'ul',
    className: 'withTopBar',

    initialize: function () {
        appState = this.options.appState;
        this.listenTo(stations, 'reset', this.render);
        this.listenTo(appState, 'change:transportTypes', this.showLines);

        this.listenTo(stations, 'show', this.updateCurrentStationAndCenter, this);

        this.lines = [];
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

    filterCollection : function(){
        return stations.filterByTypes(appState.get('transportTypes'));
    },

    clear : function(){
        this.$el.empty();
        if(this.lines.length){
            _.each(this.lines, function(lineView) {
                lineView.close();
            });
            this.lines = [];
        }
    },

    render : function(){
        console.log("rendering ListView");
        if (stations.isEmpty()) {
            this.$el.append('<div class="no-content">Aucune station</div>');
        }
        this.showLines();

        return this;
    },

    showLines: function () {
        this.clear();
        var filteredByTypes = this.filterCollection();
        this.filterCollection().each( function (station) {
                this.addSubView(station);
            }, this);
    },

    addSubView : function(station){
            var lineView = new ListLineView({model: station}).render();
            this.$el.append(lineView.el);
            this.lines.push(lineView);
    }

});