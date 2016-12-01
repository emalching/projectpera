(function(){
  angular
    .module('peraApp')
    .controller('adminCtrl', adminCtrl);
  
  adminCtrl.$inject = ['$location', '$scope', '$state', 'authentication'];
  function adminCtrl ($location, $scope, $state, authentication) {
    var vm = this;
    vm.isLoggedIn = authentication.isLoggedIn();
    vm.currentUser = authentication.currentUser();
    
    if (vm.currentUser.userType != 'admin') {
      $location.path('/login');
      return;
    }
    
    $scope.$state = $state;
    
    vm.logout = function() {
      authentication.logout();
      $location.path('/login');
    };
  }
})();