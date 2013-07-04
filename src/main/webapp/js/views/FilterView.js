var FilterView = Backbone.View.extend({

    tagName: 'ul',
    className: 'transportFilter',

    events: {
        'change input[type=checkbox]': 'onFilter'
    },

    initialize: function () {
        this.filterModel = this.model;

        this.render();
    },

    render: function () {
        this.$el.html($('#transport-filter-tmpl').html());
        return this;
    },

    onFilter: function () {
        var types = this.$('input:checked').map(function () {
            return $(this).val();
        }).get();
        this.filterModel.set('transportTypes', types);
    },

    toggle: function () {
        this.$el.parent().slideToggle(300);
    },

    hide: function () {
        this.$el.parent().hide();
    }

});
