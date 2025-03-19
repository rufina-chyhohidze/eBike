const emailInput = document.getElementById("emailBikeOwner");
const searchButton = document.getElementById("searchCustomer");
const customerFoundSection = document.getElementById("customer-found-test");
const emailMessage = document.getElementById("customer-email-message");
const bikeSection = document.getElementById("bike-section");
const customerSearchArea = document.getElementById("search-customer-title-area");
const bikeItemsSection = document.getElementById("bike-items-section");
const noRegisteredBikes = document.getElementById("bikes-not-found");
const customerNotFoundSection = document.getElementById("customer-not-found");
noRegisteredBikes.style.display = "none";
customerNotFoundSection.style.display = "none";


let customerFoundItem;

searchButton.addEventListener('click', fetchCustomer);

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
        console.log(customerSearchArea)
        customerSearchArea.classList.add("search-customer-title-area-up");
        showCustomer(await response.json());
        customerNotFoundSection.style.display = "none";
    } else if (response.status === 204) {
        bikeSection.classList.remove("bike-section-show");
        customerSearchArea.classList.remove("search-customer-title-area-up");
        customerFoundSection.innerHTML = "";
        customerNotFoundSection.style.display = "block";
        emailMessage.innerHTML = `${emailInput.value}`;
        console.log("Customer NOT FOUND");
    } else {
        bikeSection.classList.remove("bike-section-show");
        customerSearchArea.classList.remove("search-customer-title-area-up");
        customerFoundSection.innerHTML = "";
        customerNotFoundSection.style.display = "none";
        alert("Error accessing backend")
    }
}

function showCustomer(customer) {
    customerFoundSection.innerHTML = '';
    /**
     * @type { id:number, name:string, email:string, phoneNumber:string }
     */
    console.log(customer);
    customerFoundSection.innerHTML = `
        <div class="customer-found-item" id="customer-found-item">
            <strong>Name:&nbsp;</strong> <span>${customer.name}</span>
            &nbsp;&nbsp;&nbsp; <br/>
            <strong>Email:&nbsp;</strong> <span>${customer.email}</span>
            &nbsp;&nbsp;&nbsp; <br/>
            <strong>Phone Number:&nbsp;</strong> <span>${customer.phoneNumber}</span>
        </div>`;

    customerFoundItem = document.getElementById("customer-found-item");

    showCustomerBikes(customer.id);
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
        console.log("No bikes found");
    } else {
        bikeSection.classList.remove("bike-section-show");
        noRegisteredBikes.style.display = "none";
        console.log("Error while retrieving bikes")
    }
}

function displayBikes(bikes) {
    console.log("displaying biks:")
    console.log(bikes);

    bikeItemsSection.innerHTML = "";

    /**
     * @typedef {Object} BikeDTO
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
                      <button type="button" class="btn btn-outline-primary btn-custom">Select</button>
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
}