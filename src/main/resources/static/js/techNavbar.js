    const currentPath = window.location.pathname;
    const links = document.querySelectorAll("aside ul li");

    links.forEach(li => {
        const a = li.querySelector("a");
        if (a && a.getAttribute("href") === currentPath) {
            li.classList.remove("text-gray-400");
            li.classList.add("text-purple-500");
        }
    });
