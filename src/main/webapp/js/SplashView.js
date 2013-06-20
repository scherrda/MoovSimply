var SplashView = Backbone.View.extend({

    className: 'splash withoutTopBar',

    render: function () {
        this.$el.html($('#splash-tmpl').html());
        return this;
    }

});