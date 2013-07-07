var ListLineView = Backbone.View.extend({

    tagName: 'li',
    className: 'line',

    events: {
        'click .extend-arrow': 'toggle',
        'click .localize': 'showOnMap',
        'click .favori-toggle': 'toggleFavori'
    },

    initialize: function () {
        this.template = Handlebars.compile($('#list-line-tmpl').html());
        this.templateExtended = Handlebars.compile($('#list-line-extended-tmpl').html());

        this.listenTo(this.model, 'sync', this.renderDetail);
        this.listenTo(favoris, 'add remove', this.updateFavoriState);
    },

    render: function () {
        this.$el.html(this.template(this.model.attributes));
        if (favoris.findWhere({stationId: this.model.get('stationId')})) {
            this.$('.favori-toggle').addClass('active');
        }
        return this;
    },

    renderDetail: function () {
        this.model.detailsAlreadyFetched = true;
        this.$('.line-extended').html(this.templateExtended(this.model.attributes));
    },

    toggle: function () {
        if (!this.model.detailsAlreadyFetched) {
            this.model.fetch();
        } else {
            this.renderDetail();
        }

        this.$('.line-extended').slideToggle(300);
        this.$('.extend-arrow').toggleClass('extended');
    },

    showOnMap: function () {
        Backbone.history.navigate('#map', {trigger: true});
        this.model.trigger('show', this.model);
    },

    toggleFavori: function () {
        var $toggle = this.$('.favori-toggle');
        if ($toggle.hasClass('active')) {
            this.removeFavori();
        } else {
            this.addFavori();
        }
    },

    updateFavoriState: function (favori) {
        if (favori.get('stationId') === this.model.get('stationId')) this.$('.favori-toggle').toggleClass('active');
    },

    addFavori: function () {
        var favori = new Favori({
            name: this.model.get('name'),
            customName: this.model.get('name'),
            stationId: this.model.get('stationId'),
            coordinates: this.model.get('coordinates')
        });
        favoris.add(favori);
    },

    removeFavori: function () {
        favoris.findWhere({stationId: this.model.get('stationId')}).destroy();
    },

    close: function () {
        this.remove();
        this.unbind();
    }

});