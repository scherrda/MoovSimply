var GMapView = Backbone.View.extend({

    el: '#map-canvas',
    currentMarkers: [],

    render: function () {
        var mapOptions = {
            zoom: 14,
            center: new google.maps.LatLng(48.87525, 2.31110),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        this.map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);

        return this;
    }
});