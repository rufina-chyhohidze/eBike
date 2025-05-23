import {csrfHeader, csrfToken} from "./utils/csrf.js";

const btn = document.getElementById('getReportEmailBtn');

btn.addEventListener('click', async () => {
    const reportId      = document.getElementById("testId").value;
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