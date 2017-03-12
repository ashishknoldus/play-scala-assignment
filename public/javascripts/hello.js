$(function(){

    var loadSignupForm = function() {

        var ajaxCallBack = {
            method: 'GET',
            success: function(data) {
                $('section.content').fadeOut('fast', function(){
                      $(this).html(data).fadeIn('fast');
                  });
            },
            error: function(error) {
                alert(error);
            }
        }
        jsRoutes.controllers.SignupController.showSignupForm().ajax(ajaxCallBack);
    }

    var loadLoginForm = function() {

        var ajaxCallBack = {
            method: 'GET',
            success: function(data) {
                $('section.content').fadeOut("fast", function(){
                    $(this).html(data).fadeIn('fast');
                });
            },
            error: function(error) {
                alert(error);
            }
        }
        jsRoutes.controllers.LoginController.showLogin().ajax(ajaxCallBack);
    }

    var submitLoginForm = function(form) {

        var ajaxCallBack = {
            method: 'POST',
            data: form.serialize(),
            success: function(data) {

                $('section.content').fadeOut('fast', function(){
                      $(this).html(data).fadeIn('fast');
                  });
            },
            error: function(error) {
                alert(error);
            }
        }
        jsRoutes.controllers.LoginController.login().ajax(ajaxCallBack);
    }

    var submitSignupForm = function(form) {

        //FormData for multipart data
        //Not widely supported
        //Ref : https://stackoverflow.com/questions/5392344/sending-multipart-formdata-with-jquery-ajax

        data = new  FormData(jQuery(form)[0])

        var ajaxCallBack = {
            method: 'POST',
            data: data,
            contentType: false,
            processData: false,
            success: function(data) {

                $('section.content').fadeOut('fast', function(){
                      $(this).html(data).fadeIn('fast');
                  });
            },
            error: function(error) {
                alert(error);
            }
        }

        jsRoutes.controllers.SignupController.handleSignupForm().ajax(ajaxCallBack);
    }

    $('a#signup-nav-link').click( function(event){

        event.preventDefault();
        event.stopPropagation();

        loadSignupForm();
        return false;
    });

    $('a#login-nav-link').click( function(event){

        event.preventDefault();
        event.stopPropagation();

        loadLoginForm();
        return false;
    });

    $('section.content').on('submit', 'form#login-form',function(event){
        event.preventDefault();

        submitLoginForm($(this));
        return false;
    });

    $('section.content').on('submit', 'form#signup-form',function(event){
        event.preventDefault();

        submitSignupForm($(this));
        return false;
    });

});
