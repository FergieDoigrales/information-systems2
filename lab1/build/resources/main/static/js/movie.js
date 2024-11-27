// document.addEventListener("DOMContentLoaded", function () {
//     const movieForm = document.querySelector("#createMovieModal form");
//     if (movieForm) {
//         movieForm.addEventListener("submit", function (event) {
//             event.preventDefault();
//
//             const formData = new FormData(movieForm);
//             let hasErrors = false;
//
//             const errorElements = movieForm.querySelectorAll(".error-message");
//             errorElements.forEach(el => el.textContent = "");
//
//             const oscarsCountInput = document.getElementById("oscarsCount");
//             const oscarsCountError = document.getElementById("oscarsCountError");
//
//             const budgetInput = document.getElementById("budget");
//             const budgetError = document.getElementById("budgetError");
//
//             const totalBoxOfficeInput = document.getElementById("totalBoxOffice");
//             const totalBoxOfficeError = document.getElementById("totalBoxOfficeError");
//
//             const lengthInput = document.getElementById("length");
//             const lengthError = document.getElementById("lengthError");
//
//             const goldenPalmCountInput = document.getElementById("goldenPalmCount");
//             const goldenPalmCountError = document.getElementById("goldenPalmCountError");
//
//             oscarsCountError.textContent = "";
//             budgetError.textContent = "";
//             totalBoxOfficeError.textContent = "";
//             lengthError.textContent = "";
//             goldenPalmCountError.textContent = "";
//
//             if (oscarsCountInput.value < 1) {
//                 oscarsCountError.textContent = "Oscars Count must be greater than zero.";
//                 hasErrors = true;
//             }
//             if (budgetInput.value < 1) {
//                 budgetError.textContent = "Budget must be greater than zero.";
//                 hasErrors = true;
//             }
//             if (totalBoxOfficeInput.value < 1) {
//                 totalBoxOfficeError.textContent = "Total Box Office must be greater than zero.";
//                 hasErrors = true;
//             }
//             if (lengthInput.value < 1) {
//                 lengthError.textContent = "Length must be greater than zero.";
//                 hasErrors = true;
//             }
//             if (goldenPalmCountInput.value < 1) {
//                 goldenPalmCountError.textContent = "Golden Palm Count must be greater than zero.";
//                 hasErrors = true;
//             }
//
//             if (hasErrors) {
//                 return;
//             }
//
//             fetch(movieForm.getAttribute("action"), {
//                 method: "POST",
//                 body: formData,
//             })
//                 .then(response => {
//                     if (!response.ok) {
//                         throw new Error(`HTTP error! Status: ${response.status}`);
//                     }
//                     return response.json();
//                 })
//                 .then(data => {
//                     if (data.success) {
//                         document.getElementById("createMovieModal").style.display = "none";
//                     } else {
//                         alert(data.message || "Failed to save movie.");
//                     }
//                 })
//                 .catch(error => {
//                     console.error("Error saving movie:", error);
//                 });
//         });
//     }
//
//     document.querySelectorAll(".edit-movie-button").forEach(button => {
//         button.addEventListener("click", function () {
//             const movieId = this.getAttribute("data-id");
//             fetch(`/movie/${movieId}`)
//                 .then(response => response.json())
//                 .then(movie => {
//                     if (movie) {
//                         const modal = document.getElementById("createMovieModal");
//                         modal.style.display = "block";
//
//                         const closeBtn = modal.querySelector(".close");
//                         if (closeBtn) {
//                             closeBtn.onclick = function () {
//                                 modal.style.display = "none";
//                             };
//                         }
//
//                         window.onclick = function (event) {
//                             if (event.target === modal) {
//                                 modal.style.display = "none";
//                             }
//                         };
//
//                         document.querySelector("#createMovieModal form").setAttribute("action", "/movie/update");
//                         document.querySelector("#movieId").value = movie.movieId;
//                         document.querySelector("#name").value = movie.name;
//                         document.querySelector("#coordinates").value = movie.coordinates.id;
//                         document.querySelector("#oscarsCount").value = movie.oscarsCount;
//                         document.querySelector("#budget").value = movie.budget;
//                         document.querySelector("#totalBoxOffice").value = movie.totalBoxOffice;
//                         document.querySelector("#mpaaRating").value = movie.mpaaRating;
//                         document.querySelector("#director").value = movie.director.id;
//                         document.querySelector("#screenwriter").value = movie.screenwriter.id;
//                         document.querySelector("#operator").value = movie.operator.id;
//                         document.querySelector("#length").value = movie.length;
//                         document.querySelector("#goldenPalmCount").value = movie.goldenPalmCount;
//                         document.querySelector("#genre").value = movie.genre;
//                     }
//                 })
//                 .catch(error => console.error("Error fetching movie:", error));
//         });
//     });
//
//     document.querySelectorAll(".delete-movie-button").forEach(button => {
//         button.addEventListener("click", function () {
//             const movieId = this.getAttribute("data-id");
//             if (confirm("Are you sure you want to delete this movie?")) {
//                 fetch(`/movie/${movieId}`, { method: "DELETE" })
//                     .then(response => {
//                         if (response.ok) {
//                             location.reload();
//                         } else {
//                             alert("Failed to delete movie");
//                         }
//                     })
//                     .catch(error => console.error("Error deleting movie:", error));
//             }
//         });
//     });
// });
document.addEventListener("DOMContentLoaded", function () {
    const socket = new SockJS('/ws');  // Устанавливаем WebSocket соединение
    const stompClient = Stomp.over(socket);  // Создаем STOMP-клиент

    // Подключаемся к серверу WebSocket
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Подписываемся на канал /topic/movies, чтобы получать обновления
        stompClient.subscribe('/topic/movies', function (message) {
            const movieData = JSON.parse(message.body);
            console.log('Received update: ', movieData);

            fetchMovies();
            // Обновляем интерфейс на клиенте в зависимости от типа события
        });

            // Обновляем интерфейс на клиенте в зависимости от типа события
            // updateMoviesList(movieData);
        });
    });
// Функция для обновления таблицы с фильмами

let currentPage = 0;  // Изначально страница 0

// Функция для запроса фильмов с учетом пагинации
function fetchMovies() {
    fetch(`/movies?page=${currentPage}&size=10`)
        .then(response => response.json())
        .then(data => {
            updateMoviesTable(data.content);  // Обновляем таблицу
            // Опционально, обновите кнопки навигации на основе текущей страницы
        })
        .catch(error => console.error("Error fetching movies:", error));
}

// Функция для изменения страницы
function changePage(newPage) {
    currentPage = newPage;
    fetchMovies();
}

// Пример привязки кнопок "Следующая" и "Предыдущая" к изменению страницы
document.querySelector(".next-page-button").addEventListener("click", function() {
    currentPage++;
    fetchMovies();
});

document.querySelector(".prev-page-button").addEventListener("click", function() {
    if (currentPage > 0) {
        currentPage--;
        fetchMovies();
    }
});
function updateMoviesTable(movies) {
    const tableBody = document.querySelector("tbody");
    tableBody.innerHTML = '';  // Очищаем таблицу

    movies.forEach(movie => {
        const newRow = document.createElement("tr");
        newRow.innerHTML = `
            <td>${movieData.name}</td>
                    <td>${movieData.authorID}</td>
                    <td>${movieData.creationDate}</td>
                    <td>${movieData.coordinates.x}; ${movieData.coordinates.y}</td>
                    <td>${movieData.oscarsCount}</td>
                    <td>${movieData.budget}</td>
                    <td>${movieData.totalBoxOffice}</td>
                    <td>${movieData.mpaaRating}</td>
                    <td>${movieData.length}</td>
                    <td>${movieData.goldenPalmCount}</td>
                    <td>${movieData.genre}</td>
                    <td>${movieData.director.name}</td>
                    <td>${movieData.director.eyeColor}</td>
                    <td>${movieData.director.hairColor}</td>
                    <td>${movieData.director.location.x}; ${movieData.director.location.y}; ${movieData.director.location.z}</td>
                    <td>${movieData.director.passportID}</td>
                    <td>${movieData.director.nationality}</td>
                    <td>${movieData.operator.name}</td>
                    <td>${movieData.operator.eyeColor}</td>
                    <td>${movieData.operator.hairColor}</td>
                    <td>${movieData.operator.location.x}; ${movieData.operator.location.y}; ${movieData.operator.location.z}</td>
                    <td>${movieData.operator.passportID}</td>
                    <td>${movieData.operator.nationality}</td>
                    <td>${movieData.screenwriter.name}</td>
                    <td>${movieData.screenwriter.eyeColor}</td>
                    <td>${movieData.screenwriter.hairColor}</td>
                    <td>${movieData.screenwriter.location.x}; ${movieData.screenwriter.location.y}; ${movieData.screenwriter.location.z}</td>
                    <td>${movieData.screenwriter.passportID}</td>
                    <td>${movieData.screenwriter.nationality}</td>
                    <td>
                        <button class="edit-movie-button btn">Edit</button>
                        <button class="delete-movie-button btn">Delete</button>
                    </td>
        `;
        tableBody.appendChild(newRow);
    });

    // Функция для обновления списка фильмов в интерфейсе
    function updateMoviesList(movieData) {
        const tableBody = document.querySelector("tbody");  // Получаем тело таблицы
        const newRow = document.createElement("tr");

        newRow.innerHTML = `
        <td>${movieData.name}</td>
        <td>${movieData.authorID}</td>
        <td>${movieData.creationDate}</td>
        <td>${movieData.coordinates.x}; ${movieData.coordinates.y}</td>
        <td>${movieData.oscarsCount}</td>
        <td>${movieData.budget}</td>
        <td>${movieData.totalBoxOffice}</td>
        <td>${movieData.mpaaRating}</td>
        <td>${movieData.length}</td>
        <td>${movieData.goldenPalmCount}</td>
        <td>${movieData.genre}</td>
        <td>${movieData.director.name}</td>
        <td>${movieData.director.eyeColor}</td>
        <td>${movieData.director.hairColor}</td>
        <td>${movieData.director.location.x}; ${movieData.director.location.y}; ${movieData.director.location.z}</td>
        <td>${movieData.director.passportID}</td>
        <td>${movieData.director.nationality}</td>
        <td>${movieData.operator.name}</td>
        <td>${movieData.operator.eyeColor}</td>
        <td>${movieData.operator.hairColor}</td>
        <td>${movieData.operator.location.x}; ${movieData.operator.location.y}; ${movieData.operator.location.z}</td>
        <td>${movieData.operator.passportID}</td>
        <td>${movieData.operator.nationality}</td>
        <td>${movieData.screenwriter.name}</td>
        <td>${movieData.screenwriter.eyeColor}</td>
        <td>${movieData.screenwriter.hairColor}</td>
        <td>${movieData.screenwriter.location.x}; ${movieData.screenwriter.location.y}; ${movieData.screenwriter.location.z}</td>
        <td>${movieData.screenwriter.passportID}</td>
        <td>${movieData.screenwriter.nationality}</td>
        <td>
            <button class="edit-movie-button btn">Edit</button>
            <button class="delete-movie-button btn">Delete</button>
        </td>
    `;

        tableBody.appendChild(newRow);  // Добавляем новый элемент в таблицу
        // Здесь добавьте логику для обновления вашего UI с полученными данными
        // Например, добавление, изменение или удаление элемента в списке фильмов.
        console.log('Updated movie list with:', movieData);
    }

    // Подключение события для отправки формы создания фильма
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
                        // Отправляем обновление в WebSocket после успешного добавления фильма
                        stompClient.send("/app/movies", {}, JSON.stringify(data));
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
