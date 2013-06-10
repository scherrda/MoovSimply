var ListView = Backbone.View.extend({

    className: 'withTopBar',

    render: function () {
        this.$el.html($('#list-tmpl').html());
        this.renderExampleLine();
        return this;
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