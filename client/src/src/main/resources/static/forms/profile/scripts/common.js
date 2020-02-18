function closeDialog(dialogName) {
    document.getElementById(dialogName).close();
}

function getToken() {
    let token = document.cookie
        .split(';').find(function (cookie) {
            return cookie.trim().startsWith('token=');
        });

    if (token === undefined) {
        return null;
    }

    return token.substr('token='.length);
}