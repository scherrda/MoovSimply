var TopBarView = Backbone.View.extend({

    className: 'header',

    events: {
        'click .switch': 'loading',
        'click .search-link': 'showSearchView'
    },

    initialize : function () {
        this.searchView = new SearchView({display:false});
    },

    render: function () {
        this.$el.html($('#topbar-tmpl').html());
        this.$('#search').html(this.searchView.el);
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
    }

});