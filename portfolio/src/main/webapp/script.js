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

/**
 * Adds a greeting to the page.
 */
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(experienceChart);

let count = 0;
let map;

function addRandomGreeting() {
  const greetings =
      ['I ran Track and Cross Country all throughout high school',
       'I enjoy baking',
        'My favorite book is The Fountainhead by Ayn Rand', 
        'I am from Guyana',
        'My favorite food is burgers!',
        'My current binge is Greys Anatomy'];

  // Pick a random greeting.
  if(count >= greetings.length) {
      count = 0;
  }
  const greeting = greetings[count];
  count++;
  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function addComment() {
    fetch('/data',{method: "POST", headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'},
      body: getUserInput()
    }).then(response => response.json()).then((stats) => {
        const nameContainer = document.getElementById('comment-container');
        nameContainer.innerHTML = "";
        for(let i = 0; i < stats.length; i++) {
            nameContainer.appendChild(
            createListElement(stats[i]));
        }
    });
}

function experienceChart() {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Language');
    data.addColumn('number', 'Years');
        data.addRows([
            ['Python', 2],
            ['Html/Css', 5],
            ['Java', 4],
            ['C++', 1],
            ['JS', 1]
        ]);

    const options = {
        'title': 'Experience',
        'height': 550,
        'is3D': true,
        color: 'white',
        backgroundColor: 'transparent',
        legend : { position: 'bottom' }
    };

    const chart = new google.visualization.PieChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
}

function initMap() {
    const map = new google.maps.Map(
        document.getElementById('map'), {zoom: 3, center: {lat: 2.8, lng: -187.3}});
    
    const NYMarker = new google.maps.Marker({
        position: {lat: 40.77, lng: -73.97},
        map: map,
        title: 'Central Park'
    });

    const GYMarker = new google.maps.Marker({
        position: {lat: 5.68, lng: -57.39},
        map: map,
        title: 'Guyana'
    });
    
    const CAMarker = new google.maps.Marker({
        position: {lat: 38.01, lng: -122.56},
        map: map,
        title: 'San Rafeal'
    });

     const UKMarker = new google.maps.Marker({
        position: {lat: 53.51, lng: -2.31},
        map: map,
        title: 'Manchester, UK'
    });
}

function getUserInput() {
    return document.getElementById("text-area").value;
}

function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}
