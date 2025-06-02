import activateBtn from "./utils/addEventListenerToSelectButtonsInReportComparison.js";

const removeSelectedReport = document.getElementById('removeSelectedReport');
const compareButton = document.getElementById('compareButton');
const mainContent = document.getElementById('main-content');
const reportsTableContent = document.getElementById('reports-table-content');
const compareContent = document.getElementById('report-selection');
const params = new URLSearchParams(window.location.search);

// Extract the last numeric ID from the URL
const parts = window.location.pathname.split('/');
const id = parts.findLast(part => /^\d+$/.test(part));

// Enable/disable remove button if it exists
if (removeSelectedReport) {
    removeSelectedReport.disabled = !params.has('compareId');
    removeSelectedReport.addEventListener("click", () => {
        if (window.location.pathname.includes('detailed')) {
            window.location.href = `/report/${id}/detailed`;
        } else {
            window.location.href = `/report-comparison/${id}`;
        }
    });
}

// Set up compare button functionality if it exists
if (compareButton) {
    compareButton.addEventListener("click", async () => {
        if (mainContent) mainContent.style.display = "none";
        if (compareContent) {
            compareContent.classList.remove("hidden", "d-none");
        }

        const res = await fetch(`/api/reports?excludeCurrentReportId=true&currentReportId=${id}`);
        const reports = await res.json();
        reportsTableContent.innerHTML += ``;

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
            `;
        });

        activateBtn();
    });
}
