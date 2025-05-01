import { csrfHeader, csrfToken } from './utils/csrf.js';

document.addEventListener("DOMContentLoaded", () => {
    const registerButton = document.getElementById("register-button");
    const messageBox = document.getElementById("form-message");

    registerButton.addEventListener("click", async () => {
        const name = document.getElementById('name')?.value?.trim();
        const email = document.getElementById('email')?.value?.trim();
        const phoneNumber = document.getElementById('phoneNumber')?.value?.trim() || null;

        messageBox.innerText = ""; // Clear previous message
        messageBox.style.color = "";

        if (!name || !email  || !phoneNumber) {
            showMessage("Please fill in all required fields.", "red");
            return;
        }

        const payload = { name, email, phoneNumber };

        try {
            const response = await fetch("/api/customers", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(payload)
            });

            if (response.status === 201) {
                showMessage("Registration successful! Redirecting to login...", "green");
                setTimeout(() => {
                    window.location.href = "/login";
                }, 1500);
            } else {
                const error = await response.text();
                showMessage("Registration failed: " + error, "red");
                clearForm();
            }

        } catch (err) {
            console.error("Error:", err);
            showMessage("Server or network error. Try again later.", "red");
        }
    });

    function showMessage(msg, color) {
        messageBox.innerText = msg;
        messageBox.style.color = color;
    }

    function clearForm() {
        document.querySelectorAll("input").forEach(el => el.value = "");
    }
});
