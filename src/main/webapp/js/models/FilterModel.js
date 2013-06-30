var FilterModel = Backbone.Model.extend({

    defaults: {
        transportTypes: ["METRO", "RER", "TRAM", "BUS", "AUTOLIB", "VELIB", "TRAIN"]
    }

});

var filterModel = new FilterModel();
