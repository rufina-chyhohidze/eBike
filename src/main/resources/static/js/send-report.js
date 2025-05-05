import {csrfHeader, csrfToken} from "./utils/csrf.js";

const sendReportToEmailButton = document.getElementsByClassName('report-id')[0];

sendReportToEmailButton.addEventListener('click', sendReportToCustomer);

async function sendReportToCustomer() {
    const reportId = sendReportToEmailButton.id;
    console.log("Sending report with Id: " + reportId + " - to customer");

    const response = await fetch(`/api/reports/${reportId}/customer`, {
        method: "POST",
        headers : {
            "Content-Type": "application/json",
            "Accept": "application/json",
            [csrfHeader]: csrfToken
        }
    });

    if (response.ok) {
        alert("Report sent successfully!");
    } else {
        alert("Failed to send report.");
    }
}
