let customers = [
    { name: "John Smith", email: "john@example.com", phone: "123-456-7890" },
    { name: "Jane Doe", email: "jane@example.com", phone: "098-765-4321" }
];

let technicians = [
    { name: "Mark Taylor", email: "mark@example.com", phone: "555-123-4567" }
];

function showUsers(type) {
    let list = type === "customers" ? customers : technicians;
    document.getElementById("userList").innerHTML = list.map(user =>
        `<tr class="border-b"><td class="p-3">${user.name}</td><td class="p-3">${user.email}</td><td class="p-3">${user.phone}</td></tr>`
    ).join('');
}

document.getElementById("showCustomers").onclick = () => showUsers("customers");
document.getElementById("showTechnicians").onclick = () => showUsers("technicians");

showUsers("customers");

// populating errors
let errors = [
    { testId: "T1234", bike: "Model X", code: "E101", description: "Battery Voltage Too Low", date: "2024-03-08" },
    { testId: "T1235", bike: "Model Y", code: "E205", description: "Motor Overheating", date: "2024-03-09" },
    { testId: "T1236", bike: "Model Z", code: "E310", description: "Brake Sensor Failure", date: "2024-03-10" }
];

function loadErrors(filter = "") {
    let filteredErrors = errors.filter(error => error.code.includes(filter));
    document.getElementById("errorList").innerHTML = filteredErrors.map(error =>
        `<tr class="border-b">
        <td class="p-3">${error.testId}</td>
        <td class="p-3">${error.bike}</td>
        <td class="p-3 text-red-500 font-semibold">${error.code}</td>
        <td class="p-3">${error.description}</td>
        <td class="p-3">${error.date}</td>
      </tr>`
    ).join('');
}

document.getElementById("errorSearch").addEventListener("input", (e) => loadErrors(e.target.value));
document.getElementById("clearSearch").addEventListener("click", () => {
    document.getElementById("errorSearch").value = "";
    loadErrors();
});

loadErrors();