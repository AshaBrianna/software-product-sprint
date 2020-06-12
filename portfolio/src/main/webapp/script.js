// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


function addRandomFact() {
  const funfacts =
      ["I have a orange, long hair Maine Coon cat mix named Kitty Kitty", "I'm 6'1\"", "I can make origami cranes without looking at the paper", "I'm still in denial of my fear of heights", "I make excellent baklava", "I don't know how to ride a bike", "My highschool volleyball team won the CA State Championships", "I have 5 wisdom teeth"];

  // Pick a funfact.
  const funfact = funfacts[Math.floor(Math.random() * funfacts.length)];

  // Add it to the page.
  const funfactContainer = document.getElementById('funfact-container');
  funfactContainer.innerText = funfact;
}

// function getGreetingUsingArrowFunctions() {
//   fetch('/data').then(response => response.text()).then((greeting) => {
//     document.getElementById('greeting-container').innerText = greeting;
//   });
// }

//fetch JSON string from the server
function getGreetingUsingArrowFunctions(){
    fetch('/data')  // sends a request to /my-data-url
    .then(response => response.json()) // parses the response as JSON
    .then((greeting) => { // now we can reference the fields in myObject!
        // console.log(myObject.x);
        // console.log(myObject.y);
        // console.log(myObject.z);
        document.getElementById('greeting-container').innerText = greeting;
    });
}