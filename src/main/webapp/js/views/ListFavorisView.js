var ListFavorisView = Backbone.View.extend({

    tagName: 'ul',
    className: 'withTopBar',

    initialize: function () {
       // this.collection = new FavorisCollection();
        this.listenTo(this.collection, 'add', this.render);
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

      if (this.collection.isEmpty()) {
            this.$el.append('<div class="no-favori">Pas de favori</div>');
            return;
        }

        this.collection.each(_.bind(function (favori) {
            var lineView = new ListLineView({model:favori, lineTemplate:"#list-favoris-tmpl", extendedLineTemplate : "#list-line-extended-tmpl"}).render();
            this.$el.append(lineView.el);
            this.lines.push(lineView);
        }, this));

        return this;
    }
});