const customerRadio = document.getElementById('customer');
const workshopAdminRadio = document.getElementById('workshopAdmin');
const technicianRadio = document.getElementById('technician');
const registrationFormInputs = document.getElementById('registration-form-inputs');
let workshopsInput;
let phoneNumberInput;

customerRadio.addEventListener('click', displayPhoneNumberInput);
function displayPhoneNumberInput() {
    console.log("displaying phone number");
    console.log(phoneNumberInput);
    console.log(phoneNumberInput.classList);
    console.log(phoneNumberInput?.style.display); // Use optional chaining to avoid errors

    if (!phoneNumberInput) {
        console.log("phoneNumberInput is not yet set!");
        return;
    }

    phoneNumberInput.classList.remove('d-none'); // Use classList instead of style.display
    console.log(phoneNumberInput.classList);
    workshopsInput.classList.add('d-none');

}


addPhoneNumberInput();
function addPhoneNumberInput() {
    console.log("Show Phone Number Input");
    registrationFormInputs.innerHTML += `
        <div id="phoneNumberField" class="">
            <i class="fas fa-envelope fa-lg me-3 fa-fw"></i>
            <div class="">
                <input type="tel" id="phoneNumber" name="phoneNumber" class="form-control" required/>
                <label class="form-label" for="phoneNumber">Your Phone Number</label>
            </div>
        </div>
    `;
    phoneNumberInput = document.getElementById('phoneNumberField');
    phoneNumberInput.classList.add('d-none');
}

workshopAdminRadio.addEventListener('click', displayWorkshopInput);
technicianRadio.addEventListener('click', displayWorkshopInput);
function displayWorkshopInput() {
    if (phoneNumberInput !== undefined) phoneNumberInput.classList.add('d-none');
    workshopsInput.classList.remove('d-none');
}

void showWorkShopsInput();
async function showWorkShopsInput() {
    console.log("Getting workshops");
      const response = await fetch("/api/workshops",
        {
            method: "GET",
            headers : {
                "Accept": "application/json"
            }
        }
      );

      if (response.status === 200) {
          console.log("Successfully retrieved workshops");

          /**
           * @type {[{workshopId:number, workshopName:string, location:string | null}]}
           */
          const workShops = await response.json();
          let workShopsHtml = '';
          for (let workShop of workShops) {
              workShopsHtml += `<option value="${workShop.workshopId}">${workShop.workshopName}</option>`;
          }
          registrationFormInputs.innerHTML += `
                <div id="workshops-select-field" class="d-flex flex-row align-items-center mb-3">
                    <i class="fas fa-envelope fa-lg me-3 fa-fw"></i>
                    <select id="workshop-select" name="workshop-select" class="form-outline flex-fill mb-0">
                        <option selected disabled>Select...</option>
                        ${workShopsHtml}
                    </select>
                </div>
          `;
        workshopsInput = document.getElementById('workshops-select-field');
        workshopsInput.classList.add('d-none');

      } else {
          console.log("Error while fetching workshops");
      }
}