var app = app || {};

app.userViews = (function () {
    function showLoginPage(selector) {
        $.get('templates/login.html', function (temp) {
            $(selector).html(temp);
            $("#login").on('click', function () {
                var username = $("#username").val();
                var password = $("#password").val();

                $.sammy(function () {
                    this.trigger('login', {username: username, password: password});
                });
            });
        })
    }

    function showRegisterPage(selector) {
        $.get('templates/register.html', function (temp) {
            $(selector).html(temp);
            $("#register").on('click', function () {
                var username = $("#username").val();
                var password = $("#password").val();

                $.sammy(function () {
                    this.trigger('register', {username: username, password: password});
                });
            });
        })
    }

    return {
        load: function () {
            return {
                showLoginPage: showLoginPage,
                showRegisterPage: showRegisterPage
            }
        }
    }
}());