var TopBarView = Backbone.View.extend({

    className: 'header',

    events: {
        'click .switch': 'loading',
        'click .search-link': 'toggleSearchView',
        'click .transport-filter': 'toggleTransportFilterView',
        'click .geoloc-link': 'forceGeolocalize'        
    },

    initialize: function () {
        this.searchView = new SearchView({appState: this.options.appState});
        this.filterView = new FilterView({model: this.options.appState});
    },

    render: function () {
        this.$el.html($('#topbar-tmpl').html());
        this.$('#search').html(this.searchView.el);
        this.$('#transportFilter').html(this.filterView.el);
        return this;
    },

    loading: function () {
        this.changeClass('switch-load');
    },

    switchToMap: function () {
        this.changeClass('switch-list', '#list');
        this.$('.switch').attr('title', 'Basculter vers le mode liste');
    },

    switchToList: function () {
        this.changeClass('switch-map', '#map');
        this.$('.switch').attr('title', 'Basculter vers le mode carte');
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

    toggleSearchView: function (event) {
        var $wrapper = this.$('#search');

        event.preventDefault();
        $wrapper.toggle();
    },

    hide: function () {
        this.$el.hide();
        return this;
    },

    toggleTransportFilterView: function (event) {
        var $wrapper = this.$('#transportFilter');

        event.preventDefault();
        $wrapper.toggle();
        if ($wrapper.is(":visible")) {
            this.$('.transport-filter').addClass('active');
        } else {
            this.$('.transport-filter').removeClass('active');
        }        
    },
    
    forceGeolocalize : function (event) {
        event.preventDefault();
        this.options.appState.set('nextGeolocCenterOnUser', true);
        this.switchToMap();
    }    

});