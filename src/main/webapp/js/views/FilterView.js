var FilterView = Backbone.View.extend({

    tagName: 'ul',
    className: 'transportFilter',

    events : {
        'change input[type=checkbox]': 'onFilter'
    },

    initialize: function() {
        this.filterModel = this.options.model ;

        this.render();
    },

    render: function () {
        this.$el.html($('#transport-filter-tmpl').html());
        return this;
    },

    onFilter : function(event) {
        var types = this.$('input:checked').map(function() {
            return $(this).val();
        }).get();
        this.filterModel.set("transportTypes", types);
    }

});
