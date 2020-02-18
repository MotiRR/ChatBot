function onFriendClicked(friendId, friendName) {
    ReactDOM.render(
        React.createElement(ChatBoardComponent, {friendId: friendId, friendName: friendName}),
        document.getElementById('board_content')
    );
}

class FriendItem extends React.Component {
    render() {
        let friendId = this.props.friend.id;
        let friendName = this.props.friend.name;

        let element = React.createElement(
            "div",
            {
                id: 'friend_' + friendId,
                className: "friend-row",
                onClick: () => {
                    onFriendClicked(friendId, friendName);
                }
            },
            this.props.index + '. ' + friendName
        );
        return element;
    }
}

class FriendsList extends React.Component {
    render() {
        let music = this.props.friends;

        let childs = music.map((e, idx) => {
            return React.createElement(FriendItem, {friend: e, index: idx + 1}, null);
        });

        return React.createElement("div", {className: "friends-list"}, childs);
    }
}