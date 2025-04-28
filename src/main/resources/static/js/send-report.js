const sendReportToEmailButton = document.getElementsByClassName('report-id')[0];

sendReportToEmailButton.addEventListener('click', sendReportToCustomer);

async function sendReportToCustomer() {
    const reportId = sendReportToEmailButton.id;
    console.log("Sending report with Id: " + reportId + " - to customer");

    const response = await fetch(`/api/reports/${reportId}/send-to-customer`, {
        method: "GET"
    });

    if (response.ok) {
        alert("Report sent successfully!");
    } else {
        alert("Failed to send report.");
    }
}
