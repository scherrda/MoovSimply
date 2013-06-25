var Station = Backbone.Model.extend({

    defaults : {
    },

    parse: function(response) {
        this.clear();//for the change events occur (because we use the same model)
        return response ;
    },

    url: function () {
        return '/rest/moovin/' + this.get("serviceType").toLowerCase() + '/' + this.get("stationId");
    },

    setDistanceToUser : function (distance) {
        this.set("distance",distance);
    }
});
