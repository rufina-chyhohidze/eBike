let roleSelected;

const customerRadio = document.getElementById('customer');
const workshopAdminRadio = document.getElementById('workshopAdmin');
const technicianRadio = document.getElementById('technician');

const registerButton = document.getElementById('register-button');



function getWorkshopInput() {
    return document.getElementById('workshop-select');
}

customerRadio.addEventListener('click', () => setCustomerRadioSelected("CUSTOMER"));
workshopAdminRadio.addEventListener('click', () => setCustomerRadioSelected("WORKSHOP_ADMIN"));
technicianRadio.addEventListener('click', () => setCustomerRadioSelected("TECHNICIAN"));

function setCustomerRadioSelected(role) {
    roleSelected = role;
}

registerButton.addEventListener('click', registerCustomer);

async function registerCustomer() {
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');
    const passwordConfirmInput = document.getElementById('confirmPassword');
    const phoneNumberInput = document.getElementById('phoneNumber');

    const name = nameInput.value;
    const email = emailInput.value;
    const password = passwordInput.value;
    const passwordConfirm = passwordConfirmInput.value;
    const phoneNumber = phoneNumberInput === undefined || null ? null : phoneNumberInput.value;
    const workshop = getWorkshopInput() === undefined || null ? null : getWorkshopInput().value;

    if (password !== passwordConfirm) {
        alert('Passwords don\'t match');
        return;
    }
    console.log("Creating POST request for registration with values: \n" +
        `name: ${name}` + "\n" +
        "email: " + email + "\n" +
        "password: " + password + "\n" +
        "passwordConfirm: " + passwordConfirm + "\n" +
        "phoneNumber: " + phoneNumber + "\n" +
        "workshop: " + workshop + "\n");

    const response = await fetch('/api/register', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            role: roleSelected,
            name: name,
            email: email,
            password: password,
            phoneNumber: null,
            workshopId: workshop === undefined || null ? null : workshop,
        })
    });
    if (response.status === 200) {
        window.location.href = "http://localhost:8080/login";
    } else {
        alert("Error registering.")
        clearForm();
    }
}
function clearForm() {
    document.querySelectorAll("input, select").forEach(element => {
        if (element.type === "checkbox" || element.type === "radio") {
            element.checked = false;
        } else {
            element.value = "";
        }
    });
}

