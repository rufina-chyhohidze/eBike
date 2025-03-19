const DOMAIN_NAME = window.location.hostname + (window.location.port ? `:${window.location.port}` : '');

import { customerFound, showCustomerBikes } from "./start-test.js";

const DOMAIN_NAME = "localhost:8080"


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
    jsonData["bikeOwnerId"] = document.getElementById("bikeOwnerId-form").value;
    console.log(jsonData)

    const response = await fetch("/api/save/bike", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(jsonData)
    });

    if (response.ok) {
        console.log("Bike successfully saved");
        // close modal - bike creation modal should be closed when bike successfully created
        const bikeModalCloseButton = document.getElementById("close-modal-bike-creation");
        bikeModalCloseButton.click();

        clearAllInputsFromForm();
        void showCustomerBikes(customerFound.id);
    } else {
        console.log("Error while saving bike: " + response.status);
    }
});

function clearAllInputsFromForm() {
    // Clear all input elements (text, number, etc.) and select elements
    const elements = document.querySelectorAll('input, select');
    elements.forEach(element => {
        element.value = '';  // Clear the value of the element
    });
}

function webSocketCheck(data) {
    const socket = new WebSocket(`ws://${DOMAIN_NAME}/ws/status`);
    socket.onopen = function () {
        socket.send(data.id); // Replace with actual test ID
    };
    socket.onmessage = function (event) {
        if (event.data.includes("Test completed")) {
            console.log("Test completed. You can now proceed.");
            socket.close();
            loadingDiv.classList.add("d-none");
            form.classList.remove("d-none");
            retrieveReport(data.id);
        }
    };

}

function retrieveReport(id) {
    const socket = new WebSocket(`ws://${DOMAIN_NAME}/ws/result`);
    socket.onopen = function () {
        socket.send(id);
    }

    socket.onmessage = function (event) {
        console.log("Report saved");
        socket.close();
        const reportId = event.data;
        console.log("Report ID received: " + reportId);
        window.location.href = `/technician/test/success/${reportId}`;
    }


}

export  {
    webSocketCheck
}


