document.addEventListener('alpine:initialized', () => {
    inject('alpine:initialized');
});

document.addEventListener('alpine:init', () => {
    inject('alpine:init');
})

var kuengi = 'wait for inject'
var worked = false;

const inject = (msg) => {
    if (worked || document.readyState === 'loading') {
        return;
    }
    worked = true;
    console.log("run in " + document.readyState);
    $('<button class="pure-button" x-data="{ count: 0 }" x-on:click="count++" x-text="count"></button>').appendTo('body');
    $('<button class="pure-button" x-on:click="window.kuengi.log(\'test\')">test</button>').appendTo('body');
    $('p').addClass('heti');
    $('<p>' + 'Kuengi: Injected ' + msg + '</p>').appendTo('body');
}

document.addEventListener("DOMContentLoaded", (event) => {
        inject();
    });

inject('direct');