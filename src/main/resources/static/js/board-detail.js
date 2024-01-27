document.addEventListener("DOMContentLoaded", () => {
    const path = window.location.pathname;
    const parts = path.split('/');
    const boardId = parts[parts.length - 1];

    axios.request({
        url: '/api/boards/' + boardId,
        method: 'get',
    }).then((response) => {
        const board = response.data;

        const boardElement = `
                        <h1 class="post-title">${board["title"]}</h1>
                        <p>작성자: ${board["writerNickname"]}</p>
                        <p>${new Date(board["createdAt"]).toLocaleString()}</p>
                        <p>조회수: ${board["viewCount"]}</p>
                        <hr>
                        <p>${board["content"]}</p>
                    `;

        document.querySelector('.board-detail').innerHTML = boardElement;
    }).catch((error) => {
        console.error(error);
    });
});
