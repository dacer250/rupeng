$(function() {
    Slider(); // 轮播
    be_story_hover(); // 故事悬停
})
// /<summary>
// /故事悬停
// /</summary>
function be_story_hover() {
    $('#story-list li').hover(function() {
        var hover = $(this).find('.hover-box');
        hover.stop().animate({
            top : "0px"
        }, 1000);
    }, function() {
        var hover = $(this).find('.hover-box');
        hover.stop().animate({
            top : "-281px"
        }, 1000);
    })
}

// /<summary>
// /轮播
// /</summary>
function Slider() {
    var slider = $('#slider-box').unslider({
        speed : 500, // The speed to animate each slide (in milliseconds)
        delay : 4000, // The delay between slide animations (in milliseconds)
        keys : true, // Enable keyboard (left, right) arrow shortcuts
        dots : true,
        autoplay : true,
        arrows : false,
        infinite : true
    });
    $('#slider-box').hover(function() {
        slider.unslider('stop');
    }, function() {
        slider.unslider('start');
        // slider.start();
    })
}

function loadShiPinPlayCount() {
    // 加载公开课报名人数
    $('span[activityId]').each(function() {
        // 通过闭包，避免ajax回调取activityId循环数据错误
        (function(span) {
            $.ajax({
                url : "/Activities/MembersCount?activityId=" + span.attr("activityId"),
                dataType : 'json',
                type : 'post',
                success : function(data) {
                    span.text(data.data.Count);
                },
                error : function() {
                    console.error('获取activityId="+activityId+"报名人数通信异常')
                }
            });
        }($(this)));
    });

    $('span[courseId]').each(function() {
        // 通过闭包，避免ajax回调取activityId循环数据错误
        (function(span) {
            $.ajax({
                url : "/Courses/GetCoursePlayCount/" + span.attr("courseId"),
                dataType : 'json',
                type : 'post',
                success : function(data) {
                    span.text(data);
                },
                error : function() {
                    console.error('获取courseId="+courseId+"学习人数通信异常')
                }
            });
        }($(this)));
    });

    $('span[chapterId]').each(function() {
        // 通过闭包，避免ajax回调取activityId循环数据错误
        (function(span) {
            $.ajax({
                url : "/Courses/GetChapterPlayCount/" + span.attr("chapterId"),
                dataType : 'json',
                type : 'post',
                success : function(data) {
                    span.text(data);
                },
                error : function() {
                    console.error('获取chapterId="+chapterId+"学习人数通信异常')
                }
            });
        }($(this)));
    });
}