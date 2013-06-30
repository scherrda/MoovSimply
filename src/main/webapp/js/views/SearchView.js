var SearchView = Backbone.View.extend({

    events : {
        'submit form': 'onSearch'
    },

    initialize: function() {
        this.appState = this.options.appState;//TODO search view shoud not access this high level model

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
        var names = this.allStations.map( function(model){
            return model.get("name")
        });
        console.log($("input[name=search]"));
        this.$("input[name=search]").autocomplete({source: names, minLength: 3});
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
        this.model.set('search', searchText);
        var matchingStations = this.allStations.filterByName(searchText);
        if(matchingStations && matchingStations[0]){
//TODO fetch with new lat/lng or
//new rest service with id station
        }
        return false;
    }




});