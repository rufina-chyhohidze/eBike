const bikeGrid = document.getElementById("bikeGrid");
const searchInput = document.getElementById("bikeSearchInput");
const noBikesMessage = document.getElementById("noBikesMessage");


async function fetchBikes(query = "") {
    const match = window.location.pathname.match(/\/(\d+)(\/)?$/);
    const id = match ? match[1] : null;

    const res = await fetch(`/api/bikes?customerId=${id}&frameNumber=${encodeURIComponent(query)}`);
    if (!res.ok) return [];
    return await res.json();
}

function renderBikes(bikes) {
    bikeGrid.innerHTML = "";

    if (bikes.length === 0) {
        noBikesMessage.classList.remove("hidden");
        return;
    }

    noBikesMessage.classList.add("hidden");

    for (const bike of bikes) {
        const card = document.createElement("div");
        card.className =
            "bike-card bg-gray-50 rounded-2xl shadow-sm border border-gray-200 p-6 hover:shadow-md transition";

        card.innerHTML = `
        <h3 class="text-lg font-semibold text-purple-700 mb-2">${bike.brand}</h3>
        <p class="text-sm text-gray-600 mb-4">Frame No: ${bike.frameNumber}</p>

        <a href="/report-list/${bike.frameNumber}"
           class="mt-3 inline-flex items-center gap-2 bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-xl transition text-sm font-semibold shadow-md">
          <i class="ph ph-clipboard-text text-lg"></i> View Reports
        </a>

        <button class="unlink-bike-btn mt-3 inline-flex items-center gap-2 bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-xl transition text-sm font-semibold shadow-md"
                data-frame="${bike.frameNumber}">
          <i class="ph ph-trash text-lg"></i> Delete Bike
        </button>
      `;

        bikeGrid.appendChild(card);
    }
}

searchInput.addEventListener("input", async (e) => {
    const query = e.target.value.trim();
    const bikes = await fetchBikes(query);
    renderBikes(bikes);
});

if (!document.querySelector(".bike-card")) {
    fetchBikes().then(renderBikes);
}