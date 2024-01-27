const getPage = (page) => {
    axios.request({
        url: '/api/boards?page=' + page,
        method: 'get',
    }).then((response) => {
        const data = response.data;
        const boards = data.boardListResponses;
        const currentPageNumber = data.boardPageResponse.currentPageNumber;
        const totalPageNumber = data.boardPageResponse.totalPageNumber;

        let boardElement = '';
        boards.forEach(board => {
            boardElement += `
                        <tr class="board">
                            <td class="board-id">${board["id"]}</td>
                            <td class="board-title">
                                <a href="/boards/${board["id"]}" class="link-indicator">${board["title"]}</a>
                            </td>
                            <td class="board-writer-nickname">${board["writerNickname"]}</td>
                            <td class="board-created-at">${board["createdAt"]}</td>
                            <td class="board-view-count">${board["viewCount"]}</td>
                        </tr>
                      `;
        });

        let pagination = '';
        for (let i = 1; i <= totalPageNumber; i++) {
            if (i == currentPageNumber) {
                pagination += `
                <li class="page-item active">
                    <a class="page-link" onclick="getPage(${i-1})">${i}</a>
                </li>
                `;
            } else {
                pagination += `
                <li class="page-item">
                    <a class="page-link" onclick="getPage(${i-1})">${i}</a>
                </li>
                `;
            }
        }

        document.querySelector('.board-items').innerHTML = boardElement;
        document.querySelector('.pagination').innerHTML = pagination;
    }).catch((error) => {
        console.error(error);
    });
}

document.addEventListener("DOMContentLoaded", getPage(0));

const getSearchPage = (page) => {
    const searchOption = document.getElementById('searchOptions').value; // 선택된 옵션
    const searchInput = document.getElementById('searchInput').value; // 검색어 입력값

    axios.request({
        url: `/api/boards/search?${searchOption}=${searchInput}&page=` + page,
        method: 'get',
    }).then((response) => {
        const data = response.data;
        const boards = data.boardListResponses;
        const currentPageNumber = data.boardPageResponse.currentPageNumber;
        const totalPageNumber = data.boardPageResponse.totalPageNumber;

        let boardElement = '';
        boards.forEach(board => {
            boardElement += `
                        <tr class="board">
                            <td class="board-id">${board["id"]}</td>
                            <td class="board-title">
                                <a href="/boards/${board["id"]}" class="link-indicator">${board["title"]}</a>
                            </td>
                            <td class="board-writer-nickname">${board["writerNickname"]}</td>
                            <td class="board-created-at">${board["createdAt"]}</td>
                            <td class="board-view-count">${board["viewCount"]}</td>
                        </tr>
                      `;
        });

        let pagination = '';
        for (let i = 1; i <= totalPageNumber; i++) {
            if (i == currentPageNumber) {
                pagination += `
                <li class="page-item active">
                    <a class="page-link" onclick="getSearchPage(${i-1})">${i}</a>
                </li>
                `;
            } else {
                pagination += `
                <li class="page-item">
                    <a class="page-link" onclick="getSearchPage(${i-1})">${i}</a>
                </li>
                `;
            }
        }

        document.querySelector('.board-items').innerHTML = boardElement;
        document.querySelector('.pagination').innerHTML = pagination;
    }).catch((error) => {
        console.error(error);
    });
}


document.getElementById('search-icon').addEventListener('click', function() {
    getSearchPage(0);
});
