var StationsCollection = Backbone.Collection.extend({
    model: Station,

    url: function () {
        if (this.lat && this.lng) {
            return '/rest/moovin/around?LAT=' + this.lat + '&LNG=' + this.lng;
        }
        //return '/rest/moovin/all/light';
        return '/rest/moovin/transports';
        //return 'stations.json?LAT=' + this.lat + '&LNG=' + this.lng;
    },

    comparator: function (station) {
        return station.get('distance');
    },

    setCoordinates: function (lat, lng) {
        this.lat = lat == null ? 0 : lat;
        this.lng = lng == null ? 0 : lng;
        return this;
    },

    filterByName: function (name) {
        return new StationsCollection(this.filter(function (model) {
            return model.get('name').indexOf(name) !== -1;
        }));
    },

    filterByTypes: function (types) {
        return new StationsCollection(this.filter(function (model) {
            return _.find(types, function (type) {
                    return type == model.get('type');
                    //_.contains(types, model.get('type'));
                }
            );
        }));
    }

});

var stations = new StationsCollection();

var allStations = new StationsCollection();
allStations.fetch();