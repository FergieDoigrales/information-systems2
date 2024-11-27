document.addEventListener("DOMContentLoaded", function () {
    const movieForm = document.querySelector("#createMovieModal form");
    if (movieForm) {
        movieForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const formData = new FormData(movieForm);
            let hasErrors = false;

            const errorElements = movieForm.querySelectorAll(".error-message");
            errorElements.forEach(el => el.textContent = "");

            const oscarsCountInput = document.getElementById("oscarsCount");
            const oscarsCountError = document.getElementById("oscarsCountError");

            const budgetInput = document.getElementById("budget");
            const budgetError = document.getElementById("budgetError");

            const totalBoxOfficeInput = document.getElementById("totalBoxOffice");
            const totalBoxOfficeError = document.getElementById("totalBoxOfficeError");

            const lengthInput = document.getElementById("length");
            const lengthError = document.getElementById("lengthError");

            const goldenPalmCountInput = document.getElementById("goldenPalmCount");
            const goldenPalmCountError = document.getElementById("goldenPalmCountError");

            oscarsCountError.textContent = "";
            budgetError.textContent = "";
            totalBoxOfficeError.textContent = "";
            lengthError.textContent = "";
            goldenPalmCountError.textContent = "";

            if (oscarsCountInput.value < 1) {
                oscarsCountError.textContent = "Oscars Count must be greater than zero.";
                hasErrors = true;
            }
            if (budgetInput.value < 1) {
                budgetError.textContent = "Budget must be greater than zero.";
                hasErrors = true;
            }
            if (totalBoxOfficeInput.value < 1) {
                totalBoxOfficeError.textContent = "Total Box Office must be greater than zero.";
                hasErrors = true;
            }
            if (lengthInput.value < 1) {
                lengthError.textContent = "Length must be greater than zero.";
                hasErrors = true;
            }
            if (goldenPalmCountInput.value < 1) {
                goldenPalmCountError.textContent = "Golden Palm Count must be greater than zero.";
                hasErrors = true;
            }

            if (hasErrors) {
                return;
            }

            fetch(movieForm.getAttribute("action"), {
                method: "POST",
                body: formData,
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        document.getElementById("createMovieModal").style.display = "none";
                    } else {
                        alert(data.message || "Failed to save movie.");
                    }
                })
                .catch(error => {
                    console.error("Error saving movie:", error);
                });
        });
    }

    document.querySelectorAll(".edit-movie-button").forEach(button => {
        button.addEventListener("click", function () {
            const movieId = this.getAttribute("data-id");
            fetch(`/movie/${movieId}`)
                .then(response => response.json())
                .then(movie => {
                    if (movie) {
                        const modal = document.getElementById("createMovieModal");
                        modal.style.display = "block";

                        const closeBtn = modal.querySelector(".close");
                        if (closeBtn) {
                            closeBtn.onclick = function () {
                                modal.style.display = "none";
                            };
                        }

                        window.onclick = function (event) {
                            if (event.target === modal) {
                                modal.style.display = "none";
                            }
                        };

                        document.querySelector("#createMovieModal form").setAttribute("action", "/movie/update");
                        document.querySelector("#movieId").value = movie.movieId;
                        document.querySelector("#name").value = movie.name;
                        document.querySelector("#coordinates").value = movie.coordinates.id;
                        document.querySelector("#oscarsCount").value = movie.oscarsCount;
                        document.querySelector("#budget").value = movie.budget;
                        document.querySelector("#totalBoxOffice").value = movie.totalBoxOffice;
                        document.querySelector("#mpaaRating").value = movie.mpaaRating;
                        document.querySelector("#director").value = movie.director.id;
                        document.querySelector("#screenwriter").value = movie.screenwriter.id;
                        document.querySelector("#operator").value = movie.operator.id;
                        document.querySelector("#length").value = movie.length;
                        document.querySelector("#goldenPalmCount").value = movie.goldenPalmCount;
                        document.querySelector("#genre").value = movie.genre;
                    }
                })
                .catch(error => console.error("Error fetching movie:", error));
        });
    });

    document.querySelectorAll(".delete-movie-button").forEach(button => {
        button.addEventListener("click", function () {
            const movieId = this.getAttribute("data-id");
            if (confirm("Are you sure you want to delete this movie?")) {
                fetch(`/movie/${movieId}`, { method: "DELETE" })
                    .then(response => {
                        if (response.ok) {
                            location.reload();
                        } else {
                            alert("Failed to delete movie");
                        }
                    })
                    .catch(error => console.error("Error deleting movie:", error));
            }
        });
    });
});