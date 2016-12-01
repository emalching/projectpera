(function(){
  angular
    .module('peraApp')
    .controller('homeCtrl', homeCtrl);
  
  homeCtrl.$inject = ['$location', 'authentication'];
  function homeCtrl ($location, authentication) {
    var vm = this;
    vm.isLoggedIn = authentication.isLoggedIn();
    vm.currentUser = authentication.currentUser();
    
    if (!vm.isLoggedIn) {
      $location.path('/login');
      return;
    }
    
    vm.goToProfile = function() {
      $location.path('/profile/' + vm.currentUser.cardNumber);
    };
    
    vm.goToTimesheet = function() {
      $location.path('/timesheet');
    };
    
    vm.goToAdmin = function() {
      $location.path('/admin');
    };
    
    var totalButtons = 4;
    var totalAdminButtons = 2;
    vm.grid = 3;
    if (vm.currentUser.userType != 'admin') {
      displayedButtons = totalButtons - totalAdminButtons;
      if (displayedButtons == 3) {
        vm.grid = 4;
      }
      else if (displayedButtons == 2) {
        vm.grid = 6;
      }
    }
  }
})();