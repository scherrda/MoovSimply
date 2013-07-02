var SearchView = Backbone.View.extend({

    className: 'search',

    events: {
        'submit form': 'onSearch'
    },

    initialize: function () {
        this.appState = this.options.appState;

        this.model = new Backbone.Model();
        this.allStations = new StationsCollection();

        this.listenTo(this.allStations, 'sync', this.initSearch);
        this.listenTo(this.appState, 'change:transportTypes', this.initSearch);
        this.allStations.fetch();//all stations used for search

        this.render();
    },

    filterCollection: function () {
        var types = this.appState.get("transportTypes");
        return this.allStations.filterByTypes(types);
    },

    render: function () {
        this.$el.html($('#search-tmpl').html());
        return this;
    },

    initSearch: function () {

        var autocompleteSource = this.filterCollection().map(function (model) {
            return {
                label: model.get("name"),
                value: model.get("stationId"),
                icon: './img/types/' + model.get("type").toLowerCase()
            }
        });
        var searchInput = this.$("input[name=search]");
        var self = this;
        searchInput.autocomplete({
            source: autocompleteSource,
            minLength: 3,
            select: function (event, ui) {
                event.preventDefault();
                searchInput.val(ui.item.label);
                self.model.set('search', {id: ui.item.value, text: ui.item.label});
            },
            focus: function (event, ui) {
                event.preventDefault();
                searchInput.val(ui.item.label);
            }
        });
        //overriding render for example with icon
        /*           .data( "ui-autocomplete" )._renderItem = function( ul, item ) {
         return $( "<li>" )
         .append( item.label + "<img src=" + item.icon + ".png/>" )
         .appendTo( ul );
         };
         */
    },

    onSearch: function (event) {
        event.preventDefault();
        var input = this.model.get("search");
        var search = input ? input.id : this.$('input').text();
        var filteredStations = this.filterCollection();
        var searchStation = this.allStations.findWhere({stationId: search});
        if (searchStation) {
            this.updateCurrentStationAndCenter(searchStation);
        }
        /*

         var matchingStations = this.filterCollection().filterByName(searchText);
         if(matchingStations && matchingStations.length > 0){
         console.log("first matching ", matchingStations[0]);
         this.updateCurrentStationAndCenter(matchingStations[0]);
         }*/

        return false;
    },

    updateCurrentStationAndCenter: function (station) {
        var newCenter = station.get('coordinates');
        this.appState.set('mapCenter', {
            pos: new google.maps.LatLng(newCenter.latitude, newCenter.longitude),
            station: station
        });
    },

    toggle: function () {
        this.$el.parent().slideToggle(300);
    },

    hide: function () {
        this.$el.parent().hide();
    }

});