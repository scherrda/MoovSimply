var Router = Backbone.Router.extend({

    routes: {
        'splash': 'goSplash',
        'map': 'goMap',
        'list': 'goList',
        'favoris': 'goFavoris',
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

    goList: function () {
        if (!this.listView) this.listView = new ListView({appState:this.appState}).render();
        $('#content').html(this.listView.reBind().el);
        this.topBar.switchToList();
    },

    goFavoris: function () {
        if (!this.listFavorisView) this.listFavorisView = new ListFavorisView({collection:favoris});
        $('#content').html(this.listFavorisView.reBind().el);
 //       this.topBar.switchToFavoris();
    }

});