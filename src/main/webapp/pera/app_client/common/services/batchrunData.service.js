(function () {
  angular
    .module('peraApp')
    .service('batchrunData', batchrunData);
  
  batchrunData.$inject = ['$http', 'authentication'];
  function batchrunData ($http, authentication) {
    var getAllBatchrun = function () {
      return $http.get('/api/admin/batchrun/', {
        headers: {
          Authorization: 'Bearer ' + authentication.getToken()
        }
      });
    };
    
    return {
      getAllBatchrun: getAllBatchrun
    };
  };
})();
