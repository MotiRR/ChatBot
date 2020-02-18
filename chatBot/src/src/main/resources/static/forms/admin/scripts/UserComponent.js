function onUnlockUserClicked(userId) {
    let request = new XMLHttpRequest();
    request.open('PUT', '/administration/unBlockUser?userId=' + userId, false);
    request.setRequestHeader('Content-Type', 'application/json');

    request.send();

    if (request.status !== 200) {
        return;
    }

    alert('Отправлен запрос на разблокировку пользователя')
}

function onShowDetailsClicked(userInfo) {
    ReactDOM.render(
        React.createElement(UserDetailsComponent, {user: userInfo}),
        document.getElementById('main_dashboard')
    );
}

class UserComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            showRestrictions: false
        }
    }

    showRestrictionsDialog() {
        this.setState({
            showRestrictions: !this.state.showRestrictions
        });
    }

    render() {
        this.userId = this.props.user.id;

        let details = React.createElement(
            "button",
            {
                id: 'details_button',
                onClick: onShowDetailsClicked.bind(null, this.props.user)
            },
            'Details',
            null
        );

        let lock = React.createElement(
            "button",
            {
                id: 'lock_button',
                onClick: this.showRestrictionsDialog.bind(this),
            },
            'Lock user',
            null
        );

        let unlock = React.createElement(
            "button",
            {
                id: 'unlock_button',
                onClick: onUnlockUserClicked.bind(null, this.userId),
            },
            'Unlock user',
            null
        );

        let restrictions = React.createElement(
            RestrictionsViewComponent,
            {userId: this.userId, hideForm: this.showRestrictionsDialog.bind(this)},
            null
        );

        let text = React.createElement(
            "div",
            {id: 'user_title_component'},
            '' + this.props.index + '. ' + this.props.user.name + ' [id=' + this.userId + ']',
            null
        );

        let subcomponents = [text, details, lock, unlock]
        if (this.state.showRestrictions === true) {
            subcomponents.splice(1, 0, restrictions);
        }

        return React.createElement(
            'div',
            {id: 'user_component'},
            subcomponents
        );
    }
}