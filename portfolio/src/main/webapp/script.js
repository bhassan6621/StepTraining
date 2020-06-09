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
 * Adds a random greeting to the page.
 */

var count = 0;

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

function getServerName() {
    fetch('/data').then(response => response.json()).then((stats) => {
        const nameContainer = document.getElementById('name-container');
        nameContainer.innerHTML="";
        for(let i = 0; i < stats.length; i++){
            nameContainer.appendChild(
            createListElement(stats[i]));
        }
    });
}

function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}
