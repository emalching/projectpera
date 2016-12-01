(function() {
  var app = angular.module('peraApp', ['ngRoute', 'ngSanitize', 'xeditable', 'pascalprecht.translate', 'ui.select', 'datetimepicker', 'ui.router']);
  
  app.run(function(editableOptions, editableThemes) {
    editableOptions.theme = 'bs3';
  });
  
  function config($locationProvider, $stateProvider, $translateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/")
    
    $urlRouterProvider.rule(function($injector, $location) {
      var path = $location.path();
      var hasTrailingSlash = path[path.length-1] === '/';
      
      if (hasTrailingSlash) {
        var newPath = path.substr(0, path.length - 1);
        return newPath;
      }
    });
    
    $stateProvider
      .state('home', {
        url: "/",
        templateUrl: '/home/home.view.html',
        controller: 'homeCtrl',
        controllerAs: 'vm'
      })
      .state('login', {
        url: "/login",
        templateUrl: '/login/login.view.html',
        controller: 'loginCtrl',
        controllerAs: 'vm'
      })
      .state('register', {
        url: "/register",
        templateUrl: '/register/register.view.html',
        controller: 'registerCtrl',
        controllerAs: 'vm'
      })
      .state('profile', {
        url: "/profile/:cardNumber",
        templateUrl: '/profile/profile.view.html',
        controller: 'profileCtrl',
        controllerAs: 'vm'
      })
      .state('timesheet', {
        url: "/timesheet",
        templateUrl: '/timesheet/timesheet.view.html',
        controller: 'timesheetCtrl',
        controllerAs: 'vm'
      })
      .state('admin', {
        url: "/admin",
        templateUrl: '/admin/admin.view.html',
        controller: 'adminCtrl',
        controllerAs: 'vm'
      })
      .state('admin.holidays', {
        url: "/holidays",
        templateUrl: '/holidays/holidays.view.html',
        controller: 'holidaysCtrl',
        controllerAs: 'vm'
      })
      .state('admin.users', {
        url: "/users",
        templateUrl: '/users/users.view.html',
        controller: 'usersCtrl',
        controllerAs: 'vm'
      })
      .state('admin.batchrun', {
        url: "/batchrun",
        templateUrl: '/batchrun/batchrun.view.html',
        controller: 'batchrunCtrl',
        controllerAs: 'vm'
      })
    
    $locationProvider.html5Mode({
      enabled: true,
      requireBase: false
    });
    
    $translateProvider
      .useStaticFilesLoader({
        prefix: '/common/translations/locale-',
        suffix: '.json'
      })
      .preferredLanguage('en');
  }
  
  angular
    .module('peraApp')
    .config(['$locationProvider', '$stateProvider', '$translateProvider', '$urlRouterProvider', config]);
})();
