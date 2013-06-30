var AppState = Backbone.Model.extend({
    defaults : {
        currentStation :null,
        nextGeolocCenterOnUser : true,
        mapCenter : new google.maps.LatLng(48.87525, 2.31110), //Paris
        search : null
    },

    getCenterCoordinates : function(){
        return {latitude : this.get("mapCenter").lat(), longitude : this.get("mapCenter").lng()};
    }

});
