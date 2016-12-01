(function () {
  angular
    .module('peraApp')
    .service('staffData', staffData);
  
  staffData.$inject = ['$http', 'authentication'];
  function staffData ($http, authentication) {
    var getStaffByCardNumber = function (cardNumber) {
      return $http.get('/api/profile/' + cardNumber, {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    var updateStaffByCardNumber = function (cardNumber, data) {
      return $http.put('/api/profile/' + cardNumber, data, {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    var updateTimesheetByCardNumber = function (cardNumber, data) {
      return $http.put('/api/timesheet/' + cardNumber, data, {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    return {
      getStaffByCardNumber: getStaffByCardNumber,
      updateStaffByCardNumber: updateStaffByCardNumber,
      updateTimesheetByCardNumber: updateTimesheetByCardNumber
    };
  };
})();
