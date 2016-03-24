var app = app || {};

app.songController = (function () {
    function SongController(model, viewBag) {
        this._viewBag = viewBag;
        this._model = model;
    }

    SongController.prototype.loadSongPage2 = function (selector) {
        var _this = this;
        this._model.getAllSongs()
            .then(function (success) {
                var results = {
                    songs: []
                };
                success.forEach(function (song) {
                    results.songs.push({id: song._id, title: song.title, author: song.author})
                });
                _this._viewBag.showSongs(selector, results);
            });
    };

    SongController.prototype.loadHomePageSongs = function (selector) {
        var _this = this;
        this._model.getAllSongs()
            .then(function (success) {
                var results = {
                    songs: []
                };
                success.forEach(function (song) {
                    results.songs.push({id: song._id, title: song.title, author: song.author})
                });
                _this._viewBag.showHomePageSongs(selector, results);
            });
    };

    SongController.prototype.createPlaylistSongs = function (selector) {
        var _this = this;
        this._model.getAllSongs()
            .then(function (success) {
                var results = {
                    songs: []
                };
                success.forEach(function (song) {
                    results.songs.push({id: song._id, title: song.title, author: song.author})
                });
                _this._viewBag.showPlaylistSongs(selector, results);
            });

    };

    SongController.prototype.getSong = function(id) {
        var _this = this;
        this._model.getSong(id)
            .then(function(success) {
                $.sammy (function() {
                    this.trigger('song-gotten', { response: success } );
                });
            }).done();
    };

    SongController.prototype.loadSongPage = function (selector, data) {
        this._viewBag.showSongPage(selector, data);
    };


    return {
        load: function (model, viewBag) {
            return new SongController(model, viewBag);
        }
    }
})();