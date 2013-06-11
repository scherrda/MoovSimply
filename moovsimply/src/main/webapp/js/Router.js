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
        if (!this.mapView) this.mapView = new MapView().render();
        $('#content').html(this.mapView.el);
        this.topBar.switchToMap();
    },

    goList: function () {
        if (!this.listView) this.listView = new ListView().render();
        $('#content').html(this.listView.el);
        this.topBar.switchToList();
    }

});