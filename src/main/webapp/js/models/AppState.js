var AppState = Backbone.Model.extend({


    defaults : {
        currentMarker :null,
        mapCenter : new google.maps.LatLng(48.87525, 2.31110) //Paris
    }

});
