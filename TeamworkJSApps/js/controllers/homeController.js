var app = app || {};

app.homeController = (function() {
    function HomeController(viewbag,credentials) {
        this._viewbag = viewbag;
        this.credentials = credentials;
    }

    HomeController.prototype.loadHeader = function(selector){
        this._viewbag.showHeader(selector);
    };

    HomeController.prototype.loadHeaderLogged = function(selector){
        var currentUser = this.credentials.getUsername();
        var name = {'username':currentUser};
        this._viewbag.showHeaderLogged(selector,name);
    };

    HomeController.prototype.loadHomePage = function (selector) {
        this._viewbag.showHomePage(selector);
    };

    return {
        load: function (viewBag,credentials) {
            return new HomeController(viewBag,credentials);
        }
    }
}());