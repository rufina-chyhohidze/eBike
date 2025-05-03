import { csrfToken, csrfHeader } from 'utils/csrf.js';

async function changeSettings(settingName, settingValue) {
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    const response = await fetch("/api/report-settings", {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            [header]: token
        },
        body: JSON.stringify({
            settingName,
            settingValue
        })
    });

    if (response.ok) {
        alert("Settings updated successfully!");
    } else {
        alert("Failed to update settings.");
    }
}
document.getElementById('submit').addEventListener('click', async () => {
    const horizontal = document.getElementById('horizontalVibration').value;
    const vertical = document.getElementById('verticalVibration').value;

    await changeSettings("horizontalVibration", horizontal);
    await changeSettings("verticalVibration", vertical);
});
