var app = app || {};

app.userController = (function () {
    function UserController(model, viewbag, credentials) {
        this._model = model;
        this._viewbag = viewbag;
        this.credentials = credentials;
    }

    UserController.prototype.loadLoginPage = function(selector){
        this._viewbag.showLoginPage(selector);
    };

    UserController.prototype.loadRegisterPage = function(selector){
        this._viewbag.showRegisterPage(selector);
    };

    UserController.prototype.login = function (data) {
        var _this = this;
        this._model.login(data)
            .then(function (success) {
                _this.credentials.setSession(success._kmd.authtoken);
                _this.credentials.setUserId(success._id);
                _this.credentials.setUsername(success.username);
                poppy.pop("success","Login Successful","You have successfully logged in!");
                $.sammy(function () {
                    this.trigger('redirectUrl', {url: '#/home'});
                })
            },function(error){
                poppy.pop("error","Error","Invalid username or password");
            })
    };

    UserController.prototype.logout = function () {
        var _this = this;
        this._model.logout()
            .then(function (success) {
                _this.credentials.clear();
                poppy.pop("success","Success","You have successfully logged out!");
                $.sammy(function () {
                    this.trigger('redirectUrl', {url: '#/'});
                })
            })
    };

    UserController.prototype.register = function(data){
        this._model.register(data)
            .then(function(success){
                poppy.pop("success","Success","You registered successfully!");
                $.sammy(function(){
                    this.trigger('redirectUrl',{url:'#/login'});
                })
            },function(error){
                poppy.pop("error","Error","Username already taken!");
            })
    };

    return {
        load: function(model,viewbag,credentials){
            return new UserController(model,viewbag,credentials);
        }
    }

}());