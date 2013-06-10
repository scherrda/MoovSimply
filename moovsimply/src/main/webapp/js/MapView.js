var MapView = Backbone.View.extend({

    className: 'withTopBar',

    render: function () {
        this.$el.html($('#map-tmpl').html());
        _.defer(this.renderGMap);
        return this;
    },

    renderGMap: function () {
        this.gmap = new GMapView().render();
    }

});