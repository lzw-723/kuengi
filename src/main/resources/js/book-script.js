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
            txt = decodeBase64ToUTF8(txt);
            window._content.v = txt;
            klog(txt)
        }
    }
})

function decodeBase64ToUTF8(encodedString) {
  // 使用 atob 解码 Base64 字符串为二进制字符串
  const binaryString = atob(encodedString);

  // 将二进制字符串转换为字节数组
  const bytes = new Uint8Array(binaryString.length);
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i);
  }

  // 使用 TextDecoder 解码为 UTF-8 字符串
  const decoder = new TextDecoder("utf-8");
  return decoder.decode(bytes);
}

function insertDemo(){
    // fetch("../sample.html")
    return "asdfsadfsdaf"
}