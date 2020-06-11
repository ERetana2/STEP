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
  if (mediaLinks.style.display == 'inline') {
    imgCaret.style.transform = 'rotate(180deg)';
    imgCaret.style.transition = '300ms';
    imgCaret.style.color = 'whitesmoke';
    mediaLinks.style.display = 'none';
  } else {
    imgCaret.style.transform = 'rotate(0deg)';
    imgCaret.style.transition = '300ms';
    imgCaret.style.color = 'green';
    mediaLinks.style.display = 'inline';
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
  let link = '/data?numContactsToDisplay=' + numContactsToDisplay;

  // fetch  the data and create a list containing the message of each contact
  fetch(link).then(response => response.json()).then((contacts) => {
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
  const request = new Request('/delete-data', {method: 'POST'});
  fetch(request).then((results) => getData());
}

/** Allow the user to login, when logged in -> display contact form */
function userLogin() {
  fetch('/login').then((response) => response.json()).then((currUser) => {
    document.getElementById('form-overlay-text').innerHTML =
        currUser.loginMessage;
    if (currUser.isLoggedIn) {
      document.getElementById('form-overlay').style.display = 'block';
    } else {
      document.getElementById('form-overlay').style.display = 'none';
    }
    // display delete all comments button to one specified user => admin
    if (currUser.email == 'eretana@google.com') {
      document.getElementById('delete-btn').style.display = 'inline';
    } else {
      document.getElementById('delete-btn').style.display = 'none';
    }
  });
}

/** Initialize a map utlizing google's Map API for web */
function initMap() {
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
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'poi',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'poi.park',
        elementType: 'geometry',
        stylers: [{color: '#263c3f'}]
      },
      {
        featureType: 'poi.park',
        elementType: 'labels.text.fill',
        stylers: [{color: '#6b9a76'}]
      },
      {
        featureType: 'road',
        elementType: 'geometry',
        stylers: [{color: '#38414e'}]
      },
      {
        featureType: 'road',
        elementType: 'geometry.stroke',
        stylers: [{color: '#212a37'}]
      },
      {
        featureType: 'road',
        elementType: 'labels.text.fill',
        stylers: [{color: '#9ca5b3'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry',
        stylers: [{color: '#746855'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry.stroke',
        stylers: [{color: '#1f2835'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'labels.text.fill',
        stylers: [{color: '#f3d19c'}]
      },
      {
        featureType: 'transit',
        elementType: 'geometry',
        stylers: [{color: '#2f3948'}]
      },
      {
        featureType: 'transit.station',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'water',
        elementType: 'geometry',
        stylers: [{color: '#17263c'}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.fill',
        stylers: [{color: '#515c6d'}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.stroke',
        stylers: [{color: '#17263c'}]
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
