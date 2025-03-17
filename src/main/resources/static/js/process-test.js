const DOMAIN_NAME = window.location.hostname + (window.location.port ? `:${window.location.port}` : '');


const form = document.querySelector("form");
const loadingDiv = document.getElementById("loading");
const testFormDiv = document.getElementById("test-form");


form.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Submit the form
    const formData = new FormData(form);
    const jsonData = {};
    formData.forEach((value, key) => {
        jsonData[key] = value;
    });
    console.log(jsonData)

    const response = await fetch("/api/start-test", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(jsonData)
    });

    const data = await response.json();

    testFormDiv.classList.add("d-none");
    loadingDiv.classList.remove("d-none");


    const socket = new WebSocket(`ws://${DOMAIN_NAME}/ws/status`);
    socket.onopen = function () {
        socket.send(data.id); // Replace with actual test ID
    };
    socket.onmessage = function (event) {
        if (event.data.includes("Test completed")) {
            alert("Test completed. You can now proceed.");
            socket.close();
            loadingDiv.classList.add("d-none");
            form.classList.remove("d-none");
            retrieveReport(data.id);
        }
    };


});

function retrieveReport(id) {
    const socket = new WebSocket(`ws://${DOMAIN_NAME}/ws/result`);
    socket.onopen = function () {
        socket.send(id);
    }
    socket.onmessage = function (event) {
        if (event.data.includes("Report saved")) {
            alert("Report saved");
            socket.close();
            window.location.href = "/test/success";
        }
    }


}


