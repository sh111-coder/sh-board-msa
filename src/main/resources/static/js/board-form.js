const form = document.getElementById('write-form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    const board = {};

    formData.forEach((value, key) => {
        board[key] = value;
    });

    const boardJSON = JSON.stringify(board);
    // jsonDataString을 가지고 작업을 수행하거나 전송할 수 있습니다.
    // 여기서는 예시로 console에 출력해보겠습니다.
    console.log(boardJSON);

    writeBoard(boardJSON);
});

const writeBoard = (board) => {
    axios.request({
        url: '/api/boards',
        headers: {
            'Content-Type': 'application/json',
        },
        data: board,
        method: 'post'
    }).then((response) => {
        const redirectLocation = response.headers.get('Location');
        window.location.href = redirectLocation;
    }).catch((error) => {
        console.error(error);
    });
}
