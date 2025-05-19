document.getElementById("customer-filter").addEventListener("input",async function (e) {
    const name = e.target.value.toLowerCase();
    const res = await fetch(`/api/customers?name=${name}`);
    const tableContent = document.getElementById("customer-table-content");

    if (res.status === 200) {
        tableContent.innerHTML = ``
        const customers = await res.json();
        customers.forEach(customer => {
            tableContent.innerHTML += `
            <tr class="bg-white border-b">
                <td class="p-3">${customer.id}</td>
                <td class="p-3">${customer.name}</td>
                <td class="p-3">${customer.email}</td>
                <td class="p-3">${customer.phoneNumber}</td>
            </tr>
            `
        })
    } else {
        tableContent.innerHTML = ``
    }
})