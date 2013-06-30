var TopBarView = Backbone.View.extend({

    className: 'header',

    events: {
        'click .switch': 'loading',
        'click .search-link': 'showSearchView',
        'click .transport-filter': 'showTransportFilterView'
    },

    initialize : function () {
        this.searchView = new SearchView({display:false});
        this.filterView = new FilterView({display:false});
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

    showSearchView : function() {
        this.searchView.toggle();
    },

    hide: function () {
        this.$el.hide();
        return this;
    },

    showTransportFilterView : function() {
        this.filterView.toggle();
    }

});