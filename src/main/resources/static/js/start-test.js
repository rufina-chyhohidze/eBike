const emailInput = document.getElementById("emailBikeOwner");
const searchButton = document.getElementById("searchCustomer");
const customerFoundSection = document.getElementById("customer-found-test");

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
    } else if (response.status === 204) {
        customerFoundSection.innerHTML = "";
        console.log("no customers found");
    } else {
        customerFoundSection.innerHTML = "";
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
        <div class="customer-found-item">
            <strong>Name:&nbsp;</strong> <span>${customer.name}</span>
            &nbsp;&nbsp;&nbsp; <br/>
            <strong>Email:&nbsp;</strong> <span>${customer.email}</span>
            &nbsp;&nbsp;&nbsp; <br/>
            <strong>Phone Number:&nbsp;</strong> <span>${customer.phoneNumber}</span>
        </div>`;
}