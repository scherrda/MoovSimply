var ListLineView = Backbone.View.extend({

    tagName: 'li',
    className: 'line',

    events: {
        'click .extend-arrow:not(.loading)': 'toggle',
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
        this.$('.line-extended').html(this.templateExtended(this.model.attributes)).slideToggle(300);
        this.$('.extend-arrow')
            .addClass('extended')
            .removeClass('loading');
    },

    hideDetail: function () {
        this.$('.line-extended').slideToggle(300);
        this.$('.extend-arrow')
            .removeClass('extended')
            .removeClass('loading');
    },

    toggle: function () {
        var $arrow = this.$('.extend-arrow');
        $arrow.addClass('loading');

        if ($arrow.hasClass('extended')) {
            this.hideDetail();
        } else {
            this.model.fetch();
        }

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