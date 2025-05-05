import { csrfToken, csrfHeader } from './utils/csrf.js';

function getCsrfToken() {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    return csrfToken;
}

async function changeSettings(settingsArray) {
    const csrfToken = getCsrfToken(); // Fetch CSRF token just before sending the request

    const response = await fetch("/api/report-settings", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(settingsArray) // Send the array of settings
    });

    if (response.ok) {
        alert("Settings updated successfully!");
    } else {
        alert("Failed to update settings.");
    }
}

document.getElementById('submit').addEventListener('click', async () => {
    let horizontal = document.getElementById('horizontalVibration').value;
    let vertical = document.getElementById('verticalVibration').value;

    horizontal = horizontal === "None" ? null : parseFloat(horizontal);
    vertical = vertical === "None" ? null : parseFloat(vertical);

    // Construct an array of settings to send
    const settingsArray = [
        { settingName: "horizontalVibration", settingValue: horizontal },
        { settingName: "verticalVibration", settingValue: vertical }
    ];

    // Send the array of settings to the backend
    await changeSettings(settingsArray);
});
