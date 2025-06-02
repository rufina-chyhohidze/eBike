document.getElementById("customer-filter").addEventListener("input", async function (e) {
    const name = e.target.value.toLowerCase();
    const res = await fetch(`/api/customers?name=${encodeURIComponent(name)}`);
    const tableContent = document.getElementById("customer-table-content");

    if (res.status === 200) {
        tableContent.innerHTML = ``;
        const customers = await res.json();

        if (customers.length === 0) {
            tableContent.innerHTML = `
                <tr>
                    <td colspan="4" class="text-center text-gray-500 py-4">
                        No customers found matching your search.
                    </td>
                </tr>
            `;
            return;
        }

        customers.forEach(customer => {
            tableContent.innerHTML += `
                <tr class="bg-white border-b hover:bg-gray-100">
                    <td class="p-3">
                        <a href="/technician/bike-management/${customer.id}" class="block w-full h-full">${customer.id}</a>
                    </td>
                    <td class="p-3">
                        <a href="/technician/bike-management/${customer.id}" class="block w-full h-full">${customer.name}</a>
                    </td>
                    <td class="p-3">
                        <a href="/technician/bike-management/${customer.id}" class="block w-full h-full">${customer.email}</a>
                    </td>
                    <td class="p-3">
                        <a href="/technician/bike-management/${customer.id}" class="block w-full h-full">${customer.phoneNumber}</a>
                    </td>
                </tr>
            `;
        });
    } else {
        tableContent.innerHTML = `
            <tr>
                <td colspan="4" class="text-center text-gray-500 py-4">
                    Error fetching customers.
                </td>
            </tr>
        `;
    }
});
