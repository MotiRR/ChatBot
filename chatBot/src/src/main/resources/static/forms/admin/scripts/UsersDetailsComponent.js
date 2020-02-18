class UserDetailsComponent extends React.Component {
    render() {
        let userInfo = this.props.user;

        let messagesHolder = React.createElement(MessagesHolderComponent, {userId: userInfo.id}, null);
        let title = React.createElement("h4", {}, "Профиль пользователя " + userInfo.name);

        return React.createElement(
            'div',
            {id: 'usersDetails_component'},
            title,
            messagesHolder
        );
    }
}