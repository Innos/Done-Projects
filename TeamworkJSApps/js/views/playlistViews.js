var app = app || {};

app.playlistsViews = (function () {
    function showPlaylists(selector, data) {
        $.get('templates/playLists.html', function (temp) {
            var result = Mustache.render(temp, data);
            $(selector).html(result);

            $(selector + " tbody>tr").on('click', function () {
                var id = $(this).attr('data-id');
                $.sammy(function () {
                    this.trigger('loadPlaylist', {'id': id});
                })
            });

            $("#create").on('click', function () {
                $.sammy(function () {
                    this.trigger('createPlaylistSongs');
                })
            });
        })
    }

    function createPlaylist(selector, data) {
        $.get('templates/createPlaylist.html', function (temp) {
            $(selector).html(temp);
        });
    };

    function showPlaylist(selector, data) {
        $.get('templates/playList.html', function (temp) {
            var result = Mustache.render(temp, data);
            $(selector).html(result);
        })
    };

    function showPersonalPlaylist(selector) {
        $.get('templates/personalPlaylist.html', function (temp) {
            var result = Mustache.render(temp, null);
            $(selector).html(result);
        })
    };

    function showPersonalPlaylists(selector, data) {
        $.get('templates/personalPlaylists.html', function (temp) {
            var result = Mustache.render(temp, data);
            $(selector).html(result);

            $('.delete').on('click', function () {
                var element = $(this).parent();
                var id = element.attr('data-id');
                $.sammy(function () {
                    this.trigger('deletePlaylist', {id: id})
                })
            });

            $('.edit').on('click', function () {
                var element = $(this).parent();
                var id = element.attr('data-id');
                $.sammy(function () {
                    this.trigger('editPlaylist', {id: id})
                })
            });

            $('#create').on('click', function () {
                $.sammy(function () {
                    this.trigger('createPlaylistSongs');
                })
            });
        })
    };

    return {
        load: function () {
            return {
                showPlaylists: showPlaylists,
                showPlaylist: showPlaylist,
                createPlaylist: createPlaylist,
                showPersonalPlaylists: showPersonalPlaylists,
                showPersonalPlaylist: showPersonalPlaylist
            }
        }
    }
})();
