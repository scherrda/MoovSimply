var ListFavorisView = Backbone.View.extend({

    tagName: 'ul',
    className: 'withTopBar',

    initialize: function () {
        this.appState = this.options.appState;
        this.listenTo(favoris, 'add remove change', this.render);
        this.render();
    },


    reBind: function () {
        _.each(this.lines, function (lineView) {
            lineView.delegateEvents();
        });
        return this;
    },

    render: function () {
        this.$el.empty();
        this.lines = [];

        if (favoris.isEmpty()) {
            this.$el.append('<div class="no-content">Vous n’avez pas de favori</div>');
            return;
        }

        favoris.each(_.bind(function (favori) {
            var lineView = new ListFavoriLineView({model: favori, appState: this.appState}).render();
            this.$el.append(lineView.el);
            this.lines.push(lineView);
        }, this));

        return this;
    }
});