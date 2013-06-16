var TopBarView = Backbone.View.extend({

    className: 'header',

    events: {
        'click .switch': 'loading'
    },

    render: function () {
        this.$el.html($('#topbar-tmpl').html());
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

    hide: function () {
        this.$el.hide();
        return this;
    }

});