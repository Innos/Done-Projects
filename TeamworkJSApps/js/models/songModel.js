var app = app || {};


app.songModel = (function() {
    function Song(baseUrl, appId, credentials, requester) {
        this._requester = requester;
        this.serviceUrl = baseUrl + "/appdata/" + appId + "/songs/";
        this.fileUrl = baseUrl +'/blob/' + appId;
        this.credentials = credentials;
    }

    Song.prototype.getAllSongs = function () {
        return this._requester.get(this.serviceUrl, this.credentials.getHeaders(false,true))
    };

    Song.prototype.addSong = function (data) {
        return this._requester.post(this.serviceUrl, data, this.credentials.getHeaders(true,true));
    };

    Song.prototype.editSong = function (id,data) {
        var url = this.serviceUrl + id;
        return this._requester.put(url, data, this.credentials.getHeaders(true, true));
    };

    Song.prototype.getSong = function(id){
        var requestUrl = this.fileUrl + '/' + id;
        return this._requester.get(requestUrl, this.credentials.getHeaders(false, true));
    };

    Song.prototype.deleteSong = function (id) {
        var url = this.serviceUrl + id;
        return this._requester.delete(url, this.credentials.getHeaders(false, true));
    };

    return {
        load: function (baseUrl, appId, credentials, requester) {
            return new Song(baseUrl, appId, credentials, requester);
        }
    }
})();