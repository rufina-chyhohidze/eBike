const visualCheckCheckbox = document.getElementById("checkbox-visual-check");
const functionalCheckCheckbox = document.getElementById("checkbox-functional-check");
const visualCheckSection = document.getElementById("visual-check-section");
const functionalCheckSection = document.getElementById("functional-check-section");

visualCheckCheckbox.addEventListener("change", () => {
    visualCheckSection.classList.toggle("hidden");
});

functionalCheckCheckbox.addEventListener("change", () => {
    functionalCheckSection.classList.toggle("hidden");
});