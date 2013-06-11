var ListLineView = Backbone.View.extend({

    className: 'line',

    events: {
        'click .extend-arrow': 'toggle'
    },

    render: function () {
        this.$el.html(Handlebars.compile($('#list-line-tmpl').html())(this.model.toJSON()));
        return this;
    },

    toggle: function () {
        var $lineExtended = this.$('.line-extended'),
            $arrow = this.$('.extend-arrow');

        if ($lineExtended.hasClass('extended')) {
            $lineExtended
                .slideToggle(300)
                .removeClass('extended');
        } else {
            $lineExtended
                .slideToggle(300)
                .addClass('extended');
        }
        $arrow.toggleClass('extended');
    }

});