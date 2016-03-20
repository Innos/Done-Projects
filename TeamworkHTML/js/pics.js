$(document).ready(function() {
    $('.next-image').click(function() {
        var caller = this.id.slice(-1);
        var elems = document.getElementsByClassName('shown-picture');
        var previous = "#" + elems[0].id;
        var id = "#article" + caller; 
        $(previous).animate({
            left: '-500%'

        },
        {
            duration: 500,
            easing: 'linear',
            complete: function() {
                $(previous).css('left', '200%');
                $(previous).appendTo('#pictures-main-content');
                $(previous).css('diplay', 'none');
            }
        });

        $(id).animate({
            left: '0%'
        }, 500);

        $(previous).removeClass('shown-picture');
        $(previous).addClass('hidden-picture');
        $(id).removeClass('hidden-picture');
        $(id).addClass('shown-picture');
    });
});