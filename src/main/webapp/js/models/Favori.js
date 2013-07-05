var Favori = Backbone.Model.extend({
    defaults: {
        name: '',
        customName: ''
    }
});

var FavorisCollection = Backbone.Collection.extend({

    model: Favori,

    initialize: function () {
        if (!localStorage) return;

        var storedFavoris = localStorage.getItem('moovsimply-favoris');
        if (storedFavoris) {
            this.add(JSON.parse(storedFavoris));
        }
    },

    save: function () {
        if (!localStorage) return;

        localStorage.setItem('moovsimply-favoris', JSON.stringify(this.toJSON()));
    }

});

var favoris = new FavorisCollection();