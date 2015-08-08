$(function () {

    var doc = $(document),
        LEFT = 37,
        RIGHT = 39;

    $(".game").each(function (i, game) {
        var $g     = $(game),
            frames = $g.find(".frame"),
            size   = frames.length;

        function curr(value) {
            if (value !== undefined) {
                return $g.data("curr", value)
            } else {
                return $g.data("curr")
            }
        }

        $g.on("step", function (evt) {
            frames.hide();
            frames.eq(curr()).show();
        });

        $g.find(".controls button.next").click(function () {
            curr(curr() + 1);
            $g.trigger("step");
        });

        $g.find(".controls button.prev").click(function () {
            curr(curr() - 1);
            $g.trigger("step");
        });

        $g.find(".controls button.start").click(function () {
            curr(0);
            $g.trigger("step");
        });

        $g.find(".controls button.end").click(function () {
            curr(size - 1);
            $g.trigger("step");
        });

        // Keybindings
        doc.keydown(function (evt) {
            if (evt.keyCode == LEFT) {
                curr(curr() - 1);
                $g.trigger("step");
            } else if (evt.keyCode == RIGHT) {
                curr(curr() + 1);
                $g.trigger("step");
            }
        });

        $g.find(".controls button.prev").click(function () {
            curr(curr() - 1);
            $g.trigger("step");
        });

        $g.trigger("step");
    });
})
