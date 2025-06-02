import { customerFound, showCustomerBikes } from "./startmine.js";
import { csrfToken, csrfHeader } from './utils/csrf.js'
import qrcodeGenerator from "./qr-code-generation.js";

// doesnt work with https const DOMAIN_NAME = window.location.hostname + (window.location.port ? `:${window.location.port}` : '');
const wsProtocol = window.location.protocol === "https:" ? "wss" : "ws";
const wsBaseUrl = `${wsProtocol}://${window.location.host}`;




const form = document.getElementById("newBikeForm");
const loadingDiv = document.getElementById("loading");
const testFormDiv = document.getElementById("test-form");
const newBikeFormGrid = document.getElementById("newBikeFormGrid");
const existingBikeModel = document.getElementById("existing-bike-model");

const bikeModelsResponse = await fetch("/api/bike-models")
let bikeModels = {}
if (bikeModelsResponse.ok) {
    bikeModels = await bikeModelsResponse.json();
    bikeModels.map(model => existingBikeModel.innerHTML += `
    <option id="${model.id}" value="${model.id}">${model.brand} - ${model.type}</option>
    `)
}

const immutableFields = document.querySelectorAll(".immutable");
existingBikeModel.addEventListener("change", () => {
    if (existingBikeModel.value === "0") {
        // SHOW: Remove hidden, then animate in
        immutableFields.forEach(el => {
            el.classList.remove("hidden");
            // Force reflow so animation can trigger
            void el.offsetWidth;
            el.classList.remove("opacity-0", "scale-95");
            el.classList.add("opacity-100", "scale-100");
        });
            newBikeFormGrid.classList.remove("justify-items-center");
            newBikeFormGrid.classList.add("md:grid-cols-3");
    } else {
        // HIDE: Animate out, then apply hidden after transition
        immutableFields.forEach(el => {
            el.classList.remove("opacity-100", "scale-100");
            el.classList.add("opacity-0", "scale-95");

            setTimeout(() => {
                el.classList.add("hidden");
            }, 500); // Match your CSS transition duration
        });



        setTimeout(() => {
            newBikeFormGrid.classList.add("justify-items-center");
            newBikeFormGrid.classList.remove("md:grid-cols-3");
        },500)
    }
});

form.addEventListener("submit", async function (e) {
    e.preventDefault();

    const formData = new FormData(form);
    const jsonData = {};
    formData.forEach((value, key) => {
        jsonData[key] = value;
    });

    jsonData["bikeOwnerId"] = document.getElementById("bikeOwnerId-form").value;

    // Override fields from selected bike model if not "0"
    const selectedModelId = existingBikeModel.value;
    if (selectedModelId != "0") {
        const selectedModel = bikeModels.find(model => model.id.toString() === selectedModelId);
        if (selectedModel) {
            jsonData["type"] = selectedModel.type;
            jsonData["brand"] = selectedModel.brand;
            jsonData["gearType"] = selectedModel.gearType;
            jsonData["engineType"] = selectedModel.engineType;
            jsonData["powertrain"] = selectedModel.powertrain;
            jsonData["maxSupport"] = selectedModel.maxSupport;
            jsonData["enginePowerMax"] = selectedModel.enginePowerMax;
            jsonData["enginePowerNominal"] = selectedModel.enginePowerNominal;
            jsonData["engineTorque"] = selectedModel.engineTorque;
            jsonData["bikeModelId"] = selectedModelId;
        }
    }

    console.log(jsonData);

    const response = await fetch("/api/bikes", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(jsonData)
    });

    if (response.ok) {
        console.log("Bike successfully saved");
        const bikeModalCloseButton = document.getElementById("closeNewBikeModal");
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
        if (element.id != "emailCustomer") {
            if (element.id != "bikeOwnerId-form") {
                element.value = '';  // Clear the value of the element
            }
        }
    });
}

function webSocketCheck(data) {
    const socket = new WebSocket(`${wsBaseUrl}/ws/status`);
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
    const socket = new WebSocket(`${wsBaseUrl}/ws/result`);
    socket.onopen = function () {
        socket.send(id);
    };

    socket.onmessage = function (event) {
        console.log("Report saved");
        socket.close();
        const reportId = event.data;
        console.log("Report ID received: " + reportId);
        const resultSection = document.getElementById("result-section");
        const loadingSection = document.getElementById("loading");
        const testIdElement = document.getElementById("testId");
        const reportLinkElement = document.getElementById("report-link");
        reportLinkElement.href = "/report/" + reportId;
        testIdElement.value = reportId;
        qrcodeGenerator();
        loadingSection.classList.add("hidden");
        resultSection.classList.remove("hidden");
    };
}


export  {
    webSocketCheck
}


