var app = app || {};

(function() {

    app.router = Sammy(function () {

        var baseUrl = "https://baas.kinvey.com";
        var appId = "kid_Z19LWTj4JW";
        var appSecret = "a6abc3d9c01c4adf8b42bf866611e195";

        var header = "#header";
        var selector = '#container';
        var songContainer = "#songContainer";

        var requester = app.requester.load();

        var credentials = app.credentials.load(appId,appSecret);
        var userModel = app.user.load(baseUrl,appId,credentials,requester);
        var playlistModel = app.playlistModel.load(baseUrl,appId,credentials,requester);
        var songModel = app.songModel.load(baseUrl,appId,credentials,requester);

        var homeView = app.homeViews.load();
        var userView = app.userViews.load();
        var songView = app.songViews.load();
        var playlistView = app.playlistsViews.load();
        
        var homeController = app.homeController.load(homeView,credentials);
        var userController = app.userController.load(userModel,userView,credentials);
        var playlistController = app.playlistController.load(playlistModel,playlistView,credentials);
        var songController = app.songController.load(songModel,songView,credentials);

        this.before({except: {path: '#\/(register|login)?'}},function(){
            var sessionId = sessionStorage.getItem('sessionId');
            if(!sessionId){
                this.redirect("#/login");
                return false;
            }
        });

        this.get('#/', function () {
            homeController.loadHeader(header);
            homeController.loadHomePage(selector);
        });

        this.get('#/songs', function () {
            homeController.loadHeaderLogged(header);
            songController.loadSongPage(selector);
        });

        this.get('#/home', function () {
            homeController.loadHeaderLogged(header);
            songController.loadHomePageSongs(selector);
        });

        this.get('#/playlists', function () {
            homeController.loadHeaderLogged(header);
            playlistController.loadPlaylistsPage(selector);
        });

        this.get('#/playlist', function () {
            homeController.loadHeaderLogged(header);
        });

        this.get('#/login', function () {
            homeController.loadHeader(header);
            userController.loadLoginPage(selector);
        });

        this.get('#/logout', function () {
            userController.logout();
            homeController.loadHeader(header);
        });

        this.get('#/register', function () {
            homeController.loadHeader(header);
            userController.loadRegisterPage(selector);
        });

        this.get('#/myPlaylists', function () {
            homeController.loadHeaderLogged(header);
            playlistController.loadMyPlaylists(selector);
        });

        this.bind('redirectUrl',function(e,data){
            this.redirect(data.url);
        });

        this.bind('login',function(e,data){
            userController.login(data);
        });

        this.bind('register',function(e,data){
            userController.register(data);
        });

        this.bind('loadPlaylist',function(e,data){
            playlistController.getPlaylist(selector,data.id);
        });

        this.bind('createPlaylistSongs',function(e){
            playlistController.loadCreatePlaylist(selector);
            songController.createPlaylistSongs(songContainer);
        });

        this.bind('addPlaylist',function(e,data){
            playlistController.addPlaylist(data);
        });

        this.bind('deletePlaylist',function(e,data){
            playlistController.deletePlaylist(data);
        });

        this.bind('song-gotten', function(e, data) {
            songController.loadSongPage(selector, data);
        });

        this.bind('initiated-song', function(e, data) {
            songController.getSong(data.id);
        });

    });

    app.router.run('#/');

})();