const searchInput = document.getElementById("bikeSearchInput");
const tableBody = document.getElementById("bikeTableBody");

async function fetchBikes(searchTerm = "") {
    try {
        const response = await fetch(`/api/users/bikes?search=${encodeURIComponent(searchTerm)}`, {
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        });

        if (response.status === 204) {
            renderTable([]);
            return;
        }

        if (!response.ok) {
            throw new Error(`Failed to fetch bikes: ${response.status}`);
        }

        const bikes = await response.json();
        renderTable(bikes);
    } catch (error) {
        console.error("Error:", error);
        tableBody.innerHTML = `
      <tr><td colspan="4" class="text-center text-red-500 p-4">Error loading bikes</td></tr>
    `;
    }
}

function renderTable(bikes) {
    if (!bikes || bikes.length === 0) {
        tableBody.innerHTML = `
      <tr><td colspan="4" class="text-center text-gray-500 p-4">No matching bikes found.</td></tr>
    `;
        return;
    }

    tableBody.innerHTML = bikes.map(bike => `
    <tr class="border-b">
      <td class="p-3">${bike.frameNumber}</td>
      <td class="p-3">${bike.type}</td>
      <td class="p-3">${bike.brand}</td>
      <td class="p-3">${bike.customerName}</td>
    </tr>
  `).join('');
}

let debounceTimeout;
searchInput.addEventListener("input", () => {
    clearTimeout(debounceTimeout);
    debounceTimeout = setTimeout(() => {
        fetchBikes(searchInput.value.trim());
    }, 300);
});

fetchBikes();
