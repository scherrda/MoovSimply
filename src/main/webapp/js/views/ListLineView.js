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

        this.listenTo(this.model, 'sync', this.renderDetail);
    },

    render: function () {
        this.$el.html(this.template(this.model.attributes));
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