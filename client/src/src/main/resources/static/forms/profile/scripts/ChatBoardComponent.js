function getChat(friendId, time) {
    let xhr = new XMLHttpRequest();
    let url = '/messenger/history'

    let request = {
        friendId: friendId,
        fromTime: time
    };

    xhr.open('POST', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify(request));

    if (xhr.status === 401) {
        alert(JSON.parse(xhr.response).errorText);
        document.location.href = '/forms/login/login.html';
        return;
    }

    if (xhr.status === 200) {
        return JSON.parse(xhr.response);
    }

    return [];
}

function updateChat() {
    let messages = getChat(this.friendId, this.lastMessageTime + 1)

    if (messages.length == 0) {
        return;
    }

    let messagesComponents = messages.map(
        (e) => {
            let author = "unknown person";
            let elementId = "unknown_person_id"
            if (e.fromUser === this.friendId) {
                author = this.friendName;
                elementId = "friend_chat_message_id";
            }
            else
            {
                author = "me";
                elementId = "author_chat_message_id";
            }
            return React.createElement(
                "div",
                 {id: elementId},
                 "[" + new Date(e.time).toLocaleString().replace(/,/g, '') + "]\n" + author +": " + e.message
            );
        }
    );

    this.lastMessageTime = Math.max.apply(null, messages.map((e) => { return e.time; }));
    this.board.push(messagesComponents);
    this.forceUpdate();

    let chatBoard = document.getElementById('chat_board');
    chatBoard.scrollTop = chatBoard.scrollHeight;
}

function onFileSendClicked(friendId) {
    let date = new Date();
    let currentTime = date.getTime();

    let xhr = new XMLHttpRequest();
    let url = '/messenger/send/file'

    xhr.open('POST', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        'message': "This file contains secret info",
        'friendId': friendId,
        'messageTime': currentTime
    }));

    alert('File send successfully');
}

function onMessageSendSelected(friendId) {
    let text = document.getElementById('message_input').value;

    if (text.length == 0) {
        alert('Введите сообщение');
        return;
    }

    let date = new Date();
    let currentTime = date.getTime();

    let xhr = new XMLHttpRequest();
    let url = '/messenger/send/message'

    xhr.open('POST', url, false);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.send(JSON.stringify({
        'message': text,
        'friendId': friendId,
        'messageTime': currentTime
    }));

    if (xhr.status !== 200) {
        alert(JSON.parse(xhr.response).errorText);
        if (xhr.status === 401) {
            document.location.href = '/forms/login/login.html';
            return;
        }
        return;
    }

    document.getElementById('message_input').value = '';
}

class ChartTitle extends React.Component {
    render() {
        return React.createElement(
            "div",
            {
                id: 'chat_title'
            },
            "Chatting with user: " + this.props.friendName + ' [id=' + this.props.friendId + ']'
        );
    }
}

class ChartBoard extends React.Component {
    constructor(props) {
        super(props);

        this.friendId = props.friendId;
        this.friendName = props.friendName;

        this.lastMessageTime = 0;
        this.board = [];

        this.updateChatTimer = setInterval(
            updateChat.bind(this),
            2000
        );
    }

    componentWillUnmount() {
        this.stopTimer();
    }

    stopTimer() {
        if (this.updateChatTimer != null) {
            clearInterval(this.updateChatTimer);
            this.updateChatTimer = null;
        }
    }

    render() {
        if (this.props.friendId != this.friendId) {
            this.stopTimer();

            this.board = [];
            this.friendId = this.props.friendId;
            this.friendName = this.props.friendName;

            this.lastMessageTime = 0;

            this.updateChatTimer = setInterval(
                updateChat.bind(this),
                2000
            );
        }

        return React.createElement(
            "div",
            {
                id: 'chat_board'
            },
            this.board
        );
    }
}

class ChatInput extends React.Component {
    onInputMessageKeyPressed(friendId, e) {
        if(e.key === 'Enter') {
            onMessageSendSelected(friendId);
        }
    }

    render() {
        let components = [
            React.createElement("input", {
                id: 'message_input',
                type: 'search',
                placeholder: 'Your message should be here',
                onKeyDown: this.onInputMessageKeyPressed.bind(null, this.props.friendId)
            }, null),
            React.createElement("input", {
                type: 'file',
                onChange: onFileSendClicked.bind(null, this.props.friendId)
            }, null),
            React.createElement("button", {
                id: 'message_send_button',
                onClick: onMessageSendSelected.bind(null, this.props.friendId)
            }, 'Send', null),
        ];

        return React.createElement(
            "div",
            {},
            components
        );
    }
}

class ChatBoardComponent extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        this.friendId = this.props.friendId;

        let components = [
            React.createElement(ChartTitle, {friendId: this.friendId, friendName: this.props.friendName}, null),
            React.createElement(ChartBoard, {friendId: this.friendId, friendName: this.props.friendName}, null),
            React.createElement(ChatInput, {friendId: this.friendId}, null)
        ];

        let element = React.createElement(
            "div",
            {
                id: 'chat_component'
            },
            components
        );
        return element;
    }
}

