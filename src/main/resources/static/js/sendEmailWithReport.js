import {csrfHeader, csrfToken} from "./utils/csrf.js";

const btn = document.getElementById('getReportEmailBtn');
const pathParts     = window.location.pathname.split('/');
const reportId      = pathParts[pathParts.indexOf('report') + 1];

btn.addEventListener('click', async () => {
    const res = await fetch(`/api/reports/${reportId}/email`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({})
    })

    if(res.status === 200) {
        alert("Email sent successfully!");
    } else {
        alert("Failed to send email.");
    }
});