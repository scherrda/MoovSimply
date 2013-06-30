var AppState = Backbone.Model.extend({
    defaults : {
        currentStation :null,
        nextGeolocCenterOnUser : true,
        mapCenter : {pos : new google.maps.LatLng(48.87525, 2.31110)}, //Paris
        search : null,
        transportTypes: ["METRO", "RER", "TRAM", "BUS", "AUTOLIB", "VELIB", "TRAIN"]
    },

    getCenterCoordinates : function(){
        return {latitude : this.get("mapCenter").pos.lat(), longitude : this.get("mapCenter").pos.lng()};
    }

});
