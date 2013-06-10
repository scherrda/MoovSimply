var TopBarView = Backbone.View.extend({

    className: 'header',

    render: function () {
        this.$el.html($('#topbar-tmpl').html());
        return this;
    },

    switchToMap: function () {
        this.$('.switch')
            .removeClass('switch-map')
            .addClass('switch-list')
            .attr('href', '#list');
        this.$el.show();
    },

    switchToList: function () {
        this.$('.switch')
            .removeClass('switch-list')
            .addClass('switch-map')
            .attr('href', '#map');
        this.$el.show();
    },

    hide: function () {
        this.$el.hide();
        return this;
    }

});