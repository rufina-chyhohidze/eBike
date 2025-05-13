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
const closeNewBikeModal = document.getElementById("closeNewBikeModal");
const testBenchNumberInput = document.getElementById("testBenchNumber");
const newBikeForm = document.getElementById("newBikeForm");
const loadingDiv = document.getElementById("loading");
const mainStartTestPage = document.getElementById("test-start-page-main");
const customerNotFoundSection = document.getElementById("customer-not-found");
const visualCheckCheckbox = document.getElementById("checkbox-visual-check");
const functionalCheckCheckbox = document.getElementById("checkbox-functional-check");


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
newBikeForm.addEventListener("submit", async function (event) {
    event.preventDefault();

})
closeNewBikeModal.addEventListener("click", function () {
    document.getElementById("newBikeModal").classList.add("hidden");
});

document.getElementById("add-new-bike-btn").addEventListener("click", function () {
    document.getElementById("newBikeModal").classList.remove("hidden");
});

document.getElementById('emailCustomer').addEventListener('keypress', function (event) {
    if (event.key === 'Enter' && !customerSearchArea.classList.contains("search-customer-title-area-up")) {
        event.preventDefault(); // Prevent default form submission if inside a form
        document.getElementById('searchBtn').click();
    }
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
        <div class="p-5 border border-gray-200 rounded-2xl shadow hover:bg-purple-50 transition cursor-pointer m-3">
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
    const inspection = {};
    const functionalTest = {};
    document.querySelectorAll(".inspection-component").forEach(select => {
        const componentType = select.getAttribute("data-component-type");
        const componentName = select.getAttribute("data-component-name");
        const value = select.value;

        if (!componentName || !value) return;

        // For visual components (visual inspection)
        if (componentType === "visual" && visualCheckCheckbox.checked) {
            inspection[componentName] = value;  // Use the exact component name without toLowerCase()
        }
        // For functional components (functional test)
        else if (componentType === "functional" && functionalCheckCheckbox.checked) {
            functionalTest[componentName] = value;  // Same, use the exact component name
        }
    });


    const response = await fetch("/api/reports",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({
                "emailBikeOwner": customerFound.email,
                "testBenchNumber": testBenchNumberInput.value,
                "testType": testTypeInput.value,
                "frameNumber": bikeFrameOfBikeSelected,
                visualInspection: inspection,
                functionalTest: functionalTest,
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

export {customerFound, showCustomerBikes}