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

function loadComments() {
  fetch('/text').then(response => response.json()).then((comments) => { // Fetch = RPC request to server and waits then gets response
    const commentsListElement = document.getElementById('comment-container');
    comments.forEach((comment) => {
        const theCommentElement = document.createElement('li');
        theCommentElement.className = 'comment';
        theCommentElement.innerHTML = '';
        // theCommentElement.appendChild(createListElement('Post date: ' + comment.timestamp));
        theCommentElement.appendChild(createListElement("\"" + comment.message + "\"-" + comment.author));
        commentsListElement.appendChild(theCommentElement); //appendChild i
    });
  });
} //lamda functions, anonymous functions, NOTE: in Javascript, read Javascript promises

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
//creating list element twiceh

function requestTranslation() {
    const text = document.getElementById('text').value;
    const languageCode = document.getElementById('language').value;

    const resultContainer = document.getElementById('result');
    resultContainer.innerText = 'Loading...';

    const params = new URLSearchParams();
    params.append('text', text);
    params.append('languageCode', languageCode);

    fetch('/translate', {
        method: 'POST',
        body: params
    }).then(response => response.text())
    .then((translatedMessage) => {
        resultContainer.innerText = translatedMessage;
    });
}