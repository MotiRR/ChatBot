class SentenceWordsComponent extends React.Component {
    render() {
        let title = React.createElement(
            "h5",
            {},
            "Состав предложения:"
        );

        let nouns = this.props.nouns;
        let verbs = this.props.verbs;

        let words = React.createElement(
            'ul',
            {},
            React.createElement('li', {}, "Глаголы: " + (verbs.length !== 0 ? verbs: "не найдены")),
            React.createElement('li', {}, "Существительные: " + (nouns.length !== 0 ? nouns: "не найдены"))
        );

        return React.createElement(
            "div",
            {},
            title,
            words
        );
    }
}

class RestrictionsComponent extends React.Component {
    render() {
        let restrictions = this.props.restrictions;

        let title = null;
        let result = null;

        if (restrictions.length === 0) {
            title = React.createElement(
                "h5",
                {},
                "Данное предложение прошло проверку успешно"
            );
        }
        else
        {
            title = React.createElement(
                "h5",
                {},
                "Результат:"
            );

            result = React.createElement('ul', {},
                restrictions.map(
                    (r) => {
                        return React.createElement('li', {}, r)
                    }
                )
            );
        }

        return React.createElement(
            "div",
            {},
            title,
            result
        );
    }
}

class MessagesAnalise extends React.Component {
    render() {
        let title = React.createElement(
            "h5",
            {},
            "Разбор предложений:"
        );

        let sentences = this.props.message.sentences.map(
            (s, idx) => {
                let message = React.createElement(
                    "div",
                    {dangerouslySetInnerHTML:  {__html:  "" + (idx+1) +". \'" + s.sentence + '\''}}
                );

                let words = React.createElement(SentenceWordsComponent, {nouns: s.nouns, verbs: s.verbs})
                let restrictions = React.createElement(RestrictionsComponent, {restrictions: s.restrictions})

                return React.createElement(
                    "div",
                    {},
                    message,
                    words,
                    restrictions
                );
            }
        );

        return React.createElement(
            "div",
            {},
            title,
            sentences
        );
    }
}

class MessagesComponent extends React.Component {
    render() {
        let message = this.props.message;
        this.messageId = message.id;

        let messagesAnalise = React.createElement(MessagesAnalise, {message: message});

        let messageContent = React.createElement(
            "blockquote",
            {class: 'message_class'},
            '\'' + message.message + '\''
        );

        let title = React.createElement(
            "h4",
            {},
            "Cообщение № " + this.props.index + " от " + new Date(message.time).toLocaleString().replace(/,/g, '')
        );

        return React.createElement(
            "div",
            {class: "message_analyse_component_class"},
              title,
              messageContent,
              messagesAnalise
        );
    }
}

class MessagesHolderComponent extends React.Component {
    render() {
        let messages = this.loadMessages(this.props.userId);

        let messagesComponents = messages.map(
            (m, idx) => {
                return React.createElement(MessagesComponent, {message: m, index: idx + 1}, null)
            }
        );

        let title = React.createElement(
            "h4",
            {},
            "Сообщения:"
        );

        return React.createElement(
            "div",
            {id: "messages_holder_component"},
            title,
            messagesComponents
        )
    }

    loadMessages(userId) {
        let request = new XMLHttpRequest();
        request.open('GET', '/administration/messages?userId=' + userId, false);
        request.setRequestHeader('Content-Type', 'application/json');

        request.send();

        if (request.status !== 200) {
            return;
        }

        return JSON.parse(request.response);
    }
}