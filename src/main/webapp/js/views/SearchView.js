var SearchView = Backbone.View.extend({

    events : {
        'submit form': 'onSearch'
    },

    initialize: function() {
        this.appState = this.options.appState;

        this.model = new Backbone.Model();

        this.allStations = new StationsCollection();
        this.listenTo(this.allStations, 'sync', this.initSearch);
        this.allStations.fetch();//all stations used for search

        this.render();
        if(!this.options.display){
            this.display = false;
            this.hide();
        }
   },

    render: function () {
        this.$el.html($('#search-tmpl').html());
        return this;
    },

    initSearch : function () {

        var autocompletesSearch = this.allStations.map( function(model){
            return {
                label : model.get("name"),
//                value: model.get("name")},
                icon : './img/types/' + model.get("type").toLowerCase()
                }
        });
        this.$("input[name=search]").autocomplete({source: autocompletesSearch, minLength: 3});
    },


    toggle : function(){
        this.$el.toggle();
    },

    hide : function(){
        this.display = false;
        this.$el.hide();
        return this;
    },

    show : function(){
        this.display = true;
        this.$el.show();
        return this;
    },

    onSearch: function (event) {
        event.preventDefault();
        var searchText = this.$('input').val();
        console.log(searchText);
//        var searchStation =_.findWhere(this.allStations, {id : searchId});
        this.model.set('search', searchText);
        var matchingStations = this.allStations.filterByName(searchText);
        if(matchingStations && matchingStations[0]){
            console.log("first matching ", matchingStations[0]);
            this.updateCurrentStationAndCenter(matchingStations[0]);
        }

        return false;
    }

    updateCurrentStationAndCenter : function (station) {
        var newCenter = station.get('coordinates');
        this.appState.set('mapCenter', {
                   pos : new google.maps.LatLng(newCenter.latitude, newCenter.longitude),
                   station : station
                   });
    }

});