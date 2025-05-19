const compareButton = document.getElementById('compareButton');
const mainContent = document.getElementById('main-content');
const reportsTableContent = document.getElementById('reports-table-content');
const compareContent = document.getElementById('report-selection');
compareButton.addEventListener("click", async () => {
    mainContent.style.display = "none";
    compareContent.classList.remove("d-none");
    const res = await fetch("/api/reports");
    const reports = await res.json();
    reportsTableContent.innerHTML += ``
    reports.forEach(report => {
        reportsTableContent.innerHTML += `
            <tr class="border-b">
              <td class="p-3">${report.reportId}</td>
              <td class="p-3">${report.reportDate}</td>
              <td class="p-3">${report.customerId}</td>
              <td class="p-3">${report.frameNumber}</td>
              <td class="p-3">${report.engineType}</td>
              <td class="p-3">${report.benchId}</td>
              <td><button id=${report.reportId} class="selectBtn bg-purple-500 text-white px-4 py-2 rounded-lg hover:bg-purple-600 w-full">Select</button></td>
            </tr>
        `
    });
    const selectBtns = document.getElementsByClassName('selectBtn');
    for (let i = 0; i < selectBtns.length; i++) {
        selectBtns[i].addEventListener("click",  (e) => {
            const selectedId = e.target.id
            const currentUrl = new URL(window.location.href);
            currentUrl.searchParams.set('compareId', selectedId);
            window.location.href = currentUrl.toString();
            compareContent.classList.add("d-none");
            mainContent.style.display = "block";
        })
    }
})
