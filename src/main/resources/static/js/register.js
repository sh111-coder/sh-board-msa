const form = document.getElementById('register-form');

form.addEventListener('submit', (event) => {
    event.preventDefault();

    const formData = new FormData(event.target);
    let member = {};
    for (const entry of formData.entries()) {
        const [key, value] = entry;
        member[key] = value;
    }

    registerMember(member);
});

const registerMember = (member) => {
    const jsonData = JSON.stringify(member);

    axios.request({
        url: '/api/members/register',
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        data: jsonData
    }).then((response) => {
        window.location.href = '/';
    }).catch((error) => {
        console.error(error);
    });
};
