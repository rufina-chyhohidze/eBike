import { csrfHeader, csrfToken } from "./utils/csrf.js";

const search = async (name) => {
    const res = await fetch(`/api/users?name=${name}`);
    const tableContent = document.getElementById("user-table-content");

    tableContent.innerHTML = ``;

    if (res.status === 200) {
        const customers = await res.json();

        customers.forEach(customer => {
            tableContent.innerHTML += `
                <tr class="bg-white border-b hover:bg-gray-100">
                    <td class="p-3">
                        <a href="/superadmin/bike-management/${customer.id}" class="block w-full h-full text-gray-800 hover:underline">
                            ${customer.id}
                        </a>
                    </td>
                    <td class="p-3">
                        <a href="/superadmin/bike-management/${customer.id}" class="block w-full h-full text-gray-800 hover:underline">
                            ${customer.name}
                        </a>
                    </td>
                    <td class="p-3">
                        <a href="/superadmin/bike-management/${customer.id}" class="block w-full h-full text-gray-800 hover:underline">
                            ${customer.email}
                        </a>
                    </td>
                    <td class="p-3">
                        <a href="/superadmin/bike-management/${customer.id}" class="block w-full h-full text-gray-800 hover:underline">
                            ${customer.role}
                        </a>
                    </td>
                    <td class="p-3 flex gap-2">
                       
          
<button id="delete-${customer.id}" class="flex items-center gap-2 px-4 py-2 bg-gradient-to-r from-purple-500 to-purple-600 text-white rounded-full shadow-md hover:from-purple-600 hover:to-purple-700 transition-all duration-200 text-sm">
      <i class="ph ph-user-minus"></i>
      Remove User
    </button>
                    </td>
                </tr>
            `;
        });

        // Add event listeners for delete buttons
        let selectedCustomerId = null;
        let selectedCustomerName = '';
        const modal = document.getElementById("delete-modal");
        const confirmBtn = document.getElementById("confirm-delete");
        const cancelBtn = document.getElementById("cancel-delete");
        const message = document.getElementById("delete-message");

        customers.forEach(customer => {
            const btn = document.getElementById(`delete-${customer.id}`);
            if (btn) {
                btn.addEventListener("click", () => {
                    selectedCustomerId = customer.id;
                    selectedCustomerName = customer.name;
                    message.textContent = `Are you sure you want to delete ${selectedCustomerName}?`;
                    modal.classList.remove("hidden");
                });
            }
        });

        cancelBtn.addEventListener("click", () => {
            modal.classList.add("hidden");
            selectedCustomerId = null;
        });

        confirmBtn.addEventListener("click", async () => {
            if (!selectedCustomerId) return;

            const delRes = await fetch(`/api/users/${selectedCustomerId}`, {
                method: 'DELETE',
                headers: {
                    [csrfHeader]: csrfToken
                }
            });

            modal.classList.add("hidden");

            if (delRes.ok) {
                await search(document.getElementById("user-filter").value.toLowerCase());
            } else {
                alert("Failed to delete user."); // Optional: Replace with a styled toast/snackbar if desired
            }

            selectedCustomerId = null;
        });

    }
};

await search("");

document.getElementById("user-filter").addEventListener("input", async function (e) {
    const name = e.target.value.toLowerCase();
    await search(name);
});
