(function () {
  angular
    .module('peraApp')
    .controller('registerCtrl', registerCtrl);
  
  registerCtrl.$inject = ['$location','authentication'];
  function registerCtrl($location, authentication) {
    var vm = this;
    
    vm.credentials = {
      username: "",
      cardNumber: ""
    };
    
    vm.returnPage = $location.search().page || '/';
    
    vm.onSubmit = function () {
      vm.formError = "";
      if (!vm.credentials.username || !vm.credentials.cardNumber) {
        vm.formError = "All fields required, please try again.";
        return false;
      } else {
        vm.doRegister();
      }
    };
    
    vm.doRegister = function () {
      vm.formError = "";
      authentication
        .register(vm.credentials)
        .error(function (err) {
            vm.formError = err.message;
        })
        .then(function () {
          alert('Your card number has been registered successfully. Please continue logging in.');
          $location.search('page', null); 
          $location.path(vm.returnPage);
        });
    };
  }
})();
