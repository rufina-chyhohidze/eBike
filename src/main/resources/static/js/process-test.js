import { customerFound, showCustomerBikes } from "./startmine.js";
import { csrfToken, csrfHeader } from './utils/csrf.js'
import qrcodeGenerator from "./qr-code-generation.js";

const DOMAIN_NAME = window.location.hostname + (window.location.port ? `:${window.location.port}` : '');




const form = document.querySelector("form");
const loadingDiv = document.getElementById("loading");
const testFormDiv = document.getElementById("test-form");

const existingBikeModel = document.getElementById("existing-bike-model");
// const brandEl = document.getElementById("brand")
// const maxSupportEl = document.getElementById("maxSupport")
// const sizeEl = document.getElementById("bikeSize")
// const typeEl = document.getElementById("type")
// const powertrainEl = document.getElementById("powertrain")
// const enginePowerMaxEl = document.getElementById("enginePowerMax")
// const engineTorqueEl = document.getElementById("engineTorque")
// const enginePowerNominalEl = document.getElementById("enginePowerNominal")
// const gearTypeEl = document.getElementById("gearType")
// const engineTypeEl = document.getElementById("engineType")
const bikeModelsResponse = await fetch("/api/bike-models")
let bikeModels = {}
if (bikeModelsResponse.ok) {
    bikeModels = await bikeModelsResponse.json();
    bikeModels.map(model => existingBikeModel.innerHTML += `
    <option id="${model.id}" value="${model.id}">${model.brand} - ${model.type}</option>
    `)
}

const immutableFields = Array.from(document.getElementsByClassName("immutable"));
existingBikeModel.addEventListener("change", function (e) {

    if (existingBikeModel.value == 0) {
        immutableFields.map(el => el.classList.remove("d-none"))
    } else {
        immutableFields.map(el => el.classList.add("d-none"))
    }
})

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
            jsonData["bikeSize"] = selectedModel.bikeSize;
            jsonData["gearType"] = selectedModel.gearType;
            jsonData["engineType"] = selectedModel.engineType;
            jsonData["powertrain"] = selectedModel.powertrain;
            jsonData["maxSupport"] = selectedModel.maxSupport;
            jsonData["enginePowerMax"] = selectedModel.enginePowerMax;
            jsonData["enginePowerNominal"] = selectedModel.enginePowerNominal;
            jsonData["engineTorque"] = selectedModel.engineTorque;
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
        const resultSection = document.getElementById("result-section")
        const loadingSection = document.getElementById("loading")
        const testIdElement = document.getElementById("testId")
        const reportLinkElement = document.getElementById("report-link");
        const customerEmailBtnElement = document.getElementById("customerEmailBtn");
        customerEmailBtnElement.id = reportId;
        reportLinkElement.href= "/report/"+reportId;
        testIdElement.value = reportId
        qrcodeGenerator();
        loadingSection.classList.add("hidden");
        resultSection.classList.remove("hidden");
    }


}

export  {
    webSocketCheck
}


