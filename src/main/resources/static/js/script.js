var map;
var markers = [];       // markers array to hold new markers
var givenMarkers = [];  // markers array to hold markers of previously saved locations

function createMap () {

    var options = {
        center: new google.maps.LatLng(41.881832, -87.623177),
        zoom: 10
    };
    map = new google.maps.Map(document.getElementById('map'), options);


    /* enable the user to drop a pin */

    // add listener for click on map
    map.addListener('click', function(event) {
        if (markers.length >= 1) {
            deleteMarkers();
        }

        // add marker
        addMarker(event.latLng, markers);

        // add to thymeleaf
        document.getElementById('newLocation').innerHTML = event.latLng;
    });

    // add marker to map and push to markers array
    function addMarker(coords, markersArray) {
        var marker = new google.maps.Marker({
            position: coords,
            map: map
        });
        markersArray.push(marker);
    }

    // sets map on all markers in array
    function setMapOnMarkers(myMap) {
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(myMap);
        }
    }

    // deletes all markers in array
    function deleteMarkers() {
        // remove markers from map first
        setMapOnMarkers(null);
        // clear array
        markers = [];
    }


    /* place all previous markers on the map */

    // load button
    window.onload = function() {
        var btn = document.getElementById('loadLocations');
        btn.onclick = displayGivenLocations;
    }

    function displayGivenLocations() {
        // get all locations from thymeleaf element
        var coordinates = document.getElementsByClassName('loc');

        // loop over locations
        for (var i = 0; i < coordinates.length; i++) {
            console.log(coordinates[i].innerHTML);
            displayLocationAt(coordinates[i].innerHTML);
        }
    }

    function displayLocationAt(coords) {
        var n = coords.indexOf(',');        // find break in the string between lat and lng
        var myLat = parseFloat(coords.substring(1,n));
        var myLng = parseFloat(coords.substring(n+1, coords.length-1));
        var myLatLng = {lat: myLat, lng: myLng};
        addMarker(myLatLng, givenMarkers);  // add marker at given location
    }


    /* enable searchBox */

    var input = document.getElementById('search');
    var searchBox = new google.maps.places.SearchBox(input);

    // add listener for zooming, only search within these new bounds
    map.addListener('bounds_changed', function() {
        searchBox.setBounds(map.getBounds());
    });

    // add listener for a search
    searchBox.addListener('places_changed', function () {
        var places = searchBox.getPlaces();

        if (places.length === 0)
            return;

        var bounds = new google.maps.LatLngBounds();

        places.forEach(function (p) {
            if (!p.geometry)
                return;

            if (p.geometry.viewport)
                bounds.union(p.geometry.viewport);
            else
                bounds.extend(p.geometry.location);
        });

        map.fitBounds(bounds);
    });

}