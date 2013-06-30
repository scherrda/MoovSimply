var Station = Backbone.Model.extend({

    defaults: {
    },

    url: function () {
        return '/rest/moovin/' + this.get("serviceType").toLowerCase() + '/' + this.get("stationId");
    }
});
