var Favori = Backbone.Model.extend({

    defaults: {
        name :null,
        type : "station"
    },

});

var FavorisCollection = Backbone.Collection.extend({

    model: Favori,

    initialize: function() {
        if(!localStorage) return;

        var storedFavoris = localStorage.getItem('moovsimply-favoris');
        if(storedFavoris) {
            this.add(JSON.parse(storedFavoris));
        }
    },

    addAndSave: function(favori) {
        this.add(favori);
        this.save();
    },

    save: function() {
        localStorage.setItem('moovsimply-favoris', JSON.stringify(this.toJSON()));
    }

});

var favoris = new FavorisCollection();