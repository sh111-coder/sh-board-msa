const form = document.getElementById('login-form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    let member = {};
    for (const entry of formData.entries()) {
        const [key, value] = entry;
        member[key] = value;
    }

    loginMember(member);
});

const loginMember = (member) => {
    const jsonData = JSON.stringify(member);

    axios.request({
        url: '/api/members/login',
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        data: jsonData
    }).then((response) => {
        window.location.href = '/boards';
    }).catch((error) => {
        console.error(error);
    });
};
