$(function(){

    var loadSectionData = function(data) {
        $('section.content').fadeOut('fast', function(){
            $(this).html(data).fadeIn('fast');
        });
    }

    var alertWithError = function(error) {
        console.log(error);
    }

    var updateRightsInNavbar = function() {

        var userIsLogin = $('section').attr('login');

        var userProfileDiv = $('nav li#user-profile-li');
        var signoutDiv = $('nav li#signout-li');
        var signupDiv =  $('nav li#signup-li');
        var loginDiv =  $('nav li#login-li');

        if(userIsLogin === 'true') {
            userProfileDiv.css({'display': 'inline-block'});
            signoutDiv.css({'display': 'inline-block'});
            signupDiv.css({'display': 'none'});
            loginDiv.css({'display': 'none'});
        } else {
            userProfileDiv.css({'display': 'none'});
            signoutDiv.css({'display': 'none'});
            signupDiv.css({'display': 'inline-block'});
            loginDiv.css({'display': 'inline-block'});
        }

    }

    var loadHome = function() {

        var ajaxCallBack = {
            method: 'GET',
            success: function(data){ loadSectionData(data) },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.HomeDataController.homeData().ajax(ajaxCallBack);

    }

    loadHome(); //Load home content


    var loadSignupForm = function() {

        var ajaxCallBack = {
            method: 'GET',
            success: function(data){ loadSectionData(data) },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.SignupController.showSignupForm().ajax(ajaxCallBack);
    }

    var loadLoginForm = function() {

        var ajaxCallBack = {
            method: 'GET',
            success: function(data){ loadSectionData(data); },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.LoginController.showLogin().ajax(ajaxCallBack);
    }

    var submitLoginForm = function(form) {

        var ajaxCallBack = {
            method: 'POST',
            data: form.serialize(),
            success: function(data){
                loadSectionData(data);
                $('section').attr('login','true');
            },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.LoginController.login().ajax(ajaxCallBack);
    }

    var suspendUser = function(emailOfUser) {

        var ajaxCallBack = {
            success: function(data){ loadSectionData(data) },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.UserManagementController.suspend(emailOfUser).ajax(ajaxCallBack);
    }

    var resumeUser = function(emailOfUser) {

        var ajaxCallBack = {
            success: function(data){ loadSectionData(data) },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.UserManagementController.resume(emailOfUser).ajax(ajaxCallBack);
    }

    var signOutUser = function() {

        var ajaxCallBack = {
            success: function(data) {
                loadSectionData(data);
                $('section').attr('login','false');
            },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.SignoutController.logout().ajax(ajaxCallBack);
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
            success: function(data){
                loadSectionData(data);
                $('section').attr('login','true');
            },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.SignupController.handleSignupForm().ajax(ajaxCallBack);
    }

    var showProfile = function() {

        var ajaxCallBack = {
            success: function(data){ loadSectionData(data) },
            error: function(error){ alertWithError(error) },
            complete: function(){ updateRightsInNavbar() }
        }

        jsRoutes.controllers.ProfileController.showProfile().ajax(ajaxCallBack);
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

    $('section.content').on('click', 'a.suspend',function(event){
        event.preventDefault();
        event.stopPropagation();

        suspendUser();
        return false;
    });

    $('section.content').on('click', 'a.resume',function(event){
        event.preventDefault();
        event.stopPropagation();

        var emailOfUser = $(this).attr('href').substring(8);

        resumeUser(emailOfUser);
        return false;
    });

    $('section.content').on('click', 'a.suspend',function(event){
        event.preventDefault();
        event.stopPropagation();

        var emailOfUser = $(this).attr('href').substring(9);

        suspendUser(emailOfUser);
        return false;
    });


    $('nav').on('click', 'a#signout-nav-link',function(event){
        event.preventDefault();
        event.stopPropagation();

        signOutUser();
        return false;
    });

    $('nav').on('click', 'a#signout-nav-link',function(event){
        event.preventDefault();
        event.stopPropagation();

        signOutUser();
        return false;
    });

    $('nav').on('click', 'a.home-data-link',function(event){
        event.preventDefault();
        event.stopPropagation();

        loadHome();
        return false;
    });

    $('nav').on('click', 'a#user-profile-nav-link',function(event){
        event.preventDefault();
        event.stopPropagation();

        showProfile();
        return false;
    });

});
