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
    imgCaret.style.transition = '500ms';
    imgCaret.style.color = 'whitesmoke';
    mediaLinks.style.display = 'none';
  } else {
    imgCaret.style.transform = 'rotate(0deg)';
    imgCaret.style.transition = '500ms';
    imgCaret.style.color = 'green';
    mediaLinks.style.display = 'inline';
  }
}
function refreshComments() {
  const infoNode = document.getElementById('more-info');
  while (infoNode.firstChild) {
    infoNode.removeChild(infoNode.lastChild);
  }
}
function getData() {
  refreshComments();
  var numContactsToDisplay = document.getElementById('quantity').value;
  var link = '/data?numContactsToDisplay=' + numContactsToDisplay

  fetch(link).then(response => response.json()).then((contacts) => {
    contacts.forEach((contact) => {
      var currContact = document.createElement('p');
      var userInfo = document.createTextNode(
          'Name:' + contact.firstName + '-  Last Name:' + contact.lastName +
          '-  Message = >' + contact.subject);
      currContact.appendChild(userInfo);
      document.getElementById('more-info').appendChild(currContact);
    });
  });
}
