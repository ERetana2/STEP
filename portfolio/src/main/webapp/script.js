// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function redirect() {
  window.location.href = 'game.html';
}

/** Hides social media content when image dropdown is toggled */
function photoDropdown() {
  const mediaLinks = document.getElementById('drop-down-container');
  const imgCaret = document.getElementById('img-caret');
  if (mediaLinks.style.display == 'block') {
    imgCaret.style.transform = 'rotate(180deg)';
    imgCaret.style.transition = '300ms';
    imgCaret.style.color = 'whitesmoke';
    mediaLinks.style.display = 'none';
  } else {
    imgCaret.style.transform = 'rotate(0deg)';
    imgCaret.style.transition = '300ms';
    imgCaret.style.color = 'green';
    mediaLinks.style.display = 'block';
  }
}

function refreshComments() {
  const infoNode = document.getElementById('more-info');
  while (infoNode.firstChild != null) {
    infoNode.removeChild(infoNode.lastChild);
  }
}

/** Obtain comment data from datastore and display them on page */
function getData() {
  refreshComments();
  const numContactsToDisplay = document.getElementById('quantity').value;
  const link = '/data?numContactsToDisplay=' + numContactsToDisplay;

  // fetch  the data and create a list containing the message of each contact
  fetch(link).then((response) => response.json()).then((contacts) => {
    console.log('Hello World');
    const infoContainer = document.getElementById('more-info');
    contacts.forEach((contact) => {
      const currContact = document.createElement('li');
      const userInfo = document.createTextNode(
          contact.firstName + ' ' + contact.lastName + ' says ' +
          contact.subject);
      currContact.appendChild(userInfo);
      infoContainer.appendChild(currContact);
    });
  });
}

function deleteData() {
  // upon request navigate to the delete servlet and remove all comments from
  // datastore
  if (confirm('Do you wish to Delete all the comments ?')) {
    const request = new Request('/delete-data', {method: 'POST'});
    fetch(request).then((results) => getData());
  }
}

/** Allow the user to login, when logged in -> display contact form */
function userLogin() {
  fetch('/login').then((response) => response.json()).then((currUser) => {
    document.getElementById('form-overlay-text').innerHTML =
        currUser.loginMessage;
    if (currUser.isLoggedIn) {
      document.getElementById('form-overlay').style.display = 'block';
      document.getElementById('delete-btn').style.display = 'inline';
    } else {
      document.getElementById('form-overlay').style.display = 'none';
      document.getElementById('delete-btn').style.display = 'none';
    }
  });
}

/** Initialize a map utlizing google's Map API for web */
function initMap() {
  // colors for styles
  const ORANGE = '#d59563';
  const OPAQUE_BLUE = '#263c3f';
  const DIM_GREEN = '#6b9a76';
  const GREY = '#38414e';
  const DARKER_GREY = '#212a37';
  const LIGHT_GREY = '#9ca5b3';
  const LIGHT_BROWN = '#746855';
  const DARK_BLUE = '#1f2835';
  const BEIGE = '#f3d19c';
  const GREY_BLUE = '#2f3948';
  const BLACK_BLUE = '#17263c';
  const DARK_BABYBLUE = '#515c6d';

  const markerPos = {lat: 31.770581604323954, lng: -106.50421142578125};
  const marker =
      new google.maps.Marker({position: markerPos, title: 'UTEP! My school.'});
  const map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 31.7869021, lng: -106.3127764},
    zoom: 15,
    // set styles for dark mode
    styles: [
      {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]}, {
        featureType: 'administrative.locality',
        elementType: 'labels.text.fill',
        stylers: [{color: ORANGE}]
      },
      {
        featureType: 'poi',
        elementType: 'labels.text.fill',
        stylers: [{color: ORANGE}]
      },
      {
        featureType: 'poi.park',
        elementType: 'geometry',
        stylers: [{color: OPAQUE_BLUE}]
      },
      {
        featureType: 'poi.park',
        elementType: 'labels.text.fill',
        stylers: [{color: DIM_GREEN}]
      },
      {featureType: 'road', elementType: 'geometry', stylers: [{color: GREY}]},
      {
        featureType: 'road',
        elementType: 'geometry.stroke',
        stylers: [{color: DARKER_GREY}]
      },
      {
        featureType: 'road',
        elementType: 'labels.text.fill',
        stylers: [{color: LIGHT_GREY}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry',
        stylers: [{color: LIGHT_BROWN}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry.stroke',
        stylers: [{color: DARK_BLUE}]
      },
      {
        featureType: 'road.highway',
        elementType: 'labels.text.fill',
        stylers: [{color: BEIGE}]
      },
      {
        featureType: 'transit',
        elementType: 'geometry',
        stylers: [{color: GREY_BLUE}]
      },
      {
        featureType: 'transit.station',
        elementType: 'labels.text.fill',
        stylers: [{color: ORANGE}]
      },
      {
        featureType: 'water',
        elementType: 'geometry',
        stylers: [{color: BLACK_BLUE}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.fill',
        stylers: [{color: DARK_BABYBLUE}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.stroke',
        stylers: [{color: DARK_BABYBLUE}]
      }
    ]
  });
  const contentString = '<div id="map-content">' +
      '<div id="site-description">' +
      '</div>' +
      '<h1 id="firstHeading" class="firstHeading">UTEP</h1>' +
      '<div id="bodyContent">' +
      '<p>Attending UTEP since Fall 2018 as a computer science major' +
      '. Utep is also recognized as one the most diverse schools along ' +
      'with successful engineering programs.</p>'
  '</div>' +
      '</div>';

  const infowindow = new google.maps.InfoWindow({content: contentString});
  marker.setMap(map);

  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });
}
