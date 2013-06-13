var ListLineView = Backbone.View.extend({

    tagName: 'li',
    className: 'line',

    events: {
        'click .extend-arrow': 'toggle'
    },

    initialize: function () {
        this.template = Handlebars.compile($('#list-line-tmpl').html());
    },

    render: function () {
        this.$el.html(this.template(this.model.toJSON()));
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
    },

    close: function () {
        this.remove();
        this.unbind();
    }

});