var map;
var newMarkers = [];
var givenMarkers = [];
var infoWindows = [];

function createMap() {
    var origin = {lat: 38.626090, lng: -90.220173};

    var options = {
        center: origin,
        zoom: 11
    };

    map = new google.maps.Map(document.getElementById('map'), options);

    map.addListener('click', function(event) {
        if (newMarkers.length >= 1) {  // clear markers
            deleteMarkers();
        }
        // add marker to map, add to Thymeleaf
        addMarker(null, event.latLng, newMarkers);
        document.getElementById('newLocation').innerText = event.latLng;
    });

    // load button
    window.onload = function() {
        displayGivenLocations();
    }

    // enable searchBox
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

// get locations from thymeleaf and display all
function displayGivenLocations() {

    var coordinates = document.getElementsByClassName('loc');

    // loop over locations
    for (var i = 0; i < coordinates.length; i++) {

        // convert coords to appropriate LatLng
        var stringLatLng = coordinates[i].innerText;
        var convertedLatLng = convertCoords(stringLatLng);

        // add marker
        addMarker(stringLatLng, convertedLatLng, givenMarkers);
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

function addMarker(oldCoords, coords, markersArray) {
    var marker = new google.maps.Marker({
        position: coords,
        map: map
    });
    markersArray.push(marker);
    var infoWindowContent = document.getElementById('infowindow-content');

    // add marker listener
    marker.addListener('click', function() {
        if (!oldCoords) {
            return;
        }

        closeInfoWindows();
        getPlaceInformation(oldCoords, marker, infoWindowContent);
    });
}

function closeInfoWindows() {
    if (infoWindows.length != 0) {
        for (var i=0; i < infoWindows.length; i++) {
            infoWindows[i].close();
        }
    }
}

function getPlaceInformation(stringLatLng, marker, infoWindowContent) {

    // find specific report by latLng value (from thymeleaf)
    var docLatLng = document.getElementById(stringLatLng.toString());

    if (!docLatLng) {
        return;
    }

    var infoWindow = new google.maps.InfoWindow({maxWidth: 300});

    // get specific info
    var reportId = docLatLng.children[0].children[2].innerText;
    var title = docLatLng.children[0].children[0].innerText;
    var urgency = docLatLng.children[0].children[1].children[0].innerText;
    var description = docLatLng.children[1].children[2].innerText;

    // set content
    infoWindowContent.children['info-title'].innerHTML = '<a href="/report/' + reportId + '">' + title + '</a>';
    infoWindowContent.children['info-urgency'].innerText = "Urgency level: " + urgency;
    infoWindowContent.children['info-body'].innerText = description;

    infoWindow.setContent(infoWindowContent);
    infoWindow.open(map, marker);
    infoWindows.push(infoWindow);
}

// sets map on all markers in array
function setMapOnMarkers(myMap) {
    for (var i = 0; i < newMarkers.length; i++) {
        newMarkers[i].setMap(myMap);
    }
}

// deletes all markers in array
function deleteMarkers() {
    // remove markers from map first
    setMapOnMarkers(null);
    // clear array
    newMarkers = [];
}