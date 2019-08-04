var map;

function createMap () {

    var options = {
        center:new google.maps.LatLng(41.881832, -87.623177),
        zoom: 10
    };

    map = new google.maps.Map(document.getElementById("map"), options);


    /* enable the user to drop a pin */

    // add listener for click on map
    google.maps.event.addListener(map, "click", function(event) {
        // add marker
        addMarker({coords:event.latLng});
    });

    // add marker to map function
    function addMarker(props) {
        var marker = new google.maps.Marker({
            position:props.coords,
            map:map,
            draggable:true
        });

        // check for content
        if(props.content) {
            var infoWindow = new google.maps.InfoWindow({
                content:props.content
            });

            marker.addListener("click", function() {
                infoWindow.open(map, marker);
            });
        }
    }


    /* enable searchBox */

    var input = document.getElementById("search");
    var searchBox = new google.maps.places.SearchBox(input);

    // add listener for zooming, only search within these new bounds
    map.addListener("bounds_changed", function() {
        searchBox.setBounds(map.getBounds());
    });

    // array of markers
    var markers = [];

    // add listener for a search
    searchBox.addListener("places_changed", function () {
        var places = searchBox.getPlaces();

        if (places.length === 0)
            return;

        markers.forEach(function (m) { m.setMap(null); });
        markers = [];

        var bounds = new google.maps.LatLngBounds();

        places.forEach(function (p) {
            if (!p.geometry)
                return;

            markers.push(new google.maps.Marker({
                map: map,
                title: p.name,
                position: p.geometry.location
            }));

            if (p.geometry.viewport)
                bounds.union(p.geometry.viewport);
            else
                bounds.extend(p.geometry.location);
        });

        map.fitBounds(bounds);
    });

}