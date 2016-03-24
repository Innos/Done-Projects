var app = app || {};

app.playlistController = (function () {
    function PlaylistController(model, viewbag, credentials) {
        this._model = model;
        this._viewbag = viewbag;
        this.credentials = credentials;
    }

    PlaylistController.prototype.loadPlaylistsPage = function (selector) {
        var _this = this;

        this._model.getAllPlaylists()
            .then(function (success) {
                var result = {
                    playlists: []
                };

                success.forEach(function (playlist) {
                    result.playlists.push({'id': playlist._id, title: playlist.title, 'author': playlist.author});
                });

                _this._viewbag.showPlaylists(selector, result);
            }).done();
    };

    PlaylistController.prototype.loadCreatePlaylist = function (selector) {
        this._viewbag.createPlaylist(selector);
    };

    PlaylistController.prototype.loadMyPlaylists = function (selector) {
        var _this = this;
        var currentUserId = this.credentials.getUserId();
        this._model.queryPlaylists('?query={"_acl.creator":' + '"' + currentUserId + '"}')
            .then(function (success) {
                var result = {
                    playlists: []
                };

                success.forEach(function (playlist) {
                    result.playlists.push({'id': playlist._id, title: playlist.title, 'author': playlist.author});
                });
                _this._viewbag.showPersonalPlaylists(selector, result);
            })

    };

    PlaylistController.prototype.getPlaylist = function (selector, id) {
        var _this = this;

        this._model.getPlaylist(id)
            .then(function (success) {
                var songs = success.songs.map(function (song) {
                    return song._obj;
                });
                var result = {
                    playlist: {title: success.title, author: success.author, songs: songs}
                };
                $.sammy(function () {
                    this.trigger('redirectUrl', {url: "#/playlist"})
                });
                _this._viewbag.showPlaylist(selector, result);
            }
        ).
            done();
    };

    PlaylistController.prototype.addPlaylist = function (data) {
        var _this = this;

        this._model.addPlaylist(data)
            .then(function (success) {
                $.sammy(function () {
                    this.trigger('redirectUrl', {url: "#/playlists"});
                })
            })
    };

    PlaylistController.prototype.editPlaylist = function (data) {
        var _this = this;

        var id = data.id;
        this._model.editPlaylist(id, data)
            .then(function (success) {
                $.sammy(function () {
                    this.trigger('redirectUrl', {url: "#/playlists"});
                })
            })
    };

    PlaylistController.prototype.deletePlaylist = function (data) {
        var _this = this;

        var id = data.id;
        this._model.deletePlaylist(id)
            .then(function (success) {
                $.sammy(function () {
                    this.trigger('redirectUrl', {url: "#/myPlaylists"});
                })
            })
    };

    return {
        load: function (model, viewbag, credentials) {
            return new PlaylistController(model, viewbag, credentials);
        }
    }

}());