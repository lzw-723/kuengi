window.onload = function () {
    let p = document.createElement('p')
    p.innerHTML = "onload"
    document.body.appendChild(p);
};

// var kuengi = false;
window.klog = (msg) => {
    console.log(msg);
    if (kuengi) {
        kuengi.log('web log: ' + msg);
    }
};
document.addEventListener('alpine:init', () => {
    window._content = Alpine.reactive({ v: '' });
    window.book = {
        load: (txt) => {
            window._content.v = txt;
            klog(txt)
        }
    }
})