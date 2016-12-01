(function(){
  angular
    .module('peraApp')
    .controller('batchrunCtrl', batchrunCtrl);
  
  batchrunCtrl.$inject = ['$scope', '$location', '$filter', 'batchrunData', 'authentication'];
  function batchrunCtrl ($scope, $location, $filter, batchrunData, authentication) {
    var vm = this;
    vm.currentUser = authentication.currentUser();
    
    if (vm.currentUser.userType != 'admin') {
      $location.path('/login');
      return;
    }
    
    vm.pageHeader = {
      title: 'Batch Run History',
    };
    vm.message = "Loading the batch run history...";
    vm.formError = "";
    
    batchrunData
      .getAllBatchrun()
      .success(function (data) {
        vm.message =  "";
        vm.data = data;
      })
      .error(function (err) {
        vm.message = err.message;
      });
    
    vm.goBack = function() {
      $location.path('/');
    };
  }
})();