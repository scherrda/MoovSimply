var ListView = Backbone.View.extend({

    tagName: 'ul',
    className: 'withTopBar',

    initialize: function () {
        this.listenTo(stations, 'sync', this.showLines);
        this.showLines();
    },

    reBind: function () {
        _.each(this.lines, function (lineView) {
            lineView.delegateEvents();
        });
        return this;
    },

    showLines: function () {
        this.$el.empty();
        this.lines = [];

        if (!alreadyLocalized) {
            this.$el.append('<div class="no-station">Basculez vers le mode "Carte" pour vous géolocaliser.</div>');
            return;
        }
        if (stations.isEmpty()) {
            this.$el.append('<div class="no-station">Aucune station à moins de 500m.</div>');
            return;
        }

        stations.sort();
        stations.each(_.bind(function (station) {
            var lineView = new ListLineView({model: station}).render();
            this.$el.append(lineView.el);
            this.lines.push(lineView);
        }, this));
    },

    renderExampleLine: function () {
        var model = new Backbone.Model({
            line: 'm14',
            name: 'Gare de Lyon',
            distance: '100',
            time: '9h41',
            directions: [
                {
                    'direction': 'Olympiades',
                    time: '09h42'
                },
                {
                    'direction': 'Cours Saint Emilion',
                    time: '09h45'
                },
                {
                    'direction': 'Saint-Lazare',
                    time: '09h46'
                }
            ]
        });
        this.$el.append(new ListLineView({model: model}).render().el);
        this.$el.append(new ListLineView({model: model}).render().el);
    }

});