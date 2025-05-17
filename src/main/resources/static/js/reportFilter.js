document.addEventListener("DOMContentLoaded", () => {
    const frameInput = document.querySelector('input[name="frameNumber"]');
    const engineInput = document.querySelector('input[name="engineType"]');
    const tableBody = document.querySelector("#reports-table-content");

    const fetchReports = async () => {
        const frameNumber = frameInput.value.trim();
        const engineType = engineInput.value.trim();

        const params = new URLSearchParams();
        if (frameNumber) params.append("frameNumber", frameNumber);
        if (engineType) params.append("engineType", engineType);

        const response = await fetch(`/api/reports?${params.toString()}`);
        const reports = await response.json();

        console.log(reports);
        renderReports(reports);
    };

    const renderReports = (reports) => {
        tableBody.innerHTML = ""; // Clear table
        console.log("Runs")
        if (reports.length === 0) {
            const row = document.createElement("tr");
            row.innerHTML = `
        <td colspan="7" class="p-3 text-center text-gray-400">
          No reports found matching your search.
        </td>
      `;
            tableBody.appendChild(row);
            return;
        }

        reports.forEach((report) => {
            const row = document.createElement("tr");
            row.classList.add("border-b");
            row.innerHTML = `
        <td class="p-3">${report.reportId}</td>
        <td class="p-3">${report.reportDate}</td>
        <td class="p-3">${report.customerId}</td>
        <td class="p-3">${report.frameNumber}</td>
        <td class="p-3">${report.engineType}</td>
        <td class="p-3">${report.benchId}</td>
        <td class="p-3">
          <a class="bg-purple-500 text-white px-4 py-2 rounded-lg hover:bg-purple-600 w-full"
             href="/report/${report.reportId}">Report</a>
        </td>
      `;
            tableBody.appendChild(row);
        });
    };

    frameInput.addEventListener("input", fetchReports);
    engineInput.addEventListener("input", fetchReports);
});
