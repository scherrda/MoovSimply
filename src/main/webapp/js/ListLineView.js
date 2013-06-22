var ListLineView = Backbone.View.extend({

    tagName: 'li',
    className: 'line',

    events: {
        'click .extend-arrow': 'toggle',
        'click .localize': 'showOnMap'
    },

    initialize: function () {
        this.template = Handlebars.compile($('#list-line-tmpl').html());
    },

    render: function () {
        this.$el.html(this.template(this.model.attributes));
        return this;
    },

    extend : function(){
        this.$('.line-extended').show();
        this.$('.extend-arrow').addClass('extended');

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

    showOnMap: function () {
        Backbone.history.navigate('#map', {trigger: true});
        this.model.trigger('show', this.model);
    },

    close: function () {
        this.remove();
        this.unbind();
    }

});