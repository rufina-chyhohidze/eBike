const input = document.getElementById('emailCustomer');
const suggestionsBox = document.getElementById('customer-suggestions');
const notFoundAlert = document.getElementById('customer-not-found');
const notFoundMessage = document.getElementById('customer-email-message');
const searchBtn = document.getElementById('searchBtn');


input.addEventListener('input', async () => {
    const query = input.value.trim();

    if (query.length < 2) {
        suggestionsBox.innerHTML = '';
        suggestionsBox.classList.add('hidden');
        return;
    }

    try {
        const res = await fetch(`/api/customers?name=${encodeURIComponent(query)}`)
        if (res.status === 204) {
            throw new Error('No customers found');
        }
        const customers = await res.json();
        showSuggestions(customers);
        notFoundAlert.classList.add('hidden');
    } catch (err) {
        suggestionsBox.innerHTML = '';
        suggestionsBox.classList.add('hidden');
        notFoundMessage.textContent = query;
        notFoundAlert.classList.remove('hidden');
    }
});

function showSuggestions(customers) {
    suggestionsBox.innerHTML = '';
    customers.forEach(customer => {
        const li = document.createElement('li');
        li.className = 'px-4 py-2 hover:bg-purple-100 cursor-pointer';
        li.textContent = `${customer.name} (${customer.email})`;
        li.addEventListener('click', () => {
            input.value = customer.name;
            suggestionsBox.classList.add('hidden');
            searchBtn.click();
        });
        suggestionsBox.appendChild(li);
    });
    suggestionsBox.classList.remove('hidden');
}

document.addEventListener('click', (event) => {
    if (!event.target.closest('.autocomplete-input') &&
        !event.target.closest('#customer-suggestions')) {
        suggestionsBox.classList.add('hidden');
    }
});
