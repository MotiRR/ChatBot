class AboutComponent extends React.Component {
    render() {
        let title = React.createElement(
            "h4",
            {},
            "О программе"
        );

        return React.createElement(
            "div",
            {id: "about_component_id"},
            title
        );
    }
}