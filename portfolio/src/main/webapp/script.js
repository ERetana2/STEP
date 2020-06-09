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

function getData() {
  refreshComments();
  var numContactsToDisplay = document.getElementById('quantity').value;
  var link = '/data?numContactsToDisplay=' + numContactsToDisplay

  fetch(link).then(response => response.json()).then((contacts) => {
    const infoContainer = document.getElementById('more-info');
    contacts.forEach((contact) => {
      var currContact = document.createElement('li');
      var userInfo = document.createTextNode(
          contact.firstName + ' ' + contact.lastName + ' says ' +
          contact.subject);
      currContact.appendChild(userInfo);
      infoContainer.appendChild(currContact);
    });
  });
}

function deleteData() {
  const request = new Request('/delete-data', {method: 'POST'});
  fetch(request).then((results) => getData());
}

function onSignIn(googleUser) {
  // Useful data for your client-side scripts:
  var profile = googleUser.getBasicProfile();
  console.log(
      'ID: ' + profile.getId());  // Don't send this directly to your server!
  console.log('Full Name: ' + profile.getName());
  console.log('Given Name: ' + profile.getGivenName());
  console.log('Family Name: ' + profile.getFamilyName());
  console.log('Image URL: ' + profile.getImageUrl());
  console.log('Email: ' + profile.getEmail());

  // The ID token you need to pass to your backend:
  var id_token = googleUser.getAuthResponse().id_token;
  console.log('ID Token: ' + id_token);
}

function userLogin() {
  const request = new Request('/login', {method: 'GET'});
  fetch(request).then(response => {
    window.location.href = '/login';
  });
}


function initMap() {
  var markerPos = {lat: 31.770581604323954, lng: -106.50421142578125};
  var marker =
      new google.maps.Marker({position: markerPos, title: 'UTEP! My school.'});
  marker.setMap(map);
  var map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 31.7869021, lng: -106.3127764},
    zoom: 15,
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
  var contentString = '<div id="map-content">' +
      '<div id="site-description">' +
      '</div>' +
      '<h1 id="firstHeading" class="firstHeading">UTEP</h1>' +
      '<div id="bodyContent">' +
      '<p>Attending UTEP since Fall 2018 as a computer science major' +
      '. Utep is also recognized as one the most diverse schools along with successful' +
      ' engineering programs.</p>'
  '</div>' +
      '</div>';

  var infowindow = new google.maps.InfoWindow({content: contentString});

  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });
}
