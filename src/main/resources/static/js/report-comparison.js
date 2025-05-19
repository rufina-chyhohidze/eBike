import activateBtn from "./utils/addEventListenerToSelectButtonsInReportComparison.js";

const removeSelectedReport = document.getElementById('removeSelectedReport');
const compareButton = document.getElementById('compareButton');
const mainContent = document.getElementById('main-content');
const reportsTableContent = document.getElementById('reports-table-content');
const compareContent = document.getElementById('report-selection');
const parts = window.location.pathname.split('/');
const id = parts[parts.length - 1];
const params = new URLSearchParams(window.location.search);

if (params.has('compareId')) {
    removeSelectedReport.disabled = false;
} else {
    removeSelectedReport.disabled = true;
}

removeSelectedReport.addEventListener("click", () => {
    window.location.href = `/report-comparison/${id}`;
})
compareButton.addEventListener("click", async () => {
    mainContent.style.display = "none";
    compareContent.classList.remove("d-none");

    const res = await fetch(`/api/reports?excludeCurrentReportId=true&currentReportId=${id}`);
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
    activateBtn();
})
