function onLoginClicked(login, password) {
    let xhr = new XMLHttpRequest();

    let url = '/login'

    let data = {
        "login": document.getElementById('input_login').value,
        "password": document.getElementById('input_password').value
    };

    xhr.open('PUT', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(data));

    if (xhr.status !== 200) {
        // обработать ошибку
        alert(JSON.parse(xhr.response).errorText);
        return;
    }

    let token = JSON.parse(xhr.response).token;

    document.cookie = "token" + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
    document.cookie = "token=" + token + ";domain=;path=/";

    document.location.href = '/forms/profile/profile.html';
}