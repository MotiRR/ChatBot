function sendAvatar(base64Text) {
    let xhr = new XMLHttpRequest();
    let url = '/profile/avatar/save'

    xhr.open('PUT', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        'image': base64Text
    }));

    if (xhr.status === 200) {
        alert('Аватар успешно обновлен!');
        document.getElementById('avatar').src = base64Text;
    }
}

function onPhotoSelected(e) {
    let file = document.getElementById('input_photo').files[0];

    if (file) {
        var reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = function (evt) {
            sendAvatar(evt.target.result);
        }
        reader.onerror = function (evt) {
            alert('Ошибка чтения файла');
        }
    }
}

function logout() {
    let xhr = new XMLHttpRequest();
    let url = '/logout'

    xhr.open('DELETE', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();

    if (xhr.status !== 200) {
        // обработать ошибку
        alert(JSON.parse(xhr.response).errorText);
    }

    document.location.href = '/forms/login/login.html';
}

function loadProfile() {
    let xhr = new XMLHttpRequest();
    let url = "/profile"

    xhr.open('GET', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();

    if (xhr.status !== 200) {
        // обработать ошибку
        alert(JSON.parse(xhr.response).errorText);
        document.location.href = '/forms/login/login.html';
        return;
    }

    let response = JSON.parse(xhr.responseText);

    document.getElementById("input_phone").value = response.phone;
    document.getElementById("input_email").value = response.email;

    document.getElementById("input_name").value = response.name;
    document.getElementById("input_lastName").value = response.lastName;
    document.getElementById("input_secondName").value = response.secondName;

    document.getElementById("avatar").src = response.photo || "/images/default_avatar.jpg";
}

function fillMusicForm() {
    let xhr = new XMLHttpRequest();
    let url = "/community/music"

    xhr.open('GET', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();

    if (xhr.status !== 200) {
        alert(JSON.parse(xhr.response).errorText);

        if (xhr.status === 401) {
            document.location.href = '/forms/login/login.html';
        }
        return;
    }

    let response = JSON.parse(xhr.responseText);

    let musicListElement = document.getElementById("musicDialog_musicList");
    while (musicListElement.firstChild) {
        musicListElement.removeChild(musicListElement.firstChild);
    }

    for (let i = 1; i <= response.length; i++) {
        let newDiv = document.createElement("div");
        newDiv.appendChild(document.createTextNode(i + ". " + response[i - 1]));
        musicListElement.appendChild(newDiv);
    }
}

function fillVideoForm() {
    let xhr = new XMLHttpRequest();
    let url = "/community/video"

    xhr.open('GET', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();

    if (xhr.status !== 200) {
        alert(JSON.parse(xhr.response).errorText);

        if (xhr.status === 401) {
            document.location.href = '/forms/login/login.html';
        }
        return;
    }

    ReactDOM.render(
        React.createElement(MusicList, {music: JSON.parse(xhr.responseText)}),
        document.getElementById('videoDialog_musicList')
    );
}

function showMusic() {
    fillMusicForm();
    document.getElementById("musicDialog").show();
}

function showVideo() {
    fillVideoForm();
    document.getElementById("videoDialog").show();
}

function getFriends() {
    let xhr = new XMLHttpRequest();
    let url = '/community/people'

    xhr.open('GET', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send();

    if (xhr.status === 200) {
        return JSON.parse(xhr.response);
    }
    return [];
}

function onFormLoad() {
    loadProfile();

    ReactDOM.render(
        React.createElement(FriendsList, {friends: getFriends()}),
        document.getElementById('div_friends_list')
    );
}