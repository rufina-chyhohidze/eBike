const idElement = document.getElementById("testId");

const url = `http://localhost:8080/report/${idElement.value}`;

const qr = qrcode(0, 'L');
qr.addData(url);
qr.make();

document.getElementById('qrcode').innerHTML = qr.createImgTag(10);
