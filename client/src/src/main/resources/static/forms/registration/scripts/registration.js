function onRegisterClicked() {
    let xhr = new XMLHttpRequest();

    let url = '/register'

    let data = {
        "login": document.getElementById('input_login').value,
        "password": document.getElementById('input_password').value,
        "passwordRetry": document.getElementById('input_password_retry').value,
        "lastName": document.getElementById('input_lastName').value,
        "name": document.getElementById('input_name').value,
        "secondName": document.getElementById('input_secondName').value,
        "phone": document.getElementById('input_phone').value,
        "email": document.getElementById('input_email').value
    };

    xhr.open('POST', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(data));

    if (xhr.status !== 200) {
        // обработать ошибку
        alert(JSON.parse(xhr.response).errorText);
        return;
    }

    alert('Вы успешно зарегистрировались! Поздравляем!');
    document.location.href = '/forms/login/login.html';
}