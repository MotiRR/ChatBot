function onFormLoad() {
    let users = getUsers();
    ReactDOM.render(
        React.createElement(AdministrationComponent, {users: users}),
        document.getElementById('main_content')
    );
}

function showAbout() {
    ReactDOM.render(
        React.createElement(AboutComponent, {}),
        document.getElementById('main_dashboard')
    );
}

class AdministrationComponent extends React.Component {
    render() {
        let users = React.createElement(UsersListComponent, {users: this.props.users}, null);
        let details = React.createElement(
            'div',
            {id: 'main_dashboard'},
            null
        );

        return React.createElement(
            'div',
            {
                id: 'administration_component',
                className: 'administration_component_class'
            },
            users, details
        )
    }
}

class UsersListComponent extends React.Component {
    render() {
        let usersComponents = this.props.users.map(
            (usr, idx) => {
                return React.createElement(
                    UserComponent,
                    {index: idx + 1, user: usr}
                );
            }
        );

        return React.createElement('div', {
            id: 'usersList_component',
            className: 'usersList_component_class'
        }, usersComponents)
    }
}

function getUsers() {
    let request = new XMLHttpRequest();
    request.open('GET', '/administration/users', false);
    request.setRequestHeader('Content-Type', 'application/json');

    request.send();

    if (request.status !== 200) {
        return [];
    }

    return JSON.parse(request.response);
}