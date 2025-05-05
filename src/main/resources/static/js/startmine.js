import {webSocketCheck} from "./process-test.js";
import {csrfHeader, csrfToken} from "./utils/csrf.js";

const emailInput = document.getElementById("emailCustomer");
const searchButton = document.getElementById("searchBtn");
const customerFoundSection = document.getElementById("customer-found-test");
const emailMessage = document.getElementById("customer-email-message");
const bikeSection = document.getElementById("bike-section");
const customerSearchArea = document.getElementById("emailSection");
const bikeItemsSection = document.getElementById("bike-items-section");
const noRegisteredBikes = document.getElementById("bikes-not-found");
const startTestButton = document.getElementById("startTestBtn");
const closeStartTestModalButton = document.getElementById("close-start-test-button");
const testTypeInput = document.getElementById("testType");
const testBenchNumberInput = document.getElementById("testBenchNumber");
const loadingDiv = document.getElementById("loading");
const mainStartTestPage = document.getElementById("test-start-page-main");
const customerNotFoundSection = document.getElementById("customer-not-found");
bikeSection.classList.add("bike-section-hide");
noRegisteredBikes.style.display = "none";
customerNotFoundSection.style.display = "none";
// loadingDiv.style.display = "none";
mainStartTestPage.style.display = "flex";

let customerFound;
let bikeFrameOfBikeSelected;
let customerFoundItem;

setButtonSearch();
emailInput.addEventListener('input', setButtonSearch);
searchButton.addEventListener('click', fetchCustomer);
startTestButton.addEventListener('click', startTest);
closeStartTestModalButton.addEventListener("click", closeTestModal);

document.getElementById("add-new-bike-btn").addEventListener("click", function () {
    document.getElementById("newBikeModal").classList.remove("hidden");
});



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
    console.log("Displaying Bikes:");
    console.log(bikes);

    // Show the whole section
    const bikeList = document.getElementById("bikeList");
    bikeList.classList.remove("hidden");

    // Clear previously inserted bikes
    bikeItemsSection.innerHTML = "";

    for (let bike of bikes) {
        const bikeCard = `
        <div class="p-5 border border-gray-200 rounded-2xl shadow hover:bg-purple-50 transition cursor-pointer">
            <h3 class="text-xl font-bold text-gray-700">Bike Model: ${bike.brand} ${bike.type}</h3>
            <p class="text-sm text-gray-500">Frame No: ${bike.frameNumber}</p>
            <button id="${bike.frameNumber}" type="button" data-bs-target="#start-test-modal" data-bs-toggle="modal"
                class="mt-3 btn btn-outline-primary btn-custom select-bike-button">Select</button>
        </div>
        `;
        bikeItemsSection.innerHTML += bikeCard;
    }

    setBikeSelectedButtons(); // make buttons clickable
}

// document.querySelector("#newBikeModal form").addEventListener("submit", async function(event) {
//     event.preventDefault();  // Prevent form from submitting the traditional way
//
//     const formData = new FormData(event.target);
//     const newBikeData = {
//         bikeOwnerId: formData.get('bikeOwnerId'),
//         frameNumber: formData.get('frameNumber'),
//         brand: formData.get('brand'),
//         productionDate: formData.get('productionDate'),
//         maxSupport: formData.get('maxSupport'),
//         bikeSize: formData.get('bikeSize'),
//         type: formData.get('type'),
//         powertrain: formData.get('powertrain'),
//         enginePowerMax: formData.get('enginePowerMax'),
//         engineTorque: formData.get('engineTorque'),
//         milleage: formData.get('milleage'),
//         accCapacity: formData.get('accCapacity'),
//         enginePowerNominal: formData.get('enginePowerNominal'),
//         gearType: formData.get('gearType'),
//         engineType: formData.get('engineType')
//     };
//
//     // Send the data to the server to create the bike
//     const response = await fetch(`/api/customers/${customerFound.id}/bikes`, {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/json",
//             [csrfHeader]: csrfToken  // Ensure CSRF token is passed
//         },
//         body: JSON.stringify(newBikeData)
//     });
//
//     if (response.status === 200) {
//         // Successfully created the bike, close the modal
//         closeTestModal();
//
//         // Re-fetch the bikes for this customer and update the list
//         await showCustomerBikes(customerFound.id);
//     } else {
//         console.error("Failed to create bike: ", response.status);
//         alert("There was an issue creating the bike. Please try again.");
//     }
// });

// function displayBikes(bikes) {
//     console.log("Displaying Bikes:")
//     console.log(bikes);
//
//     bikeItemsSection.innerHTML = "";
//
//     /**
//      * @typedef {Object} BikeDTO
//      * @property {number} bikeOwnerId
//      * @property {string} frameNumber
//      * @property {string} type
//      * @property {string} brand
//      * @property {string} registrationDate - Format: YYYY-MM-DD
//      * @property {string} productionDate - Format: YYYY-MM-DD
//      * @property {string} bikeSize
//      * @property {number} milleage
//      * @property {string} gearType
//      * @property {string} engineType
//      * @property {string} powertrain
//      * @property {number} accCapacity
//      * @property {number} maxSupport
//      * @property {number} enginePowerMax
//      * @property {number} enginePowerNominal
//      * @property {number} engineTorque
//      */
//
//     for (let bike of bikes) {
//         bikeItemsSection.innerHTML += `
//         <!-- Bike Card -->
//            <div class="col bike-item">
//               <div class="card h-100 bike-card">
//                 <div class="text-center">
//                     <img class="bike-img" src="/img/ebike.png" alt="ebike">
//                 </div>
//                 <div class="card-body pricing-features">
//                     <ol class="list-unstyled">
//                         <li class="mb-3">
//                             <strong>Frame Nr: </strong> <span>${bike.frameNumber}</span>
//                         </li>
//                     </ol>
//                     <div class="text-center mt-4">
//                       <button id="${bike.frameNumber}" type="button" data-bs-target="#start-test-modal" data-bs-toggle="modal" class="btn btn-outline-primary btn-custom select-bike-button">Select</button>
//                       </div>
//                   </div>
//               </div>
//               <div class="card bike-info">
//                     <h5 class="bike-detail-title">BIKE DETAILS</h5>
//                     <table>
//                       <thead>
//                         <th></th>
//                         <th></th>
//                         <th></th>
//                         <th></th>
//                       </thead>
//                       <tbody>
//                         <tr>
//                           <td><strong>Brand:</strong></td>
//                           <td>${bike.brand}</td>
//                           <td><strong>Type:</strong></td>
//                           <td>${bike.type}</td>
//                         </tr>
//                         <tr>
//                           <td><strong>Engine Torque:</strong></td>
//                           <td>${bike.engineTorque}</td>
//                           <td><strong>Registration Date:</strong></td>
//                           <td>${bike.registrationDate}</td>
//                         </tr>
//                         <tr>
//                           <td><strong>Size:</strong></td>
//                           <td>${bike.bikeSize}</td>
//                           <td><strong>Production Date:</strong></td>
//                           <td>${bike.productionDate}</td>
//                         </tr>
//                         <tr>
//                           <td><strong>Mileage:</strong></td>
//                           <td>${bike.milleage}</td>
//                           <td><strong>Gear Type:</strong></td>
//                           <td>${bike.gearType}</td>
//                         </tr>
//                         <tr>
//                           <td><strong>Engine Type:</strong></td>
//                           <td>${bike.engineType}</td>
//                           <td><strong>Power Train:</strong></td>
//                           <td>${bike.powertrain}</td>
//                         </tr>
//                         <tr>
//                           <td><strong>Acc. Capacity:</strong></td>
//                           <td>${bike.accCapacity}</td>
//                           <td><strong>Max. Support:</strong></td>
//                           <td>${bike.maxSupport}</td>
//                         </tr>
//                         <tr>
//                           <td><strong>Max. Engine Power:</strong></td>
//                           <td>${bike.enginePowerMax}</td>
//                           <td><strong>Nominal Engine Power:</strong></td>
//                           <td>${bike.enginePowerMax}</td>
//                         </tr>
//                       </tbody>
//                     </table>
//               </div>
//            </div>
//         <!-- END - Bike Card -->
//         `;
//     }
//
//     setBikeSelectedButtons();
// }

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

    const testModal = document.getElementById("testModal");
    testModal.classList.remove("hidden");
}

async function startTest() {
    const response = await fetch("/api/reports",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                [csrfHeader]: csrfToken
            },
            body : JSON.stringify({
                "emailBikeOwner" : customerFound.email,
                "testBenchNumber" : testBenchNumberInput.value,
                "testType" : testTypeInput.value,
                "frameNumber" : bikeFrameOfBikeSelected,
            })
        }
    );
    if (response.status === 200) {
        const data = await response.json();
        /**
         * @type {[{id:string}]}
         */
        console.log("Id received: " + data.id);
        closeStartTestModalButton.click();
        mainStartTestPage.style.display = "none";
        loadingDiv.classList.remove("hidden");
        // loadingDiv.classList.remove("d-none");
        // loadingDiv.style.display = "flex";

        webSocketCheck(data);
    } else {
        console.log("Error: " + response.status);
    }
}

function closeTestModal() {
    const testModal = document.getElementById("testModal");
    testModal.classList.add("hidden");
}

export { customerFound, showCustomerBikes }