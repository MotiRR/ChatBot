class MusicItem extends React.Component {
    render() {
        return React.createElement("div", {className: "music-row"}, this.props.name);
    }
}

class MusicList extends React.Component {
    render() {
        let music = this.props.music;

        let childs = music.map((e, index) => {
            return React.createElement(MusicItem, {name: (index + 1) + '. ' + e}, null);
        });

        return React.createElement("div", {className: "music-list"}, childs);
    }
}