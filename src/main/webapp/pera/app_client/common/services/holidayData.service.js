(function () {
  angular
    .module('peraApp')
    .service('holidayData', holidayData);
  
  holidayData.$inject = ['$http', 'authentication'];
  function holidayData ($http, authentication) {
    var getAllHolidays = function () {
      return $http.get('/api/admin/holidays', {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    var updateHolidays = function (data) {
      return $http.put('/api/admin/holidays', data, {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    return {
      getAllHolidays: getAllHolidays,
      updateHolidays: updateHolidays
    };
  };
})();
