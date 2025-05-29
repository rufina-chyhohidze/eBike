document.getElementById("user-filter").addEventListener("input",async function (e) {
    const name = e.target.value.toLowerCase();
    const res = await fetch(`/api/users?name=${name}`);
    const tableContent = document.getElementById("user-table-content");

    if (res.status === 200) {
        tableContent.innerHTML = ``
        const customers = await res.json();
        customers.forEach(customer => {
            tableContent.innerHTML += `
            <tr class="bg-white border-b">
                <td class="p-3">${customer.id}</td>
                <td class="p-3">${customer.name}</td>
                <td class="p-3">${customer.email}</td>
                <td class="p-3">${customer.role}</td>
                <td class="p-3 flex gap-2">
                  <button class="bg-purple-600 text-white px-3 py-1 rounded-lg hover:bg-purple-700 text-sm">Edit</button>
                  <button class="bg-red-600 text-white px-3 py-1 rounded-lg hover:bg-red-700 text-sm">Delete</button>
                  <button class="bg-blue-600 text-white px-3 py-1 rounded-lg hover:bg-green-700 text-sm">Reports</button>
                </td>
            </tr>
            `
        })
    } else {
        tableContent.innerHTML = ``
    }
})