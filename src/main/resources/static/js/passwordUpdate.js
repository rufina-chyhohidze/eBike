import {csrfHeader, csrfToken} from "./utils/csrf.js";

console.log("IN PASSWORD UPDATEEEEE")

document.getElementById('passwordForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const id = document.getElementById('id').value;
    const newPassword = document.getElementById('newPassword').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const messageEl = document.getElementById('passwordMessage');

    messageEl.textContent = '';
    messageEl.className = 'text-sm mt-2';

    console.log("IN PASSWORD UPDATEEEEE FIRST")

    if (newPassword !== confirmPassword) {
        messageEl.textContent = 'Passwords do not match.';
        messageEl.classList.add('text-red-600');

        console.log("IN PASSWORD UPDATEEEEE IF NOT THE SAME")

        return;
    }

    try {
        const response = await fetch(`/api/users/${id}`, {
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

            console.log("IN PASSWORD UPDATEEEEE IN RESPONSE OK")
        } else {
            const err = await response.text();
            messageEl.textContent = `Update failed: ${err}`;
            messageEl.classList.add('text-red-600');

            console.log("IN PASSWORD UPDATEEEEE IN ELSE ")
        }
    } catch (err) {
        console.error(err);
        messageEl.textContent = 'An error occurred. Please try again.';
        messageEl.classList.add('text-red-600');

        console.log("IN PASSWORD UPDATEEEEE IN CATCH ERROR")
    }
});