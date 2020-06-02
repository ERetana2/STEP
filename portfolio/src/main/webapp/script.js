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

function redirect(){
    window.location.href="game.html";
}
function photoDropdown(){
    const mediaLinks = document.getElementById('drop-down-container');
    if(mediaLinks.style.display == 'none'){
        mediaLinks.style.display = 'inline';
    }else{
       mediaLinks.style.display = 'none'; 
    }
}
function getData() {
    fetch('/data').then(response => response.json()).then((tasks) => {
        tasks.forEach((task) =>{
            var currTask = document.createElement("p");
            var userInfo = document.createTextNode("Name:"+ task.firstName + "-\tLast Name:" + task.lastName + '-\tMessage = >' + task.subject);
            currTask.appendChild(userInfo);
            document.getElementById('more-info').appendChild(currTask);
        });
    });
}
