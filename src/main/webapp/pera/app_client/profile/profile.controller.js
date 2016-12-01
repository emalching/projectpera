(function () {
  angular
    .module('peraApp')
    .controller('profileCtrl', profileCtrl);
  
  profileCtrl.$inject = ['$scope', '$stateParams', '$location', 'staffData', 'userData'];
  function profileCtrl($scope, $stateParams, $location, staffData, userData) {
    var vm = this;
    vm.cardNumber = $stateParams.cardNumber;
    vm.pageHeader = {
      title: 'User Profile',
    };
    vm.message = "Loading your user profile...";
    vm.loadError = "";
    
    staffData
      .getStaffByCardNumber(vm.cardNumber)
      .success(function (data) {
        vm.message =  "";
        if (data[0].cardNumber != "") {
          vm.data = { staff: data[0] };
          if (!vm.data.staff.employeeName) {
            vm.data.staff.employeeName = vm.data.staff.firstName + ' ' + vm.data.staff.lastName;
          }
        }
        else {
          vm.loadError = "User Profile not found.";
        }
      })
      .error(function (err) {
        vm.message =  "";
        vm.loadError = err.message;
      });
    
    userData
      .getAllUsers()
      .success(function (data) {
        vm.users = data;
      })
      .error(function (err) {
        vm.message = err.message;
      });
    
    $scope.updateProfile = function () {
      staffData
        .updateStaffByCardNumber(vm.cardNumber, {
          employeeName: vm.data.staff.employeeName,
          project: vm.data.staff.project,
          department: vm.data.staff.department,
          position: vm.data.staff.position,
          teamLeadName: vm.data.staff.teamLeadName,
        })
        .success(function (data) {
        })
        .error(function (err) {
          vm.formError = "Your profile has not been updated, please try again.";
        });
      
      return false;
    };
    
    $scope.isEmpty = function(fieldName, fieldValue) {
      if (angular.isUndefined(fieldValue) || fieldValue === null || fieldValue.length <= 0) {
        return fieldName + " is required.";
      }
    };
    
    vm.goBack = function() {
      $location.path('/');
    };
  }
})();
