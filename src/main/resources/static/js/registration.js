const customerRadio = document.getElementById('customer');
const workshopAdminRadio = document.getElementById('workshopAdmin');
const technicianRadio = document.getElementById('technician');
const registrationFormInputs = document.getElementById('registration-form-inputs');
let workshopsInput;
let phoneNumberInput;

const phoneInputDiv = document.getElementById("phoneNumberField")
const workshopSelectDiv = document.getElementById("workshops-select-field")

customerRadio.addEventListener('click', displayPhoneNumberInput);
workshopAdminRadio.addEventListener('click', displayWorkshopInput);
technicianRadio.addEventListener('click', displayWorkshopInput);


function displayPhoneNumberInput() {
    workshopSelectDiv.classList.add("d-none");
    phoneInputDiv.classList.remove("d-none");
}


function displayWorkshopInput() {
    phoneInputDiv.classList.add("d-none");
    workshopSelectDiv.classList.remove('d-none');
    void showWorkShopsInput()
}

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
           * @type {[{workshopId:number, workshopName:string, workshopsLocation:string | null}]}
           */
          const workShops = await response.json();
          let workShopsHtml = '';
          for (let workShop of workShops) {
              workShopsHtml += `<option value="${workShop.workshopId}">${workShop.workshopName}</option>`;
          }
          const workshopSelect = document.getElementById("workshop-select")
          workshopSelect.innerHTML += workShopsHtml;
      } else {
          console.log("Error while fetching workshops");
      }
}