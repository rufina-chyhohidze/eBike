const search = async (name) => {
    const res = await fetch(`/api/users?name=${name}`);
    const tableContent = document.getElementById("user-table-content");

    if (res.status === 200) {
        tableContent.innerHTML = ``;
        const customers = await res.json();
        customers.forEach(customer => {
            if (customer.role === "TECHNICIAN") {
                tableContent.innerHTML += `
<tr class="bg-white border-b hover:bg-gray-100">
  <td class="p-3">
    <p  class="block w-full h-full text-gray-800">
      ${customer.id}
    </p>
  </td>
  <td class="p-3">
    <p  class="block w-full h-full text-gray-800">
      ${customer.name}
    </p>
  </td>
  <td class="p-3">
    <p class="block w-full h-full text-gray-800">
      ${customer.email}
    </p>
  </td>
</tr>
            `;
            } else {
                tableContent.innerHTML += `
<tr class="bg-white border-b hover:bg-gray-100">
  <td class="p-3">
    <a href="/workshopadmin/bike-management/${customer.id}" class="block w-full h-full text-gray-800 hover:underline">
      ${customer.id}
    </a>
  </td>
  <td class="p-3">
    <a href="/workshopadmin/bike-management/${customer.id}" class="block w-full h-full text-gray-800 hover:underline">
      ${customer.name}
    </a>
  </td>
  <td class="p-3">
    <a href="/workshopadmin/bike-management/${customer.id}" class="block w-full h-full text-gray-800 hover:underline">
      ${customer.email}
    </a>
  </td>
</tr>
            `;
            }

        });
    } else {
        tableContent.innerHTML = ``;
    }
}

search("");

document.getElementById("user-filter").addEventListener("input", async function (e) {
    const name = e.target.value.toLowerCase();
    await search(name);
});
