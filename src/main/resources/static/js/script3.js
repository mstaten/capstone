/* script 3 -- incorporating google suggestions w/ clickeventhandlers etc */
/* get report info by using event handlers */
var map;
var markers = [];
var givenMarkers = [];
var givenCoordinates = [];

/* create map, initialize button and clickHandler */
function createMap() {
    var origin = {lat: 41.881832, lng: -87.623177};

    var options = {
        center: origin,
        zoom: 10
    };

    var myInfoWindow = null;

    map = new google.maps.Map(document.getElementById('map'), options);

    var clickHandler = new ClickEventHandler(map, origin);

    // load button - click event for DOM, doesn't need sep fn
    window.onload = function() {
        var btn = document.getElementById('loadLocations');
        btn.onclick = displayGivenLocations;
    }
    // automatically display when I load map ?
}

/** constructor **/
var ClickEventHandler = function(map, origin) {
    this.origin = origin;
    this.map = map;
    this.infoWindow = new google.maps.InfoWindow;
    this.infoWindowContent = document.getElementById('infowindow-content');
    this.infoWindow.setContent(this.infoWindowContent);

    // Listen for clicks on the map
    this.map.addListener('click', this.handleClick.bind(this));
};


// handle a click on map, distinguish bw adding new marker and
// clicking on existing marker
ClickEventHandler.prototype.handleClick = function(event) {
    // why doesn't this behave same as map.addlistener from script2?
    // event.latLng doesn't work for markers already on map
    // BLOCKER, START HERE

    // check whether button pressed yet; if not yet, click it!!!
    // auto display so don't need to check

    console.log("BLOCKER, START HERE");

    // if place has placeId
    if (event.placeId) {
        // prevent default window from showing
        event.stop();
    }
    // display info (only works for
    this.getPlaceInformation(event.latLng);

    console.log('You clicked on ' + event.latLng);  // prints only if making new marker
    // if clicking on new location to add new marker
    if (markers.length >= 1) {  // clear markers
        deleteMarkers();
    }
    // add marker to map, add to Thymeleaf
    addMarker(event.latLng, markers);
    document.getElementById('newLocation').innerHTML = event.latLng;
};


// function to open window w/place info (first closes other windows)
ClickEventHandler.prototype.getPlaceInformation = function(latLng) {
    var me = this;
    var stringLatLng = latLng.toString();

    // find specific report by latLng value (from thymeleaf)
    var docLatLng = document.getElementById(stringLatLng);

    if (!docLatLng) {
        return;
    }
    // get specific info
    var title = docLatLng.children[0].children[0].textContent;
    var urgency = docLatLng.children[0].children[1].children[0].textContent;
    var description = docLatLng6.children[1].children[2].textContent;

    console.log(title);
    console.log(urgency);
    console.log(description);
    // display info in infoWindow
    me.infoWindow.close();
    me.infoWindow.setPosition(latLng);  // i think this is right? BLOCKER
    me.infoWindowContent.children['info-title'].textContent = title;
    me.infoWindowContent.children['info-urgency'].textContent = urgency;
    me.infoWindowContent.children['info-body'].textContent = description;
    me.infoWindow.open(me.map);

};




    // (upon btn click) get locations from thymeleaf and display all on map
    function displayGivenLocations() {

        givenCoordinates = document.getElementsByClassName('loc');

        // loop over locations
        for (var i = 0; i < givenCoordinates.length; i++) {
            // convert coords to appropriate LatLng
            var convertedLatLng = convertCoords(givenCoordinates[i].innerHTML);
            console.log(convertedLatLng); // ??
            // add marker, add to givenMarkers array to keep track
            addMarker(convertedLatLng, givenMarkers);
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


    // add marker to map and push to markers array
    function addMarker(coords, markersArray) {
        var marker = new google.maps.Marker({
            position: coords,
            map: map
        });
        markersArray.push(marker);
        // add marker listener
        marker.addListener('click', function() {
            // call getplaceinfo
            console.log("in addMarker fn -- marker.addlistener");
            if (myInfoWindow) {
                myInfoWindow.close();
            }

            myInfoWindow = new google.maps.InfoWindow();

            this.getPlaceInformation(event.latLng);// how do i get this??
            // need THIS here, bc getPlaceInfo needs THIS
            // in marker.addListener,
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

