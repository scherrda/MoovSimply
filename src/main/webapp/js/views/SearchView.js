var SearchView = Backbone.View.extend({

    className: 'search',

    events: {
        'submit form': 'onSearch'
    },

    initialize: function () {
        this.appState = this.options.appState;

        this.model = new Backbone.Model();

        this.listenTo(allStations, 'sync', this.initSearch);
        this.listenTo(this.appState, 'change:transportTypes', this.initSearch);

        this.render();
    },

    filterCollection: function () {
        var types = this.appState.get("transportTypes");
        return allStations.filterByTypes(types);
    },

    render: function () {
        this.$el.html($('#search-tmpl').html());
        return this;
    },

    initSearch: function () {

        var autocompleteSource = this.filterCollection().map(function (model) {
            return {
                label: model.get("name"),
                //value: model.get("id"),
                value: model.get("stationId"),
                icon: 'img/types/' + model.get("type").toLowerCase() + '.png',
                labelAndType: model.get("name") + '(' + model.get("type") + ')'
            }
        });
        // Suppression des doublons {name, type}
        autocompleteSource = _.uniq(autocompleteSource, function (item, key, a) {
            return item.labelAndType;
        });

        var $searchInput = this.$("input[name=search]");
        var self = this;
        $searchInput.autocomplete({
            source: autocompleteSource,
            minLength: 3,
            select: function (event, ui) {
                event.preventDefault();
                $searchInput.val(ui.item.label);
                self.model.set('search', {id: ui.item.value, text: ui.item.label});
            },
            focus: function (event, ui) {
                event.preventDefault();
                $searchInput.val(ui.item.label);
            }
        }).data("ui-autocomplete")._renderItem = function (ul, item) {
            return $('<li></li>')
                .data("item.autocomplete", item)
                .append('<a><img class="station-type" src="' + item.icon + '" width="15" height="15">' + item.label + '</a>')
                .appendTo(ul);
        };
    },

    onSearch: function (event) {
        event.preventDefault();
        var input = this.model.get("search");
        var search = input ? input.id : this.$('input').text();
        var filteredStations = this.filterCollection();
        var searchStation = allStations.findWhere({stationId: search});
        if (searchStation) {
            this.updateCurrentStationAndCenter(searchStation);
        }
        return false;
    },

    updateCurrentStationAndCenter: function (station) {
        var newCenter = station.get('coordinates');
        var oldSearch = this.appState.get('mapCenter').station;
        this.appState.set('mapCenter', {
            //pos: new google.maps.LatLng(station.get('lat'), station.get('lng')),
            pos: new google.maps.LatLng(newCenter.latitude, newCenter.longitude),
            station: station
        });
        //force triggering event "change" if same search as last one
        if(oldSearch == station){
            this.appState.trigger('change:mapCenter');
        }
    },

    toggle: function () {
        this.$el.parent().slideToggle(300);
    },

    hide: function () {
        this.$el.parent().hide();
    }

});