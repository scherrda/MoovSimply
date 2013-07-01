var Router = Backbone.Router.extend({

    routes: {
        'splash': 'goSplash',
        'map': 'goMap',
        'list': 'goList',
        //'transportFilter': '',
        //'search' : '',
//        'geoloc': 'centerOnUser',
        '*path': 'goSplash'
    },

    initialize: function () {
        this.appState = new AppState();
        this.topBar = new TopBarView({appState:this.appState}).render().hide();
        $('#topbar').html(this.topBar.el);
    },

    goSplash: function () {
        $('#content').html(new SplashView().render().el);
        this.topBar.hide();
    },

    goMap: function () {
        if (!this.mapView) this.mapView = new MapView({appState:this.appState}).render();
        $('#content').html(this.mapView.reBind().el);
        this.topBar.switchToMap();
    },
/*
    centerOnUser: function () {
        if (!this.mapView) {
            return this.goMap();
        }
        this.mapView.forceCenterOnUser();
        this.topBar.switchToMap();
    },

*/
    goList: function () {
        if (!this.listView) this.listView = new ListView({appState:this.appState}).render();
        $('#content').html(this.listView.reBind().el);
        this.topBar.switchToList();
    }

});