var app = app|| {};

app.credentials = (function(){
    function Credentials(appId,appSecret){
        this._appId = appId;
        this._appSecret = appSecret;
    }

    Credentials.prototype.getHeaders = function (contentType,useSession){
        var headers = {};
        if(contentType){
            headers['Content-Type'] = "application/json";
        }
        if(useSession){
            headers['Authorization'] = "Kinvey " + sessionStorage.getItem('sessionId');
        }
        else{
            var token = this._appId + ":" + this._appSecret;
            headers['Authorization'] = "Basic " + btoa(token);
        }
        return headers;
    };

    Credentials.prototype.getSession = function(){
        return sessionStorage.getItem('sessionId');
    };
    Credentials.prototype.setSession = function (sessionAuth){
        sessionStorage.setItem('sessionId',sessionAuth);
    };
    Credentials.prototype.getUserId = function (){
        return sessionStorage.getItem('userId');
    };
    Credentials.prototype.setUserId = function (userId){
        sessionStorage.setItem('userId',userId);
    };
    Credentials.prototype.getUsername = function (){
        return sessionStorage.getItem('username');
    };
    Credentials.prototype.setUsername = function (username){
        sessionStorage.setItem('username',username);
    };
    Credentials.prototype.clear = function (){
        sessionStorage.removeItem('sessionId');
        sessionStorage.removeItem('userId');
        sessionStorage.removeItem('username');
    };

    return {
        load: function(appId,appSecret){
            return new Credentials(appId,appSecret);
        }
    }
}());

