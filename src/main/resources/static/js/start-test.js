import {webSocketCheck} from "./process-test.js";
import {csrfHeader, csrfToken} from "./utils/csrf.js";

const emailInput = document.getElementById("emailBikeOwner");
const searchButton = document.getElementById("searchCustomer");
const customerFoundSection = document.getElementById("customer-found-test");
const emailMessage = document.getElementById("customer-email-message");
const bikeSection = document.getElementById("bike-section");
const customerSearchArea = document.getElementById("search-customer-title-area");
const bikeItemsSection = document.getElementById("bike-items-section");
const noRegisteredBikes = document.getElementById("bikes-not-found");
const startTestButton = document.getElementById("start-test");
const closeStartTestModalButton = document.getElementById("close-start-test-button");
const testTypeInput = document.getElementById("testType");
const testBenchNumberInput = document.getElementById("testBenchNumber");
const loadingDiv = document.getElementById("loading");
const mainStartTestPage = document.getElementById("test-start-page-main");
const customerNotFoundSection = document.getElementById("customer-not-found");
bikeSection.classList.add("bike-section-hide");
noRegisteredBikes.style.display = "none";
customerNotFoundSection.style.display = "none";
loadingDiv.style.display = "none";
mainStartTestPage.style.display = "block";

let customerFound;
let bikeFrameOfBikeSelected;
let customerFoundItem;

setButtonSearch();
emailInput.addEventListener('input', setButtonSearch);
searchButton.addEventListener('click', fetchCustomer);
startTestButton.addEventListener('click', startTest);

function setButtonSearch() {
    if (emailInput.value === "") {
        searchButton.disabled = true;
        searchButton.style.opacity = "50%";
    } else {
        searchButton.disabled = false;
        searchButton.style.opacity = "100%";
    }
}
async function fetchCustomer() {
    const response = await fetch(`/api/customers?email=${emailInput.value}`,
        {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json",
            }
    });

    if (response.status === 200) {
        bikeSection.classList.remove("bike-section-hide");
        bikeSection.classList.add("bike-section-show");
        console.log(customerSearchArea)
        customerSearchArea.classList.add("search-customer-title-area-up");
        customerFound = await response.json();
        showCustomer();
        customerNotFoundSection.style.display = "none";
    } else if (response.status === 204) {
        bikeSection.classList.add("bike-section-hide");
        bikeSection.classList.remove("bike-section-show");
        customerSearchArea.classList.remove("search-customer-title-area-up");
        customerFoundSection.innerHTML = "";
        customerNotFoundSection.style.display = "block";
        emailMessage.innerHTML = `${emailInput.value}`;
        console.log("Customer NOT FOUND");
    } else {
        bikeSection.classList.add("bike-section-hide");
        bikeSection.classList.remove("bike-section-show");
        customerSearchArea.classList.remove("search-customer-title-area-up");
        customerFoundSection.innerHTML = "";
        customerNotFoundSection.style.display = "none";
        alert("Error accessing backend")
    }
}

function showCustomer() {
    customerFoundSection.innerHTML = '';
    /**
     * @type { id:number, name:string, email:string, phoneNumber:string }
     */
    console.log(customerFound);
    customerFoundSection.innerHTML = `
        <div class="customer-found-item" id="customer-found-item">
            <strong>Name:&nbsp;</strong> <span>${customerFound.name}</span>
            &nbsp;&nbsp;&nbsp; <br/>
            <strong>Email:&nbsp;</strong> <span>${customerFound.email}</span>
            &nbsp;&nbsp;&nbsp; <br/>
            <strong>Phone Number:&nbsp;</strong> <span>${customerFound.phoneNumber}</span>
        </div>`;

    const inputBikeOwnerID = document.getElementById("bikeOwnerId-form");
    inputBikeOwnerID.value = customerFound.id;
    customerFoundItem = document.getElementById("customer-found-item");

    void showCustomerBikes(customerFound.id);
}

async function showCustomerBikes(customerId) {
    const response = await fetch(`/api/customers/${customerId}/bikes`,
        {
            method: "GET",
            headers: {
                "Accept": "application/json",
            }
    });

    if (response.status === 200) {
        bikeSection.classList.add("bike-section-show");
        noRegisteredBikes.style.display = "none";

        displayBikes(await response.json());
    } else if (response.status === 204) {
        bikeSection.classList.remove("bike-section-show");
        noRegisteredBikes.style.display = "block";
        bikeItemsSection.innerHTML = "";
        console.log("No bikes found");
    } else {
        bikeSection.classList.remove("bike-section-show");
        noRegisteredBikes.style.display = "none";
        console.log("Error while retrieving bikes")
    }
}

function displayBikes(bikes) {
    console.log("Displaying Bikes:")
    console.log(bikes);

    bikeItemsSection.innerHTML = "";

    /**
     * @typedef {Object} BikeDTO
     * @property {number} bikeOwnerId
     * @property {string} frameNumber
     * @property {string} type
     * @property {string} brand
     * @property {string} registrationDate - Format: YYYY-MM-DD
     * @property {string} productionDate - Format: YYYY-MM-DD
     * @property {string} bikeSize
     * @property {number} milleage
     * @property {string} gearType
     * @property {string} engineType
     * @property {string} powertrain
     * @property {number} accCapacity
     * @property {number} maxSupport
     * @property {number} enginePowerMax
     * @property {number} enginePowerNominal
     * @property {number} engineTorque
     */

    for (let bike of bikes) {
        bikeItemsSection.innerHTML += `
        <!-- Bike Card -->
           <div class="col bike-item">
              <div class="card h-100 bike-card">
                <div class="text-center">
                    <img class="bike-img" src="/img/ebike.png" alt="ebike">
                </div>
                <div class="card-body pricing-features">
                    <ol class="list-unstyled">
                        <li class="mb-3">
                            <strong>Frame Nr: </strong> <span>${bike.frameNumber}</span>
                        </li>
                    </ol>
                    <div class="text-center mt-4">
                      <button id="${bike.frameNumber}" type="button" data-bs-target="#start-test-modal" data-bs-toggle="modal" class="btn btn-outline-primary btn-custom select-bike-button">Select</button>
                      </div>
                  </div>
              </div>
              <div class="card bike-info">
                    <h5 class="bike-detail-title">BIKE DETAILS</h5>
                    <table>
                      <thead>
                        <th></th>
                        <th></th>
                        <th></th>
                        <th></th>
                      </thead>
                      <tbody>
                        <tr>
                          <td><strong>Brand:</strong></td>
                          <td>${bike.brand}</td>
                          <td><strong>Type:</strong></td>
                          <td>${bike.type}</td>
                        </tr>
                        <tr>
                          <td><strong>Engine Torque:</strong></td>
                          <td>${bike.engineTorque}</td>
                          <td><strong>Registration Date:</strong></td>
                          <td>${bike.registrationDate}</td>
                        </tr>
                        <tr>
                          <td><strong>Size:</strong></td>
                          <td>${bike.bikeSize}</td>
                          <td><strong>Production Date:</strong></td>
                          <td>${bike.productionDate}</td>
                        </tr>
                        <tr>
                          <td><strong>Mileage:</strong></td>
                          <td>${bike.milleage}</td>
                          <td><strong>Gear Type:</strong></td>
                          <td>${bike.gearType}</td>
                        </tr>
                        <tr>
                          <td><strong>Engine Type:</strong></td>
                          <td>${bike.engineType}</td>
                          <td><strong>Power Train:</strong></td>
                          <td>${bike.powertrain}</td>
                        </tr>
                        <tr>
                          <td><strong>Acc. Capacity:</strong></td>
                          <td>${bike.accCapacity}</td>
                          <td><strong>Max. Support:</strong></td>
                          <td>${bike.maxSupport}</td>
                        </tr>
                        <tr>
                          <td><strong>Max. Engine Power:</strong></td>
                          <td>${bike.enginePowerMax}</td>
                          <td><strong>Nominal Engine Power:</strong></td>
                          <td>${bike.enginePowerMax}</td>
                        </tr>
                      </tbody>
                    </table>
              </div>
           </div>
        <!-- END - Bike Card -->
        `;
    }

    setBikeSelectedButtons();
}

function setBikeSelectedButtons() {
    const bikeSelectedButtons = [...document.getElementsByClassName("select-bike-button")];
    bikeSelectedButtons.forEach(button => {
        button.addEventListener("click", displayTestFormWithBikeSelected)
    })
}

function displayTestFormWithBikeSelected(e) {
    console.log("Bike Selected: " + e.target.id);
    bikeFrameOfBikeSelected = e.target.id;

    document.getElementById("bike-selected-frame-title").innerHTML = `Bike Frame Selected: ${e.target.id}`;
}

async function startTest() {
    const inspection = {};
    const functionalTest = {};

    document.querySelectorAll(".inspection-component").forEach(select => {
        const componentType = select.getAttribute("data-component-type");
        const componentName = select.getAttribute("data-component-name");
        const value = select.value;

        if (!componentName || !value) return;

        // For visual components (visual inspection)
        if (componentType === "visual") {
            inspection[componentName] = value;  // Use the exact component name without toLowerCase()
        }
        // For functional components (functional test)
        else if (componentType === "functional") {
            functionalTest[componentName] = value;  // Same, use the exact component name
        }
    });

    // Make the POST request to start the test
    const response = await fetch("/api/reports", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            [csrfHeader]: csrfToken,  // Ensure csrfHeader and csrfToken are defined elsewhere in your code
        },
        body: JSON.stringify({
            emailBikeOwner: customerFound.email,
            testBenchNumber: testBenchNumberInput.value,
            testType: testTypeInput.value,
            frameNumber: bikeFrameOfBikeSelected,
            visualInspection: inspection,  // Add the visual inspection data here
            functionalTest: functionalTest  // Add the functional test data here
        })
    });

    if (response.status === 200) {
        const data = await response.json();
        console.log("Id received: " + data.id);

        // Close the modal and show loading screen
        closeStartTestModalButton.click();
        mainStartTestPage.style.display = "none";
        loadingDiv.classList.remove("d-none");

        // Proceed with WebSocket or any other functionality you have after a successful submission
        webSocketCheck(data);
    } else {
        console.log("Error: " + response.status);
    }
}

export { customerFound, showCustomerBikes }