const emailInput = document.getElementById("emailBikeOwner");
const searchButton = document.getElementById("searchCustomer");
const customerFoundSection = document.getElementById("customer-found-test");
const emailMessage = document.getElementById("customer-email-message");
const customerNotFoundSection = document.getElementById("customer-not-found");
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
        showCustomer(await response.json());
        customerNotFoundSection.style.display = "none";
    } else if (response.status === 204) {
        customerFoundSection.innerHTML = "";
        customerNotFoundSection.style.display = "block";
        emailMessage.innerHTML = `${emailInput.value}`;
        console.log("Customer NOT FOUND");
    } else {
        customerFoundSection.innerHTML = "";
        customerNotFoundSection.style.display = "none";
        alert("Error accessing backend")
    }
}

function showCustomer(customer) {
    customerFoundSection.innerHTML = '';
    /**
     * @type { name:string, email:string, phoneNumber:string }
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
        customerFoundItem.addEventListener('click', event => handleCustomerClick(event, customer.email));
}

function handleCustomerClick(event, customerEmail) {
    console.log("Clicked on customer");
    console.log(customerEmail)
}