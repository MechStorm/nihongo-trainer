function toggleCreateForm() {
    const form = document.getElementById('createCategoryForm');
    if (form) {
        form.classList.toggle('hidden');
    }

    console.log('ok');
    console.log(form);
}

function toggleEditForm(id) {
    const label = document.getElementById('label-' + id);
    const form = document.getElementById('form-' + id);
    if (label && form) {
        label.classList.toggle('hidden');
        form.classList.toggle('hidden');
    }

    console.log('ok');
    console.log(form);
    console.log(label);
}