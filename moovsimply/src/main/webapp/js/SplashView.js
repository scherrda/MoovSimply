var SplashView = Backbone.View.extend({

    render: function () {
        this.$el.html($('#splash-tmpl').html());
        return this;
    }

});