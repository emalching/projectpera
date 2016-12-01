(function(){
  angular
    .module('peraApp')
    .controller('usersCtrl', usersCtrl);
  
  usersCtrl.$inject = ['$scope', '$location', '$filter', 'userData', 'authentication'];
  function usersCtrl ($scope, $location, $filter, userData, authentication) {
    var vm = this;
    vm.currentUser = authentication.currentUser();
    
    if (vm.currentUser.userType != 'admin') {
      $location.path('/login');
      return;
    }
    
    vm.pageHeader = {
      title: 'List of Users',
    };
    vm.message = "Loading the list of users...";
    vm.formError = "";
    
    vm.userTypes = [
      {value: 'user', text: 'user'},
      {value: 'admin', text: 'admin'}
    ];
    
    userData
      .getAllUsers()
      .success(function (data) {
        vm.message =  "";
        vm.data = data;
      })
      .error(function (err) {
        vm.message = err.message;
      });
    
    $scope.saveUser = function (data, cardNumber) {
      return userData
        .updateUserByCardNumber(cardNumber, {
          userType: data.userType
        })
        .success(function (userData) {
        })
        .error(function (err) {
          vm.formError = "User with card number " + cardNumber + " has not been updated, please try again.";
        });
    };
    
    vm.goToProfile = function(cardNumber) {
      $location.path('/profile/' + cardNumber);
    };
    
    vm.goToTimesheet = function() {
      $location.path('/timesheet');
    };
    
    vm.goBack = function() {
      $location.path('/');
    };
  }
})();