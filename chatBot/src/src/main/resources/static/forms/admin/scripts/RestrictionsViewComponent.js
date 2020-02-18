function loadRestrictions() {
    let request = new XMLHttpRequest();
    request.open('GET', '/administration/restrictions', false);
    request.setRequestHeader('Content-Type', 'application/json');

    request.send();

    if (request.status !== 200) {
        return [];
    }

    return JSON.parse(request.response);
}

function blockUser(userId, restrictions) {
    let request = new XMLHttpRequest();
    request.open('PUT', '/administration/blockUser', false);
    request.setRequestHeader('Content-Type', 'application/json');

    request.send(JSON.stringify({
            "userId": userId,
            "restrictionsId": restrictions
        }
    ));

    if (request.status !== 200) {
        return false;
    }

    return true;
}

class RestrictionsViewComponent extends React.Component {
    constructor(props) {
        super(props);
        this.restrictions = loadRestrictions();
        this.hideForm = this.props.hideForm;
    }

    lockUser() {
        let checkedButtons = document.getElementById("restrictions_form_" + this.props.userId)
            .querySelectorAll('input[type="checkbox"][name="restriction"]:checked');

        if (checkedButtons.length === 0) {
            alert('Выберите причину для блокировки пользователя')
            return
        }

        let retCode = blockUser(this.props.userId, Array.from(checkedButtons).map((e) => {
            return e.id
        }));

        if (retCode === true) {
            alert('Отправлен запрос на блокировку пользователя')
            this.hideForm()
        }
    }

    render() {
        let submit = React.createElement(
            "button",
            {
                onClick: this.lockUser.bind(this)
            },
            "OK"
        );

        let br = React.createElement(
            "br",
            {},
        );

        let reasons = this.restrictions.map(
            (r, index) => {
                return [
                    br,
                    React.createElement(
                        "input",
                        {
                            type: "checkbox",
                            name: "restriction",
                            id: r.id
                        }
                    ),
                    React.createElement(
                        "label",
                        {
                            htmlFor: r.id,
                        },
                        r.label
                    )
                ]
            }
        );

        let reasonsForm = React.createElement(
            "form",
            {
                id: "restrictions_form_" + this.props.userId
            },
            reasons
        );

        return React.createElement(
            "div",
            {
                id: "lockuser_component"
            },
            'Выберите причину:',
            reasonsForm, br, submit
        );
    }
}