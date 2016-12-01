(function () {
  angular
    .module('peraApp')
    .service('userData', userData);
  
  userData.$inject = ['$http', 'authentication'];
  function userData ($http, authentication) {
    var getAllUsers = function () {
      return $http.get('/api/admin/users/', {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    var updateUserByCardNumber = function (cardNumber, data) {
      return $http.put('/api/admin/users/' + cardNumber, data, {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    return {
      getAllUsers: getAllUsers,
      updateUserByCardNumber: updateUserByCardNumber
    };
  };
})();
