document.addEventListener("DOMContentLoaded", () => {
    const openBtn = document.getElementById("openModalBtn");
    const modal = document.getElementById("categoryModal");
    const closeBtn = document.getElementById("closeModalBtn");

    const searchInput = document.getElementById("search-input");
    const suggestionsContainer = document.getElementById("autocomplete-suggestions");
    let debounceTimeout;

    if (openBtn && modal && closeBtn) {
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
    }

    if (searchInput && suggestionsContainer) {
        // AJAX-request
        async function fetchSuggestions(term) {
            const response = await fetch(`/api/words/autocomplete?term=${encodeURIComponent(term)}`);
            return await response.json();
        }

        // Show suggestions
        function showSuggestions(suggestions) {
            suggestionsContainer.innerHTML = '';
            if (suggestions.length === 0) {
                suggestionsContainer.style.display = 'none';
                return;
            }

            const ul = document.createElement('ul');
            suggestions.forEach(suggestion => {
                const li = document.createElement('li');
                li.textContent = `${suggestion.japanese} (${suggestion.translation}) - ${suggestion.example}`;
                li.addEventListener('click', () => {
                    searchInput.value = suggestion.japanese;
                    suggestionsContainer.innerHTML = '';
                    suggestionsContainer.style.display = 'none';
                    searchInput.form.submit();
                });
                ul.appendChild(li);
            });
            suggestionsContainer.appendChild(ul);
            suggestionsContainer.style.display = 'block';
        }

        // Input text handler
        searchInput.addEventListener('input', () => {
            clearTimeout(debounceTimeout);
            debounceTimeout = setTimeout(async () => {
                const term = searchInput.value.trim();
                if (term.length < 1) {
                    suggestionsContainer.innerHTML = '';
                    suggestionsContainer.style.display = 'none';
                    return;
                }

                try {
                    const suggestions = await fetchSuggestions(term);
                    showSuggestions(suggestions);
                } catch (err) {
                    console.error('Error fetching suggestions: ', err);
                    suggestionsContainer.innerHTML = '';
                    suggestionsContainer.style.display = 'none';
                }
            }, 300)
        });

        // Hide suggestions when cursor out of field
        document.addEventListener('click', (event) => {
            if(!searchInput.contains(event.target) && !suggestionsContainer.contains(event.target)) {
                suggestionsContainer.innerHTML = '';
                suggestionsContainer.style.display = 'none';
            }
        });

        // Key event handler
        searchInput.addEventListener('keydown', (event) => {
            if(event.key === 'Enter') {
                suggestionsContainer.innerHTML = '';
                suggestionsContainer.style.display = 'none';
            }
        });
    } else {
        console.log("Autocomplete elements not found on this page.");
    }
});