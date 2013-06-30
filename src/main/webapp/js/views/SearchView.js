var SearchView = Backbone.View.extend({

    events : {
        'submit form': 'onSearch'
    },

    initialize: function() {
        this.appState = this.options.appState;

//        this.model = new Backbone.Model();
        this.allStations = new StationsCollection();

        this.listenTo(this.allStations, 'sync', this.initSearch);
        this.listenTo(this.appState, 'change:transportTypes', this.initSearch);
        this.allStations.fetch();//all stations used for search

        this.render();
        if(!this.options.display){
            this.display = false;
            this.hide();
        }
   },

   filterCollection : function(){
        var types = this.appState.get("transportTypes");
       return this.allStations.filterByTypes(types);
   },

    render: function () {
        this.$el.html($('#search-tmpl').html());
        return this;
    },

    initSearch : function () {

        var autocompleteSource = this.filterCollection().map( function(model){
            return {
                label : model.get("name"),
                value: model.get("stationId"),
                icon : './img/types/' + model.get("type").toLowerCase()
                }
        });
        this.$("input[name=search]").autocomplete({source: autocompleteSource, minLength: 3});
 //overriding render for example with icon
 /*           .data( "ui-autocomplete" )._renderItem = function( ul, item ) {
              return $( "<li>" )
                .append( item.label + "<img src=" + item.icon + ".png/>" )
                .appendTo( ul );
            };
*/
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
        var searchOnText = this.$('input').val();
//        this.model.set('search', searchOnText);
        console.log(searchOnText);

        var filteredStations = this.filterCollection();
        var searchStation =this.allStations.findWhere({stationId : searchOnText});
        if(searchStation){
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

    updateCurrentStationAndCenter : function (station) {
        var newCenter = station.get('coordinates');
        this.appState.set('mapCenter', {
                   pos : new google.maps.LatLng(newCenter.latitude, newCenter.longitude),
                   station : station
                   });
    }

});