var Router = Backbone.Router.extend({

    routes: {
        'splash': 'goSplash',
        'map': 'goMap',
        'list': 'goList',
        '*path': 'goSplash'
    },

    initialize: function () {
        this.topBar = new TopBarView().render().hide();
        $('#topbar').html(this.topBar.el);
    },

    goSplash: function () {
        $('#content').html(new SplashView().render().el);
        this.topBar.hide();
    },

    goMap: function () {
        var mapView = new MapView().render();
        $('#content').html(mapView.el);
        this.topBar.switchToMap();
    },

    goList: function () {
        $('#content').html(new ListView().render().el);
        this.topBar.switchToList();
    }

});