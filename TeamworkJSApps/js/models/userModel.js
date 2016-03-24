var app = app || {};

app.user = (function () {
    function User(baseUrl, appId, credentials, requester) {
        this._requester = requester;
        this.serviceUrl = baseUrl + "/user/" + appId + "/";
        this.credentials = credentials;
    }

    User.prototype.login = function (data) {
        var url = this.serviceUrl + "login";
        return this._requester.post(url, data, this.credentials.getHeaders(true))
    };

    User.prototype.register = function (data) {
        return this._requester.post(this.serviceUrl, data, this.credentials.getHeaders(true));
    };

    User.prototype.editProfile = function (data) {
        var url = this.serviceUrl + this.credentials.getUserId();
        return this._requester.put(url, data, this.credentials.getHeaders(true, true));
    };

    User.prototype.getCurrentUser = function () {
        var user = {
            'userId': this.credentials.getUserId(),
            'username': this.credentials.getUsername
        };
        return user;
    };

    User.prototype.logout = function () {
        var url = this.serviceUrl + "_logout";
        return this._requester.post(url, null, this.credentials.getHeaders(false, true));
    };

    return {
        load: function (baseUrl, appId, credentials, requester) {
            return new User(baseUrl, appId, credentials, requester);
        }
    }
}());