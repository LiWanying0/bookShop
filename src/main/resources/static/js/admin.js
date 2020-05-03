$(function(){

    $("#btn-publish").click(function(){
        $("#admin-content").html(" ");
        $.get("/admin/publish", {},
            function (data, textStatus, jqXHR) {
                $("#admin-content").html(data);
            },
            "html"
        );
    });

    $("#btn-book").click(function(){
        $("#admin-content").html(" ");
        $.get("/admin/book", {},
            function (data, textStatus, jqXHR) {
                $("#admin-content").html(data);
            },
            "html"
        );
    });

    $("#btn-personZone").click(function(){
        $("#admin-content").html(" ");
        $.get("/admin/personZone", {},
            function (data, textStatus, jqXHR) {
                $("#admin-content").html(data);
            },
            "html"
        );
    });

    $("#btn-collection").click(function(){
        $("#admin-content").html(" ");
        $.get("/admin/collection", {},
            function (data, textStatus, jqXHR) {
                $("#admin-content").html(data);
            },
            "html"
        );
    });


    $("#btn-history").click(function(){
        $("#admin-content").html(" ");
        $.get("/admin/history", {},
            function (data, textStatus, jqXHR) {
                $("#admin-content").html(data);
            },
            "html"
        );
    });

    $("#btn-message").click(function(){
        $("#admin-content").html(" ");
        $.get("/admin/message", {},
            function (data, textStatus, jqXHR) {
                $("#admin-content").html(data);
            },
            "html"
        );
    });

    $("#btn-purchase").click(function(){
        $("#admin-content").html(" ");
        $.get("/admin/purchase", {},
            function (data, textStatus, jqXHR) {
                $("#admin-content").html(data);
            },
            "html"
        );
    });
});