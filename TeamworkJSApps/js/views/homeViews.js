var app = app || {};

app.homeViews = (function() {

    function showHeader(selector) {
        $.get('templates/header.html', function (templ) {
            $(selector).html(templ);
        })
    }

    function showHeaderLogged(selector,data) {
        $.get('templates/headerLogged.html', function (templ) {
            var result = Mustache.render(templ,data);
            $(selector).html(result);
        })
    }

    function showHomePage(selector) {
        $.get('templates/home.html', function (templ) {
            $(selector).html(templ);
        })
    }

    return {
        load: function() {
            return {
                showHeader: showHeader,
                showHeaderLogged: showHeaderLogged,
                showHomePage: showHomePage,
            }
        }
    }
})();