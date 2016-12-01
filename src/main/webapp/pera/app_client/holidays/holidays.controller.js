(function(){
  angular
    .module('peraApp')
    .controller('holidaysCtrl', holidaysCtrl);
  
  holidaysCtrl.$inject = ['$scope', '$location', '$filter', 'holidayData', 'authentication'];
  function holidaysCtrl ($scope, $location, $filter, holidayData, authentication) {
    var vm = this;
    vm.currentUser = authentication.currentUser();
    
    if (vm.currentUser.userType != 'admin') {
      $location.path('/login');
      return;
    }
    
    vm.pageHeader = {
      title: 'List of Holidays',
    };
    vm.message = "Loading the list of holidays...";
    vm.formError = "";
    
    holidayData
      .getAllHolidays()
      .success(function (data) {
        vm.message =  "";
        vm.data = [];
        if (data.length != 0) {
          vm.data = data[0].holidayList;
        }
        
        $scope.originalHolidayList = angular.copy(vm.data);
        generateYearRange(vm.data);
      })
      .error(function (err) {
        vm.message = err.message;
      });
    
    vm.yearRange = [];
    
    generateYearRange = function(data) {
      var length = data.length;
      var startYear = new Date(data[0].date).getFullYear();
      var endYear = new Date(data[length-1].date).getFullYear();
      
      var currentYear = new Date().getFullYear();
      if (currentYear < startYear) {
        startYear = currentYear;
      }
      
      for (var i = 0; startYear <= endYear; startYear++) {
        vm.yearRange.push(startYear);
        
        if (currentYear >= startYear && currentYear <= endYear) {
          vm.selectedYear = vm.yearRange[i];
        }
        
        i++;
      }
    };
    
    vm.criteriaMatch = function(selectedYear) {
      return function(item) {
        if (item.isDeleted) {
          return false;
        }
        
        return true;
        
        /*itemYear = new Date(item.date).getFullYear();
        
        if (itemYear == selectedYear || $scope.tableform.$visible) {
          return true;
        }*/
      };
    };
    
    $scope.cancel = function() {
      vm.data = angular.copy($scope.originalHolidayList);
      vm.formError = "";
    };
    
    $scope.isValidRow = function(row, inputHoliday) {
      var inputCount = 0;
      if (row.date) {
        inputCount++;
      }
      if (inputHoliday) {
        inputCount++;
      }
      if (inputCount != 2 && inputCount != 0) {
        vm.formError = "Date and Holiday must all be provided.";
        return " ";
      }
    };
    
    $scope.updateHolidays = function() {
      for (var i = vm.data.length; i--;) {
        var holiday = vm.data[i];
        if (holiday.isDeleted) {
          vm.data.splice(i, 1);
        }
        
        if (holiday.isNew) {
          holiday.isNew = false;
        }
      }
      
      vm.data = $filter('orderBy')(vm.data, 'date', false)
      
      holidayData
        .updateHolidays({
          holidayList: vm.data
        })
        .success(function (data) {
        })
        .error(function (err) {
          vm.formError = "Holiday list has not been updated, please try again."
        });
      
      vm.yearRange = [];
      generateYearRange(vm.data);
      
      return false;
    };
    
    $scope.add = function() {
      vm.data.push({
        date: '',
        holiday: '',
        isNew: true
      });
    };
    
    $scope.delete = function(index) {
      vm.data[index].isDeleted = true;
    };
    
    vm.goBack = function() {
      $location.path('/');
    };
  }
})();