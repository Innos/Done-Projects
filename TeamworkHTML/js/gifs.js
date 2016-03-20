$(document).ready(function() {
    $('.next-gif').click(function() {
        var caller = this.id.slice(-1);
        var elems = document.getElementsByClassName('shown-gif');
        var previous = "#" + elems[0].id;
        var id = "#gif-article" + caller; 
        $(previous).animate({
            left: '-500%'

        },
        {
            duration: 500,
            easing: 'linear',
            complete: function() {
                $(previous).css('left', '200%');
                $(previous).appendTo('#gifs-main-content');
                $(previous).css('diplay', 'none');
            }
        });

        $(id).animate({
            left: '0%'
        }, 500);

        $(previous).removeClass('shown-gif');
        $(previous).addClass('hidden-gif');
        $(id).removeClass('hidden-gif');
        $(id).addClass('shown-gif');
    });
});