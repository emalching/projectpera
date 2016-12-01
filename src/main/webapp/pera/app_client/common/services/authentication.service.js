(function () {
  angular
    .module('peraApp')
    .service('authentication', authentication);
  
  authentication.$inject = ['$http', '$window'];
  function authentication($http, $window) {
    var saveToken = function (token) {
      $window.localStorage['pera-token'] = token;
    };
    
    var getToken = function () {
      return $window.localStorage['pera-token'];
    };
    
    var isLoggedIn = function () {
      var token = getToken();
      if (token) {
        var payload = JSON.parse($window.atob(token.split('.')[1]));
        return payload.exp > Date.now() / 1000;
      } else {
        return false;
      }
    };
    
    var currentUser = function () {
      if (isLoggedIn()) {
        var token = getToken();
        var payload = JSON.parse($window.atob(token.split('.')[1]));
        return {
          username: payload.username,
          cardNumber: payload.cardNumber,
          emailAddress: payload.emailAddress,
          userType: payload.userType
        };
      }
    };
    
    login = function (user) {
      return $http.post('/api/login', user).success(function (data) {
        saveToken(data.token);
      });
    };
    
    register = function (user) {
      return $http.post('/api/register', user).success(function (data){
      });
    };
    
    logout = function () {
      $window.localStorage.removeItem('pera-token');
    };
    
    return {
      saveToken: saveToken,
      getToken: getToken,
      isLoggedIn: isLoggedIn,
      currentUser: currentUser,
      login: login,
      register: register,
      logout: logout
    };
  }
})();
