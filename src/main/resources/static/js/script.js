document.addEventListener("DOMContentLoaded", () => {
    const openBtn = document.getElementById("openModalBtn");
    const modal = document.getElementById("categoryModal");
    const closeBtn = document.getElementById("closeModalBtn");

    openBtn.addEventListener("click", () => {
        modal.style.display = "block";
    });

    closeBtn.addEventListener("click", () => {
       modal.style.display = "none";
    });

    window.addEventListener("click", (e) => {
        if(e.target === modal) {
            modal.style.display = "none";
        }
    });
});