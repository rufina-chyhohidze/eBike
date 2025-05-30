import {csrfHeader, csrfToken} from "./utils/csrf.js";

const search = async (name) => {
    const res = await fetch(`/api/users?name=${name}`);
    const tableContent = document.getElementById("user-table-content");

    tableContent.innerHTML = ``;

    if (res.status === 200) {
        const customers = await res.json();

        customers.forEach(customer => {
            tableContent.innerHTML += `
                <tr class="bg-white border-b">
                    <td class="p-3">${customer.id}</td>
                    <td class="p-3">${customer.name}</td>
                    <td class="p-3">${customer.email}</td>
                    <td class="p-3">${customer.role}</td>
                    <td class="p-3 flex gap-2">
                ${
                    customer.role === "CUSTOMER"
                    ? `<a href="/superadmin/reports?customerId=${customer.id}" class="bg-purple-600 text-white px-3 py-1 rounded-lg hover:bg-purple-700 text-sm">Reports</a>`
                    : ``
                }
                <button id="delete-${customer.id}" class="bg-red-600 text-white px-3 py-1 rounded-lg hover:bg-red-700 text-sm">Delete</button>
            </td>
                </tr>
            `;
        });

        customers.forEach(customer => {
            const btn = document.getElementById(`delete-${customer.id}`);
            if (btn) {
                btn.addEventListener("click", async () => {
                    const confirmed = confirm(`Are you sure you want to delete ${customer.name}?`);
                    if (!confirmed) return;

                    const delRes = await fetch(`/api/users/${customer.id}`, {
                        method: 'DELETE',
                        headers: {
                            [csrfHeader]: csrfToken
                        }
                    });
                    if (delRes.ok) {
                        alert("User deleted.");
                        await search(name); // Refresh table
                    } else {
                        alert("Failed to delete user.");
                    }
                });
            }
        });
    }
};

await search("");

document.getElementById("user-filter").addEventListener("input", async function (e) {
    const name = e.target.value.toLowerCase();
    await search(name);
});
