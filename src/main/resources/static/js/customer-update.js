import { csrfHeader, csrfToken } from "./utils/csrf.js";

// PHONE NUMBER FORM SUBMIT
document.getElementById('phoneForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('id').value;
    const phoneNumber = document.getElementById('phoneNumber').value;
    const messageEl = document.getElementById('phoneMessage');

    messageEl.textContent = '';
    messageEl.className = 'text-sm mt-2';

    try {
        const response = await fetch(`/api/customers/${id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ phoneNumber })
        });

        if (response.ok) {
            messageEl.textContent = 'Phone number updated successfully.';
            messageEl.classList.add('text-green-600');
        } else {
            const err = await response.text();
            messageEl.textContent = `Update failed: ${err}`;
            messageEl.classList.add('text-red-600');
        }
    } catch (err) {
        messageEl.textContent = 'An error occurred. Please try again.';
        messageEl.classList.add('text-red-600');
    }
});

// PASSWORD FORM SUBMIT
document.getElementById('passwordForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('id').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const messageEl = document.getElementById('passwordMessage');

    messageEl.textContent = '';
    messageEl.className = 'text-sm mt-2';

    if (newPassword !== confirmPassword) {
        messageEl.textContent = 'Passwords do not match.';
        messageEl.classList.add('text-red-600');
        return;
    }

    try {
        const response = await fetch(`/api/customers/${id}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify({ password: newPassword })
        });

        if (response.ok) {
            messageEl.textContent = 'Password updated successfully.';
            messageEl.classList.add('text-green-600');
        } else {
            const err = await response.text();
            messageEl.textContent = `Update failed: ${err}`;
            messageEl.classList.add('text-red-600');
        }
    } catch (err) {
        messageEl.textContent = 'An error occurred. Please try again.';
        messageEl.classList.add('text-red-600');
    }
});
