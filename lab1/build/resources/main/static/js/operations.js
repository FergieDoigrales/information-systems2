function getMovieCountByGenre() {
    const genre = document.getElementById('genre').value;
    axios.get(`/movie/count-by-genre?genre=${genre.toUpperCase()}`)
        .then(response => {
            document.getElementById('movieCountResult').innerHTML = `<strong>Count:</strong> ${response.data.count}`;
        })
        .catch(error => {
            document.getElementById('movieCountResult').innerHTML = `<strong>Error:</strong> ${error.response.data.message}`;
        });
}

function getUniqueGoldenPalmCounts() {
    axios.get('/movie/unique-golden-palm-counts')
        .then(response => {
            const counts = response.data.join(', ');
            document.getElementById('goldenPalmCountsResult').innerHTML = `<strong>Unique Golden Palm Counts:</strong> ${counts}`;
        })
        .catch(error => {
            document.getElementById('goldenPalmCountsResult').innerHTML = `<strong>Error:</strong> ${error.response.data.message}`;
        });
}

function getMovieWithMinDirector() {
    axios.get('/movie/min-director')
        .then(response => {
            const movie = response.data;
            document.getElementById('minDirectorMovieResult').innerHTML = `<strong>Movie:</strong> ${movie}`;
        })
        .catch(error => {
            document.getElementById('minDirectorMovieResult').innerHTML = `<strong>Error:</strong> ${error.response.data.message}`;
        });
}

function getOperatorsWithoutOscar() {
    axios.get('/person/operators-without-oscar')
        .then(response => {
            const operators = response.data.map(operator => operator.name).join(', ');
            document.getElementById('operatorsResult').innerHTML = `<strong>Operators without Oscar:</strong> ${operators}`;
        })
        .catch(error => {
            document.getElementById('operatorsResult').innerHTML = `<strong>Error:</strong> ${error.response.data.message}`;
        });
}

function getScreenwritersWithoutOscar() {
    axios.get('/person/screenwriters-without-oscar')
        .then(response => {
            const screenwriters = response.data.map(writer => writer.name).join(', ');
            document.getElementById('screenwritersResult').innerHTML = `<strong>Screenwriters without Oscar:</strong> ${screenwriters}`;
        })
        .catch(error => {
            document.getElementById('screenwritersResult').innerHTML = `<strong>Error:</strong> ${error.response.data.message}`;
        });
}