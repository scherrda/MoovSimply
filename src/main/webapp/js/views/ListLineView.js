var ListLineView = Backbone.View.extend({

    tagName: 'li',
    className: 'line',

    events: {
        'click .extend-arrow': 'toggle',
        'click .localize': 'showOnMap'
    },

    initialize: function () {
        this.template = Handlebars.compile($('#list-line-tmpl').html());
        this.templateExtended = Handlebars.compile($('#list-line-extended-tmpl').html());
        this.listenTo(this.model, "change", this.renderDetail);
    },

    render: function () {
        console.log("rendering listlineview");
        this.$el.html(this.template(this.model.attributes));
        return this;
    },

    renderDetail: function () {
        console.log("rendering detail area");
        this.$('.line-extended').html(this.templateExtended(this.model.attributes));
        return this;
    },

    toggle: function () {
        console.log("in toggle");
        this.model.fetch();
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