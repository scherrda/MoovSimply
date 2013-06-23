var Station = Backbone.Model.extend({

    defaults : {
    },

    url: function () {
        return '/rest/moovin/' + this.get("type") + '/' + this.get("lineNumber");
    }
});
