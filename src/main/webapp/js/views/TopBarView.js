var TopBarView = Backbone.View.extend({

    className: 'header',

    events: {
        'click .switch': 'loading',
        'click input[name=search]':'initSearch',
        'submit .search': 'onSearch'
    },

    initialize: function() {
        this.appState = this.options.appState;
   },

    render: function () {
        this.$el.html($('#topbar-tmpl').html());
        return this;
    },

    initSearch : function () {
        var names = stations.map( function(model){
            return model.get("name")
        });
        $("input[name=search]").autocomplete({source: names});
    },


    onSearch: function (event) {
        var searchText = this.$('input').val();
        console.log(searchText);
        this.appState.set('search', searchText);
        var matchingStations = stations.filterByName(searchText);
        if(matchingStations && matchingStations[0]){
            this.appState.set('currentStation', matchingStations[0]);
        }
    },

    loading: function () {
        this.changeClass('switch-load');
    },

    switchToMap: function () {
        this.changeClass('switch-list', '#list');
    },

    switchToList: function () {
        this.changeClass('switch-map', '#map');
    },

    changeClass: function (newClass, newHref) {
        this.$('.switch')
            .attr('class', 'switch')
            .addClass(newClass);
        if (newHref) {
            this.$('.switch').attr('href', newHref);
        }
        this.$el.show();
    },

    hide: function () {
        this.$el.hide();
        return this;
    }

});