var app = app || {};


app.playlistModel = (function() {
    function Playlist(baseUrl, appId, credentials, requester) {
        this._requester = requester;
        this.serviceUrl = baseUrl + "/appdata/" + appId + "/playlists/";
        this.credentials = credentials;
    }

    Playlist.prototype.getAllPlaylists = function () {
        return this._requester.get(this.serviceUrl, this.credentials.getHeaders(false,true))
    };

    Playlist.prototype.getPlaylist = function (id) {
        var url = this.serviceUrl + id + "?resolve=songs";
        return this._requester.get(url, this.credentials.getHeaders(false,true))
    };

    Playlist.prototype.queryPlaylists = function (query) {
        var url = this.serviceUrl + query;
        return this._requester.get(url, this.credentials.getHeaders(false,true))
    };

    Playlist.prototype.addPlaylist = function (data) {
        return this._requester.post(this.serviceUrl, data, this.credentials.getHeaders(true,true));
    };

    Playlist.prototype.editPlaylist = function (id,data) {
        var url = this.serviceUrl + id;
        return this._requester.put(url, data, this.credentials.getHeaders(true, true));
    };

    Playlist.prototype.deletePlaylist = function (id) {
        var url = this.serviceUrl + id;
        return this._requester.delete(url, this.credentials.getHeaders(false, true));
    };

    return {
        load: function (baseUrl, appId, credentials, requester) {
            return new Playlist(baseUrl, appId, credentials, requester);
        }
    }
})();