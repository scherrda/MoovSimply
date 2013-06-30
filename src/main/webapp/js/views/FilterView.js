var FilterView = Backbone.View.extend({

    events : {
        'change input[type=checkbox]': 'onFilter'
    },

    initialize: function() {
        this.filterModel = this.options.model ;

        this.render();
        if(!this.options.display){
            this.display = false;
            this.hide();
        }
    },

    render: function () {
        this.$el.html($('#transport-filter-tmpl').html());
        return this;
    },

    toggle : function() {
        this.$el.toggle();
    },

    hide : function() {
        this.display = false;
        this.$el.hide();
        return this;
    },

    show : function() {
        this.display = true;
        this.$el.show();
        return this;
    },

    onFilter : function(event) {
        var types = this.$('input:checked').map(function() {
            return $(this).val();
        }).get();
        this.filterModel.set("transportTypes", types);
        // TODO : implementer filterStations
        //stations.filterStations();
    }

});
