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

        var detail = new DetailStation({serviceType : this.model.get('serviceType'), stationId : this.model.get('stationId')});
        this.model.detail = detail;
        this.listenTo(this.model.detail, "change", this.renderDetail);
    },

    render: function () {
        this.$el.html(this.template(this.model.attributes));
        this.renderDetail();
        return this;
    },

    renderDetail: function () {
        this.$('.line-extended').html(this.templateExtended(this.model.detail.attributes));
        return this;
    },

    toggle: function () {
        this.model.detail.fetch();
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