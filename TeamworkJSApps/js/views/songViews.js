var app = app || {};

app.songViews = (function() {

    function showSongs(selector,data) {
        $.get('templates/songTemplate2.html', function (templ) {
            var result = Mustache.render(templ,data);
            $(selector).html(result);
        })
    }

    function showHomePageSongs(selector,data) {
        $.get('templates/songsHomePage.html', function (templ) {
            var result = Mustache.render(templ,data);
            $(selector).html(result);
            $(".btn-get-song").on('click', function() {
                var data = prompt("Enter value: ");
                $.sammy(function () {
                    this.trigger('initiated-song', { id: data } );
                });
            });
        })
    }

    function showPlaylistSongs(selector,data) {
        $.get('templates/songsList.html', function (templ) {
            var result = Mustache.render(templ,data);
            $(selector).html(result);
        });

        $("#createPlaylist").on('click', function () {
            var title = $("#title").val();
            var author = $("#author").val();
            var songs = [];
            var checkedBoxes = $(".include");
            $.each(checkedBoxes,function () {
                if($(this).is(":checked")){
                    songs.push({'_type': "KinveyRef", '_id': $(this).val(), '_collection': "songs"});
                }
            });

            $.sammy(function () {
                this.trigger('addPlaylist', {title: title, author: author, songs: songs})
            })
        })
    }

    var initialised = false;
    function showSongPage(selector, data) {
        var url = data.response._downloadURL;
        $.get('templates/songTemplate.html', function (templ) {
            $(selector).html(Mustache.render(templ, data));
            $("#play-pause-button").on('click', function(event) {
                console.log(app.equalizer);
                if(!initialised){
                    app.equalizer.init(url);
                    initialised = true;
                    $("#play-pause-button").html("pause");
                }
                else{
                    console.log(app.equalizer);
                    if(app.equalizer.audioContext.state === 'running') {
                        app.equalizer.audioContext.suspend().then(function() {
                            $("#play-pause-button").html("pause");
                        });
                    }
                    else if(app.equalizer.audioContext.state === 'suspended') {
                        app.equalizer.audioContext.resume().then(function() {
                            $("#play-pause-button").html("play");
                        });
                    }
                }
            });
            $("#download-button").on('click', function(event) {
                var link = document.createElement("a");
                link.download = url.substr(url.lastIndexOf('/'));
                link.href = url;
                link.click();
            })
        })
    }

    return {
        load: function() {
            return {
                showPlaylistSongs:showPlaylistSongs,
                showSongs:showSongs,
                showHomePageSongs:showHomePageSongs,
                showSongPage:showSongPage
            }
        }
    }
})();