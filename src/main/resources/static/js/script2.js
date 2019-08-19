var map;
var markers = [];       // markers array to hold new markers
var givenMarkers = [];  // markers array to hold markers of previously saved locations
var infoWindows = [];   // infoWindows array to hold windows

/* get report info on click here too */

function createMap () {

    var options = {
        center: new google.maps.LatLng(41.881832, -87.623177),
        zoom: 10
    };
    map = new google.maps.Map(document.getElementById('map'), options);


    /* enable the user to drop a pin */

    // add listener for click on map
    map.addListener('click', function(event) {

        // if clicking on new location to add new marker
        if (markers.length >= 1) {  // clear markers
            deleteMarkers();
        }

        // add marker to map, add to Thymeleaf
        // since I changed addMarker fn might need to adjust here
        addMarker(event.latLng, markers);
        document.getElementById('newLocation').innerHTML = event.latLng;

        if (event.placeId) {
            console.log('You clicked on ' + event.placeId);
        } else {
            console.log('Idk the placeId');
            // can i get this to match doc id selector
            var stringLatLng = event.latLng.toString();
            console.log("stringLatLng: " + stringLatLng);
            var docLatLng = document.querySelector("div.p." + stringLatLng);
            console.log("docLatLng: " + docLatLng);
        }

        // if clicking on existing marker, view infoWindow
        // close other info windows first
        /*if (infoWindows.length >= 1) {
            // close infoWindows
            // clear array
        }*/
    });

    // add marker to map and push to markers array
    function addMarker(oldCoords, coords, markersArray) {
        var marker = new google.maps.Marker({
            position: coords,
            map: map
        });

        // if there's a given report title
        /*if (reportTitle) {
            var infoWindow = new google.maps.InfoWindow({
                content: reportTitle
            });
        } else {}*/
        var infoWindow = new google.maps.InfoWindow();


        markersArray.push(marker);
        marker.addListener('click', function(){
            // insert alternative fn here

            var stringLatLng = oldCoords.toString();    // does this need .toString()??
            console.log("stringLatLng: " + stringLatLng);   // works fine

            var docLatLng = document.getElementById(stringLatLng);
            console.log("docLatLng: " + docLatLng.innerHTML);
            //var title = document.getElementById(stringLatLng).children[0];

            var title = docLatLng.children[0].children[0].textContent;
            console.log(title);

            var urgency = docLatLng.children[0].children[1].children[0].textContent;
            console.log(urgency);

            var description = docLatLng.children[1].children[2].textContent;
            console.log(description);

            // if other windows open first
            /*if (infoWindows.length >= 1) {
                // new fn?
                // close infoWindows
                this.infoWindow.close();
                // clear array
                infoWindows = [];
            }*/
            // not sure how to set content really... where's this
            infoWindow.setContent("whatup");
            infoWindow.open(map, marker);
            infoWindows.push(marker);
        });
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

        // load button for info window content
        /*var btn2 = document.getElementById('loadOne');
        btn2.onclick = infoWindow.open(map, oneMarker);*/
    }

    // get locations from thymeleaf and display all
    function displayGivenLocations() {

        var coordinates = document.getElementsByClassName('loc');

        // loop over locations
        for (var i = 0; i < coordinates.length; i++) {
            //console.log(coordinates[i].innerHTML);

            // convert coords to appropriate LatLng
            var oldLatLng = coordinates[i].innerHTML;
            var convertedLatLng = convertCoords(oldLatLng);

            // add marker
            addMarker(oldLatLng, convertedLatLng, givenMarkers);  // add marker at given location
        }
    }

    // convert coords to appropriate LatLng style
    function convertCoords(coords) {
        var n = coords.indexOf(',');        // find break in the string between lat and lng
        var myLat = parseFloat(coords.substring(1,n));
        var myLng = parseFloat(coords.substring(n+1, coords.length-1));
        var myLatLng = {lat: myLat, lng: myLng};
        return myLatLng;
    }


    /* Add Info Window */

   /* // create info window content
    var contentString = '<div id="infoWindowContent">' +
        '<h4>Report title</h4>' +
        '<div id="infoWindowBody">' +
            '<p>Report body.....</p>' +
            '<p><a href="#">Click me!</a></p>' +
        '</div>' +
        '</div>';
        */

    /*// create info window
    var infoWindow = new google.maps.InfoWindow({
        content: contentString
    });*/

    /*// create marker for info window
    var oneMarker = new google.maps.Marker({
        position: new google.maps.LatLng(41.881832, -87.623177),
        map: map,
        title: 'chi-town'
    });*/


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